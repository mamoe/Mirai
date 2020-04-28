/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE", "FunctionName", "unused")

package net.mamoe.mirai.message

import kotlinx.coroutines.Job
import net.mamoe.mirai.Bot
import net.mamoe.mirai.JavaFriendlyAPI
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.recallIn
import net.mamoe.mirai.utils.MiraiExperimentalAPI
import net.mamoe.mirai.utils.MiraiInternalAPI
import net.mamoe.mirai.utils.PlannedRemoval
import net.mamoe.mirai.utils.internal.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * 发送消息后得到的回执. 可用于撤回.
 *
 * 此对象持有 [Contact] 的弱引用, [Bot] 离线后将会释放引用, 届时 [target] 将无法访问.
 *
 * @param source 指代发送出去的消息
 * @param target 消息发送对象
 *
 * @see Group.sendMessage 发送群消息, 返回回执（此对象）
 * @see QQ.sendMessage 发送群消息, 返回回执（此对象）
 * @see Member.sendMessage 发送临时消息, 返回回执（此对象）
 *
 * @see MessageReceipt.sourceId 源 id
 * @see MessageReceipt.sourceTime 源时间
 */
@OptIn(MiraiInternalAPI::class)
open class MessageReceipt<out C : Contact>(
    /**
     * 指代发送出去的消息.
     */
    val source: OnlineMessageSource.Outgoing,
    /**
     * 发送目标, 为 [Group] 或 [QQ] 或 [Member]
     */
    val target: C,

    @MiraiExperimentalAPI
    val botAsMember: Member?
) {
    /**
     * 是否为发送给群的消息的回执
     */
    val isToGroup: Boolean = target is Group

    @JavaFriendlyAPI
    @JvmName("quoteReply")
    fun __quoteReplyBlockingForJava__(message: Message): MessageReceipt<C> {
        return runBlocking { return@runBlocking quoteReply(message) }
    }

    @JavaFriendlyAPI
    @JvmName("quoteReply")
    fun __quoteReplyBlockingForJava__(message: String): MessageReceipt<C> {
        return runBlocking { quoteReply(message) }
    }

    @JavaFriendlyAPI
    @JvmName("recall")
    fun __recallBlockingForJava__() {
        return runBlocking { recall() }
    }

    @JavaFriendlyAPI
    @JvmName("recallIn")
    fun __recallInBlockingForJava__(timeMillis: Long): Job {
        return recallIn(timeMillis = timeMillis)
    }

    @JavaFriendlyAPI
    @JvmName("quote")
    fun __quoteBlockingForJava__(): QuoteReply {
        return this.quote()
    }


    @PlannedRemoval("1.0.0")
    @Deprecated("for binary compatibility", level = DeprecationLevel.HIDDEN)
    @JvmSynthetic
    @JavaFriendlyAPI
    @JvmName("recall")
    fun __recallInBlockingForJava__2(timeMillis: Long): Job {
        return recallIn(timeMillis = timeMillis)
    }
}

/**
 * 撤回这条消息. [recall] 或 [recallIn] 只能被调用一次.
 *
 * @see Bot.recall
 * @throws IllegalStateException 当此消息已经被撤回或正计划撤回时
 */
suspend inline fun MessageReceipt<*>.recall() {
    return target.bot.recall(source)
}

/**
 * 在一段时间后撤回这条消息. [recall] 或 [recallIn] 只能被调用一次.
 *
 * @param timeMillis 延迟时间, 单位为毫秒
 * @throws IllegalStateException 当此消息已经被撤回或正计划撤回时
 */
inline fun MessageReceipt<*>.recallIn(
    timeMillis: Long,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job = source.recallIn(timeMillis, coroutineContext)


/**
 * 引用这条消息.
 * @see MessageChain.quote 引用一条消息
 */
@JvmSynthetic
inline fun MessageReceipt<*>.quote(): QuoteReply = this.source.quote()

/**
 * 引用这条消息并回复.
 * @see MessageChain.quote 引用一条消息
 */
@JvmSynthetic
suspend inline fun <C : Contact> MessageReceipt<C>.quoteReply(message: Message): MessageReceipt<C> {
    @Suppress("UNCHECKED_CAST")
    return target.sendMessage(this.quote() + message) as MessageReceipt<C>
}

/**
 * 引用这条消息并回复.
 * @see MessageChain.quote 引用一条消息
 */
@JvmSynthetic
suspend inline fun <C : Contact> MessageReceipt<C>.quoteReply(message: String): MessageReceipt<C> {
    return this.quoteReply(message.toMessage())
}


/**
 * 获取源消息 [MessageSource.id]
 *
 * @see MessageSource.id
 */
@get:JvmSynthetic
inline val MessageReceipt<*>.sourceId: Int
    get() = this.source.id

/**
 * 获取源消息 [MessageSource.time]
 *
 * @see MessageSource.time
 */
@get:JvmSynthetic
inline val MessageReceipt<*>.sourceTime: Int
    get() = this.source.time

