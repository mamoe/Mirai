/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.internal.network.components

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.mamoe.mirai.internal.QQAndroidBot
import net.mamoe.mirai.internal.network.component.ComponentKey
import net.mamoe.mirai.internal.network.component.ComponentStorage
import net.mamoe.mirai.internal.network.handler.NetworkHandler
import net.mamoe.mirai.internal.network.handler.NetworkHandler.State
import net.mamoe.mirai.internal.network.handler.selector.NetworkException
import net.mamoe.mirai.internal.network.handler.state.JobAttachStateObserver
import net.mamoe.mirai.internal.network.handler.state.StateObserver
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Facade of [ContactUpdater], [OtherClientUpdater], [ConfigPushSyncer].
 * Handles initialization jobs after successful logon.
 *
 * Attached to handler state [NetworkHandler.State.LOADING] [as state observer][asObserver] in [QQAndroidBot.stateObserverChain].
 */
internal interface BotInitProcessor {
    suspend fun init()

    companion object : ComponentKey<BotInitProcessor>
}

internal fun BotInitProcessor.asObserver(targetState: State = State.LOADING): StateObserver {
    return JobAttachStateObserver("BotInitProcessor.init", targetState) { init() }
}


internal class BotInitProcessorImpl(
    private val bot: QQAndroidBot,
    private val context: ComponentStorage,
    private val logger: MiraiLogger,
) : BotInitProcessor {

    private val initialized = atomic(false)

    override tailrec suspend fun init() {
        if (initialized.value) return
        if (!initialized.compareAndSet(expect = false, update = true)) return init()

        check(bot.isActive) { "bot is dead therefore network can't init." }
        context[ContactUpdater].closeAllContacts(CancellationException("re-init"))

        val registerResp =
            context[SsoProcessor].registerResp ?: error("Internal error: registerResp is not yet available.")

        // do them parallel.
        context[MessageSvcSyncer].startSync()
        context[BdhSessionSyncer].loadFromCache()


        coroutineScope {
            launch { runWithCoverage { context[OtherClientUpdater].update() } }
            launch { runWithCoverage { context[ContactUpdater].loadAll(registerResp.origin) } }
        }

        bot.components[SsoProcessor].firstLoginSucceed = true
    }

    private inline fun runWithCoverage(block: () -> Unit) {
        try {
            block()
        } catch (e: NetworkException) {
            logger.warning(
                "An NetworkException was thrown during initialization process of Bot ${bot.id}. " +
                        "This means your network is unstable at this moment, " +
                        "or the server has closed the connection due to some reason (you will see the cause if further trials are all failed). " +
                        "Halting the log-in process to wait for a while to reconnect..."
            )
            throw e
        } catch (e: Throwable) {
            logger.warning(
                "An exception was thrown during initialization process of Bot ${bot.id}. " +
                        "Trying to ignore the error and continue logging in...",
                e
            )
        }
    }

}

