/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.console.testFramework

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class AbstractConsoleInstanceTest {
    val mockPlugin by lazy { mockKotlinPlugin() }
    private lateinit var implementation: MiraiConsoleImplementation
    val consoleImplementation: MiraiConsoleImplementation by ::implementation

    @BeforeEach
    protected open fun initializeConsole() {
        this.implementation = MockConsoleImplementation().apply { start() }
        CommandManager
    }

    @AfterEach
    protected open fun stopConsole() {
        if (MiraiConsoleImplementation.instanceInitialized) {
            try {
                runBlocking { MiraiConsole.job.cancelAndJoin() }
            } catch (e: CancellationException) {
                // ignored
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                MiraiConsoleImplementation.instance = null
            }
        }
    }

    companion object {
        fun mockKotlinPlugin(id: String = "org.test.test"): KotlinPlugin {
            return object : KotlinPlugin(JvmPluginDescription(id, "1.0.0")) {}
        }
    }
}