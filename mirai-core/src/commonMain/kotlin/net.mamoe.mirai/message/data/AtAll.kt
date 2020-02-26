/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:JvmMultifileClass
@file:JvmName("MessageUtils")

package net.mamoe.mirai.message.data

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * "@全体成员".
 *
 * 非会员每天只能发送 10 次 [AtAll]. 超出部分会被以普通文字看待.
 *
 * @see At at 单个群成员
 */
object AtAll : Message, Message.Key<AtAll>, MessageContent {
    override fun toString(): String = "@全体成员"

    // 自动为消息补充 " "

    override fun followedBy(tail: Message): CombinedMessage {
        if (tail is PlainText && tail.stringValue.startsWith(' ')) {
            return super<MessageContent>.followedBy(tail)
        }
        return super<MessageContent>.followedBy(PlainText(" ")) + tail
    }
}