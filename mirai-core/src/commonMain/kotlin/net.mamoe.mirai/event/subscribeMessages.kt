/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("EXPERIMENTAL_API_USAGE", "MemberVisibilityCanBePrivate", "unused")

package net.mamoe.mirai.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.message.FriendMessage
import net.mamoe.mirai.message.GroupMessage
import net.mamoe.mirai.message.MessagePacket
import net.mamoe.mirai.message.data.Message
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 订阅来自所有 [Bot] 的所有联系人的消息事件. 联系人可以是任意群或任意好友或临时会话.
 *
 * @see CoroutineScope.incoming
 */
@UseExperimental(ExperimentalContracts::class)
@MessageDsl
inline fun <R> CoroutineScope.subscribeMessages(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline listeners: MessageSubscribersBuilder<MessagePacket<*, *>>.() -> R
): R {
    // contract 可帮助 IDE 进行类型推断. 无实际代码作用.
    contract {
        callsInPlace(listeners, InvocationKind.EXACTLY_ONCE)
    }

    return MessageSubscribersBuilder { messageListener: MessageListener<MessagePacket<*, *>> ->
        // subscribeAlways 即注册一个监听器. 这个监听器收到消息后就传递给 [listener]
        // listener 即为 DSL 里 `contains(...) { }`, `startsWith(...) { }` 的代码块.
        subscribeAlways(coroutineContext) {
            messageListener.invoke(this, this.message.toString())
            // this.message.toString() 即为 messageListener 中 it 接收到的值
        }
    }.run(listeners)
}

/**
 * 订阅来自所有 [Bot] 的所有群消息事件
 *
 * @see CoroutineScope.incoming
 */
@UseExperimental(ExperimentalContracts::class)
@MessageDsl
inline fun <R> CoroutineScope.subscribeGroupMessages(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline listeners: MessageSubscribersBuilder<GroupMessage>.() -> R
): R {
    contract {
        callsInPlace(listeners, InvocationKind.EXACTLY_ONCE)
    }
    return MessageSubscribersBuilder<GroupMessage> { listener ->
        subscribeAlways(coroutineContext) {
            listener(this, this.message.toString())
        }
    }.run(listeners)
}

/**
 * 订阅来自所有 [Bot] 的所有好友消息事件
 *
 * @see CoroutineScope.incoming
 */
@UseExperimental(ExperimentalContracts::class)
@MessageDsl
inline fun <R> CoroutineScope.subscribeFriendMessages(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline listeners: MessageSubscribersBuilder<FriendMessage>.() -> R
): R {
    contract {
        callsInPlace(listeners, InvocationKind.EXACTLY_ONCE)
    }
    return MessageSubscribersBuilder<FriendMessage> { listener ->
        subscribeAlways(coroutineContext) {
            listener(this, this.message.toString())
        }
    }.run(listeners)
}

/**
 * 订阅来自这个 [Bot] 的所有联系人的消息事件. 联系人可以是任意群或任意好友或临时会话.
 *
 * @see CoroutineScope.incoming
 */
@UseExperimental(ExperimentalContracts::class)
@MessageDsl
inline fun <R> Bot.subscribeMessages(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline listeners: MessageSubscribersBuilder<MessagePacket<*, *>>.() -> R
): R {
    contract {
        callsInPlace(listeners, InvocationKind.EXACTLY_ONCE)
    }
    return MessageSubscribersBuilder<MessagePacket<*, *>> { listener ->
        this.subscribeAlways(coroutineContext) {
            listener(this, this.message.toString())
        }
    }.run(listeners)
}

/**
 * 订阅来自这个 [Bot] 的所有群消息事件
 *
 * @param coroutineContext 给事件监听协程的额外的 [CoroutineContext]
 *
 * @see CoroutineScope.incoming
 */
@UseExperimental(ExperimentalContracts::class)
@MessageDsl
inline fun <R> Bot.subscribeGroupMessages(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline listeners: MessageSubscribersBuilder<GroupMessage>.() -> R
): R {
    contract {
        callsInPlace(listeners, InvocationKind.EXACTLY_ONCE)
    }
    return MessageSubscribersBuilder<GroupMessage> { listener ->
        this.subscribeAlways(coroutineContext) {
            listener(this, this.message.toString())
        }
    }.run(listeners)
}

/**
 * 订阅来自这个 [Bot] 的所有好友消息事件.
 *
 * @see CoroutineScope.incoming
 */
@UseExperimental(ExperimentalContracts::class)
@MessageDsl
inline fun <R> Bot.subscribeFriendMessages(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline listeners: MessageSubscribersBuilder<FriendMessage>.() -> R
): R {
    contract {
        callsInPlace(listeners, InvocationKind.EXACTLY_ONCE)
    }
    return MessageSubscribersBuilder<FriendMessage> { listener ->
        this.subscribeAlways(coroutineContext) {
            listener(this, this.message.toString())
        }
    }.run(listeners)
}

/**
 * 返回一个指定事件的接收通道
 *
 * @param capacity 同 [Channel] 的参数, 参见 [Channel.Factory] 中的常量.
 *
 * @see subscribeFriendMessages
 * @see subscribeMessages
 * @see subscribeGroupMessages
 */
inline fun <reified E : Event> CoroutineScope.incoming(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    capacity: Int = Channel.RENDEZVOUS
): ReceiveChannel<E> {
    return Channel<E>(capacity).apply {
        subscribeAlways<E>(coroutineContext) {
            send(this)
        }
    }
}


/**
 * 消息事件的处理器.
 *
 * 注:
 * 接受者 T 为 [MessagePacket]
 * 参数 String 为 转为字符串了的消息 ([Message.toString])
 */
typealias MessageListener<T> = @MessageDsl suspend T.(String) -> Unit

/**
 * 消息订阅构造器
 *
 * @see subscribeFriendMessages
 * @sample demo.subscribe.messageDSL
 */
// TODO: 2019/12/23 应定义为 inline, 但这会导致一个 JVM run-time VerifyError. 等待 kotlin 修复 bug (Kotlin 1.3.61)
@Suppress("unused")
@MessageDsl
class MessageSubscribersBuilder<T : MessagePacket<*, *>>(
    /**
     * invoke 这个 lambda 时, 它将会把 [消息事件的处理器][MessageListener] 注册给事件, 并返回注册完成返回的监听器.
     */
    val subscriber: (MessageListener<T>) -> Listener<T>
) {
    /**
     * 监听的条件
     */
    open inner class ListeningFilter(
        val filter: T.(String) -> Boolean
    ) {
        /**
         * 进行逻辑 `or`.
         */
        infix fun or(another: ListeningFilter): ListeningFilter =
            ListeningFilter { filter.invoke(this, it) || another.filter.invoke(this, it) }

        /**
         * 进行逻辑 `and`.
         */
        infix fun and(another: ListeningFilter): ListeningFilter =
            ListeningFilter { filter.invoke(this, it) && another.filter.invoke(this, it) }

        /**
         * 进行逻辑 `xor`.
         */
        infix fun xor(another: ListeningFilter): ListeningFilter =
            ListeningFilter { filter.invoke(this, it) xor another.filter.invoke(this, it) }

        /**
         * 进行逻辑 `nand`, 即 `not and`.
         */
        infix fun nand(another: ListeningFilter): ListeningFilter =
            ListeningFilter { !filter.invoke(this, it) || !another.filter.invoke(this, it) }

        /**
         * 进行逻辑 `not`
         */
        fun not(): ListeningFilter =
            ListeningFilter { !filter.invoke(this, it) }

        /**
         * 启动事件监听.
         */
        // do not inline due to kotlin (1.3.61) bug: java.lang.IllegalAccessError
        operator fun invoke(onEvent: MessageListener<T>): Listener<T> {
            return content(filter, onEvent)
        }

        infix fun reply(toReply: String): Listener<T> {
            return content(filter) { reply(toReply) }
        }

        infix fun reply(message: Message): Listener<T> {
            return content(filter) { reply(message) }
        }

        infix fun reply(replier: (@MessageDsl suspend T.(String) -> Any?)): Listener<T> {
            return content(filter) {
                @Suppress("DSL_SCOPE_VIOLATION_WARNING")
                executeAndReply(replier)
            }
        }

        infix fun quoteReply(toReply: String): Listener<T> {
            return content(filter) { quoteReply(toReply) }
        }

        infix fun quoteReply(message: Message): Listener<T> {
            return content(filter) { quoteReply(message) }
        }

        infix fun quoteReply(replier: (@MessageDsl suspend T.(String) -> Any?)): Listener<T> {
            return content(filter) {
                @Suppress("DSL_SCOPE_VIOLATION_WARNING")
                executeAndQuoteReply(replier)
            }
        }
    }

    /**
     * 无任何触发条件.
     */
    @MessageDsl
    fun always(onEvent: MessageListener<T>): Listener<T> = subscriber(onEvent)

    /**
     * 如果消息内容 `==` [equals]
     */
    @MessageDsl
    fun case(
        equals: String,
        ignoreCase: Boolean = false,
        trim: Boolean = true
    ): ListeningFilter {
        return if (trim) {
            val toCheck = equals.trim()
            content { it.trim().equals(toCheck, ignoreCase = ignoreCase) }
        } else {
            content { it.equals(equals, ignoreCase = ignoreCase) }
        }
    }

    /**
     * 如果消息内容 `==` [equals]
     * @param trim `true` 则删除首尾空格后比较
     * @param ignoreCase `true` 则不区分大小写
     */
    @MessageDsl
    inline fun case(
        equals: String,
        ignoreCase: Boolean = false,
        trim: Boolean = true,
        crossinline onEvent: @MessageDsl suspend T.(String) -> Unit
    ): Listener<T> {
        val toCheck = if (trim) equals.trim() else equals
        return content({ (if (trim) it.trim() else it).equals(toCheck, ignoreCase = ignoreCase) }, {
            onEvent(this, this.message.toString())
        })
    }

    /**
     * 如果消息内容包含 [sub]
     */
    @MessageDsl
    fun contains(sub: String): ListeningFilter =
        content { sub in it }

    /**
     * 如果消息内容包含 [sub]
     */
    @MessageDsl
    inline fun contains(
        sub: String,
        ignoreCase: Boolean = false,
        trim: Boolean = true,
        crossinline onEvent: MessageListener<T>
    ): Listener<T> {
        return if (trim) {
            val toCheck = sub.trim()
            content({ it.contains(toCheck, ignoreCase = ignoreCase) }, {
                onEvent(this, this.message.toString().trim())
            })
        } else {
            content({ it.contains(sub, ignoreCase = ignoreCase) }, {
                onEvent(this, this.message.toString())
            })
        }
    }

    /**
     * 如果消息的前缀是 [prefix]
     */
    @MessageDsl
    fun startsWith(
        prefix: String,
        trim: Boolean = true
    ): ListeningFilter {
        val toCheck = if (trim) prefix.trim() else prefix
        return content { (if (trim) it.trim() else it).startsWith(toCheck) }
    }

    /**
     * 如果消息的前缀是 [prefix]
     */
    @MessageDsl
    inline fun startsWith(
        prefix: String,
        removePrefix: Boolean = true,
        trim: Boolean = true,
        crossinline onEvent: @MessageDsl suspend T.(String) -> Unit
    ): Listener<T> {
        return if (trim) {
            val toCheck = prefix.trim()
            content({ it.trimStart().startsWith(toCheck) }, {
                if (removePrefix) this.onEvent(this.message.toString().substringAfter(toCheck).trim())
                else onEvent(this, this.message.toString().trim())
            })
        } else {
            content({ it.startsWith(prefix) }, {
                if (removePrefix) this.onEvent(this.message.toString().removePrefix(prefix))
                else onEvent(this, this.message.toString())
            })
        }
    }

    /**
     * 如果消息的结尾是 [suffix]
     */
    @MessageDsl
    fun endsWith(suffix: String): ListeningFilter =
        content { it.endsWith(suffix) }

    /**
     * 如果消息的结尾是 [suffix]
     */
    @MessageDsl
    inline fun endsWith(
        suffix: String,
        removeSuffix: Boolean = true,
        trim: Boolean = true,
        crossinline onEvent: @MessageDsl suspend T.(String) -> Unit
    ): Listener<T> {
        return if (trim) {
            val toCheck = suffix.trim()
            content({ it.trimEnd().endsWith(toCheck) }, {
                if (removeSuffix) this.onEvent(this.message.toString().removeSuffix(toCheck).trim())
                else onEvent(this, this.message.toString().trim())
            })
        } else {
            content({ it.endsWith(suffix) }, {
                if (removeSuffix) this.onEvent(this.message.toString().removeSuffix(suffix))
                else onEvent(this, this.message.toString())
            })
        }
    }

    /**
     * 如果是这个人发的消息. 消息目前只会是群消息
     */
    @MessageDsl
    fun sentBy(name: String): ListeningFilter =
        content { this is GroupMessage && this.senderName == name }

    /**
     * 如果是这个人发的消息. 消息目前只会是群消息
     */
    @MessageDsl
    inline fun sentBy(name: String, crossinline onEvent: MessageListener<T>): Listener<T> =
        content({ this is GroupMessage && this.senderName == name }, onEvent)

    /**
     * 如果是这个人发的消息. 消息可以是好友消息也可以是群消息
     */
    @MessageDsl
    fun sentBy(qq: Long): ListeningFilter =
        content { sender.id == qq }

    /**
     * 如果是这个人发的消息. 消息可以是好友消息也可以是群消息
     */
    @MessageDsl
    inline fun sentBy(qq: Long, crossinline onEvent: MessageListener<T>): Listener<T> =
        content({ this.sender.id == qq }, onEvent)

    /**
     * 如果是好友发来的消息
     */
    @MessageDsl
    inline fun sentByFriend(crossinline onEvent: MessageListener<FriendMessage>): Listener<T> =
        content({ this is FriendMessage }) {
            onEvent(this as FriendMessage, it)
        }

    /**
     * 如果是好友发来的消息
     */
    @MessageDsl
    fun sentByFriend(): ListeningFilter = ListeningFilter { this is FriendMessage }

    /**
     * 如果是管理员或群主发的消息
     */
    @MessageDsl
    fun sentByOperator(): ListeningFilter =
        content { this is GroupMessage && sender.permission.isOperator() }

    /**
     * 如果是管理员或群主发的消息
     */
    @MessageDsl
    inline fun sentByOperator(crossinline onEvent: MessageListener<T>): Listener<T> =
        content({ this is GroupMessage && this.sender.isOperator() }, onEvent)

    /**
     * 如果是管理员发的消息
     */
    @MessageDsl
    fun sentByAdministrator(): ListeningFilter =
        content { this is GroupMessage && sender.permission.isAdministrator() }

    /**
     * 如果是管理员发的消息
     */
    @MessageDsl
    inline fun sentByAdministrator(crossinline onEvent: MessageListener<T>): Listener<T> =
        content({ this is GroupMessage && this.sender.isAdministrator() }, onEvent)

    /**
     * 如果是群主发的消息
     */
    @MessageDsl
    fun sentByOwner(): ListeningFilter =
        content { this is GroupMessage && sender.isOwner() }

    /**
     * 如果是群主发的消息
     */
    @MessageDsl
    inline fun sentByOwner(crossinline onEvent: MessageListener<T>): Listener<T> =
        content({ this is GroupMessage && this.sender.isOwner() }, onEvent)

    /**
     * 如果是来自这个群的消息
     */
    @MessageDsl
    fun sentFrom(groupId: Long): ListeningFilter =
        content { this is GroupMessage && group.id == groupId }

    /**
     * 如果是来自这个群的消息, 就执行 [onEvent]
     */
    @MessageDsl
    inline fun sentFrom(groupId: Long, crossinline onEvent: MessageListener<GroupMessage>): Listener<T> =
        content({ this is GroupMessage && this.group.id == groupId }) {
            onEvent(this as GroupMessage, it)
        }

    /**
     * 如果消息内容包含 [M] 类型的 [Message]
     */
    @MessageDsl
    inline fun <reified M : Message> has(): ListeningFilter =
        content { message.any { it is M } }

    /**
     * 如果消息内容包含 [M] 类型的 [Message], 就执行 [onEvent]
     */
    @MessageDsl
    inline fun <reified M : Message> has(crossinline onEvent: MessageListener<T>): Listener<T> =
        content({ message.any { it is M } }, onEvent)

    /**
     * 如果 [filter] 返回 `true`
     */
    @MessageDsl
    fun content(filter: T.(String) -> Boolean): ListeningFilter =
        ListeningFilter(filter)

    /**
     * 如果 [filter] 返回 `true` 就执行 `onEvent`
     */
    @MessageDsl
    inline fun content(crossinline filter: T.(String) -> Boolean, crossinline onEvent: MessageListener<T>): Listener<T> =
        subscriber {
            if (filter(this, it)) onEvent(this, it)
        }

    /**
     * 如果消息内容可由正则表达式匹配([Regex.matchEntire])
     */
    @MessageDsl
    fun matching(regex: Regex): ListeningFilter =
        content { regex.matchEntire(it) != null }

    /**
     * 如果消息内容可由正则表达式匹配([Regex.matchEntire]), 就执行 `onEvent`
     */
    @MessageDsl
    inline fun matching(regex: Regex, crossinline onEvent: @MessageDsl suspend T.(MatchResult) -> Unit): Listener<T> =
        always {
            val find = regex.matchEntire(it) ?: return@always
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                onEvent.invoke(this, find)
            }
        }

    /**
     * 如果消息内容可由正则表达式查找([Regex.find])
     */
    @MessageDsl
    fun finding(regex: Regex): ListeningFilter =
        content { regex.find(it) != null }

    /**
     * 如果消息内容可由正则表达式查找([Regex.find]), 就执行 `onEvent`
     */
    @MessageDsl
    inline fun finding(regex: Regex, crossinline onEvent: @MessageDsl suspend T.(MatchResult) -> Unit): Listener<T> =
        always {
            val find = regex.find(it) ?: return@always
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                onEvent.invoke(this, find)
            }
        }


    /**
     * 若消息内容包含 [this] 则回复 [reply]
     */
    @MessageDsl
    infix fun String.containsReply(reply: String): Listener<T> =
        content({ this@containsReply in it }, { reply(reply) })

    /**
     * 若消息内容包含 [this] 则执行 [replier] 并将其返回值回复给发信对象.
     *
     * [replier] 的 `it` 将会是消息内容 string.
     *
     * @param replier 若返回 [Message] 则直接发送; 若返回 [Unit] 则不回复; 其他情况则 [Any.toString] 后回复
     */
    @MessageDsl
    inline infix fun String.containsReply(crossinline replier: @MessageDsl suspend T.(String) -> Any?): Listener<T> =
        content({ this@containsReply in it }, {
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply(replier)
        })

    /**
     * 若消息内容可由正则表达式匹配([Regex.matchEntire]), 则执行 [replier] 并将其返回值回复给发信对象.
     *
     * [replier] 的 `it` 将会是消息内容 string.
     *
     * @param replier 若返回 [Message] 则直接发送; 若返回 [Unit] 则不回复; 其他情况则 [Any.toString] 后回复
     */
    @MessageDsl
    inline infix fun Regex.matchingReply(crossinline replier: @MessageDsl suspend T.(MatchResult) -> Any?): Listener<T> =
        always {
            val find = this@matchingReply.matchEntire(it) ?: return@always
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                replier.invoke(this, find)
            }
        }

    /**
     * 若消息内容可由正则表达式查找([Regex.find]), 则执行 [replier] 并将其返回值回复给发信对象.
     *
     * [replier] 的 `it` 将会是消息内容 string.
     *
     * @param replier 若返回 [Message] 则直接发送; 若返回 [Unit] 则不回复; 其他情况则 [Any.toString] 后回复
     */
    @MessageDsl
    inline infix fun Regex.findingReply(crossinline replier: @MessageDsl suspend T.(MatchResult) -> Any?): Listener<T> =
        always {
            val find = this@findingReply.find(it) ?: return@always
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                replier.invoke(this, find)
            }
        }

    /**
     * 不考虑空格, 若消息内容以 [this] 开始则执行 [replier] 并将其返回值回复给发信对象.
     *
     * [replier] 的 `it` 将会是去掉用来判断的前缀并删除前后空格后的字符串.
     * 如当消息为 "kick    123456     " 时
     * ```kotlin
     * "kick" startsWithReply {
     *     println(it) // it 为 "123456"
     * }
     * ```
     *
     * @param replier 若返回 [Message] 则直接发送; 若返回 [Unit] 则不回复; 其他类型则 [Any.toString] 后回复
     */
    @MessageDsl
    inline infix fun String.startsWithReply(crossinline replier: @MessageDsl suspend T.(String) -> Any?): Listener<T> {
        val toCheck = this.trimStart()
        return content({ it.trim().startsWith(toCheck) }, {
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                replier(this, it.trim().removePrefix(toCheck))
            }
        })
    }

    /**
     * 不考虑空格, 若消息内容以 [this] 结尾则执行 [replier] 并将其返回值回复给发信对象.
     *
     * [replier] 的 `it` 将会是去掉用来判断的后缀并删除前后空格后的字符串.
     * 如当消息为 "  123456 test" 时
     * ```kotlin
     * "test" endsWithReply {
     *     println(it) // it 为 "123456"
     * }
     * ```
     *
     * @param replier 若返回 [Message] 则直接发送; 若返回 [Unit] 则不回复; 其他情况则 [Any.toString] 后回复
     */
    @MessageDsl
    inline infix fun String.endsWithReply(crossinline replier: @MessageDsl suspend T.(String) -> Any?): Listener<T> {
        val toCheck = this.trimEnd()
        return content({ it.trim().endsWith(toCheck) }, {
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                replier(this, it.trim().removeSuffix(toCheck))
            }
        })
    }

    @MessageDsl
    infix fun String.reply(reply: String): Listener<T> {
        val toCheck = this.trim()
        return content({ it.trim() == toCheck }, { reply(reply) })
    }

    @MessageDsl
    infix fun String.reply(reply: Message): Listener<T> {
        val toCheck = this.trim()
        return content({ it.trim() == toCheck }, { reply(reply) })
    }

    @MessageDsl
    inline infix fun String.reply(crossinline replier: @MessageDsl suspend T.(String) -> Any?): Listener<T> {
        val toCheck = this.trim()
        return content({ it.trim() == toCheck }, {
            @Suppress("DSL_SCOPE_VIOLATION_WARNING")
            this.executeAndReply {
                replier(this, it.trim())
            }
        })
    }

    @PublishedApi
    @Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE") // false positive
    internal suspend inline fun T.executeAndReply(replier: suspend T.(String) -> Any?) {
        when (val message = replier(this, this.message.toString())) {
            is Message -> this.reply(message)
            is Unit -> {

            }
            else -> this.reply(message.toString())
        }
    }

    @PublishedApi
    @Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE") // false positive
    internal suspend inline fun T.executeAndQuoteReply(replier: suspend T.(String) -> Any?) {
        when (val message = replier(this, this.message.toString())) {
            is Message -> this.quoteReply(message)
            is Unit -> {

            }
            else -> this.quoteReply(message.toString())
        }
    }
/* 易产生迷惑感
 fun replyCase(equals: String, trim: Boolean = true, replier: MessageReplier<T>) = case(equals, trim) { reply(replier(this)) }
 fun replyContains(value: String, replier: MessageReplier<T>) = content({ value in it }) { replier(this) }
 fun replyStartsWith(value: String, replier: MessageReplier<T>) = content({ it.startsWith(value) }) { replier(this) }
 fun replyEndsWith(value: String, replier: MessageReplier<T>) = content({ it.endsWith(value) }) { replier(this) }
*/
}

/**
 * DSL 标记. 将能让 IDE 阻止一些错误的方法调用.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@DslMarker
annotation class MessageDsl