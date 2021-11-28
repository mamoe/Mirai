/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("unused")

package net.mamoe.mirai.contact

import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.utils.DeprecatedSinceMirai
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.millisToHumanReadableString
import kotlin.time.ExperimentalTime

/**
 * 发送消息时消息过长抛出的异常.
 *
 * @see Contact.sendMessage
 */
public class MessageTooLargeException constructor(
    public override val target: Contact,
    /**
     * 原发送消息
     */
    originalMessage: Message,
    /**
     * 经过事件拦截处理后的消息
     */
    public val messageAfterEvent: Message,
    exceptionMessage: String
) : SendMessageFailedException(target, Reason.MESSAGE_TOO_LARGE, originalMessage) {
    override val message: String = exceptionMessage
}

/**
 * 发送消息时 bot 正处于被禁言状态时抛出的异常.
 *
 * @see Group.sendMessage
 */
@OptIn(ExperimentalTime::class)
public class BotIsBeingMutedException @MiraiInternalApi constructor(
    // this constructor is since 2.9.0-RC
    public override val target: Group,
    originalMessage: Message,
) : SendMessageFailedException(target, Reason.BOT_MUTED, originalMessage) {
    @DeprecatedSinceMirai("2.9")
    @Deprecated("Deprecated without replacement. Please consider copy this exception to your code.")
    // this constructor is since 2.0
    public constructor(
        target: Group,
    ) : this(target, messageChainOf())

    override val message: String = "bot is being muted, remaining ${
        target.botMuteRemaining.times(1000).millisToHumanReadableString()
    } seconds"
}

public inline val BotIsBeingMutedException.botMuteRemaining: Int get() = target.botMuteRemaining

/**
 * 发送消息失败时抛出的异常
 *
 * @since 2.9.0
 */
public open class SendMessageFailedException @MiraiInternalApi constructor(
    public open val target: Contact,
    public val reason: Reason,
    public val originalMessage: Message,
) : RuntimeException(
    "Failed sending message to $target, reason=$reason"
) {
    public enum class Reason {
        /**
         * 消息过长
         */
        MESSAGE_TOO_LARGE,

        /**
         * 机器人被禁言
         */
        BOT_MUTED,

        /**
         * 达到群每分钟发言次数限制
         */
        GROUP_CHAT_LIMITED,
    }
}