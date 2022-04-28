/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.message.protocol.impl

import net.mamoe.mirai.contact.AnonymousMember
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.internal.message.protocol.*
import net.mamoe.mirai.internal.message.protocol.MessageDecoderContext.Companion.BOT
import net.mamoe.mirai.internal.message.protocol.MessageDecoderContext.Companion.GROUP_ID
import net.mamoe.mirai.internal.message.protocol.MessageDecoderContext.Companion.MESSAGE_SOURCE_KIND
import net.mamoe.mirai.internal.message.protocol.MessageEncoderContext.Companion.contact
import net.mamoe.mirai.internal.message.source.MessageSourceInternal
import net.mamoe.mirai.internal.message.source.OfflineMessageSourceImplData
import net.mamoe.mirai.internal.network.protocol.data.proto.ImMsgBody
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.OnlineMessageSource
import net.mamoe.mirai.message.data.QuoteReply

internal class QuoteReplyProtocol : MessageProtocol(PRIORITY_METADATA) {
    override fun ProcessorCollector.collectProcessorsImpl() {
        add(Encoder())
        add(Decoder())
    }

    private class Decoder : MessageDecoder {
        override suspend fun MessageDecoderContext.process(data: ImMsgBody.Elem) {
            if (data.srcMsg == null) return
            OfflineMessageSourceImplData(
                data.srcMsg,
                attributes[BOT],
                attributes[MESSAGE_SOURCE_KIND],
                attributes[GROUP_ID]
            )
        }

    }

    private class Encoder : MessageEncoder<QuoteReply> {
        override suspend fun MessageEncoderContext.process(data: QuoteReply) {
            val source = data.source as? MessageSourceInternal ?: return
            collect(ImMsgBody.Elem(srcMsg = source.toJceData()))
            if (contact is Group) {
                if (source is OnlineMessageSource.Incoming.FromGroup) {
                    val sender0 = source.sender
                    if (sender0 !is AnonymousMember) {
                        processAlso(At(sender0))
                    }
                    // transformOneMessage(PlainText(" "))
                    // removed by https://github.com/mamoe/mirai/issues/524
                    // 发送 QuoteReply 消息时无可避免的产生多余空格 #524
                }
            }
        }

    }
}