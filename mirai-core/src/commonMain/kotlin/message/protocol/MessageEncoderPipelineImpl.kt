/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.message.protocol

import net.mamoe.mirai.internal.network.protocol.data.proto.ImMsgBody
import net.mamoe.mirai.internal.pipeline.AbstractProcessorPipeline
import net.mamoe.mirai.internal.pipeline.PipelineConfiguration
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.utils.*


internal open class MessageEncoderPipelineImpl :
    AbstractProcessorPipeline<MessageEncoderProcessor<*>, MessageEncoderContext, SingleMessage, ImMsgBody.Elem>(
        PipelineConfiguration(stopWhenConsumed = true),
        @OptIn(TestOnly::class)
        defaultTraceLogging
    ),
    MessageEncoderPipeline {

    private inner class MessageEncoderContextImpl(attributes: TypeSafeMap) : MessageEncoderContext,
        BaseContextImpl(attributes) {
        override var generalFlags: ImMsgBody.Elem by lateinitMutableProperty {
            ImMsgBody.Elem(generalFlags = ImMsgBody.GeneralFlags(pbReserve = PB_RESERVE_FOR_ELSE))
        }
    }

    override fun createContext(attributes: TypeSafeMap): MessageEncoderContext = MessageEncoderContextImpl(attributes)

    companion object {
        private val PB_RESERVE_FOR_ELSE = "78 00 F8 01 00 C8 02 00".hexToBytes()

        @TestOnly
        val defaultTraceLogging: MiraiLoggerWithSwitch by lazy {
            MiraiLogger.Factory.create(MessageEncoderPipelineImpl::class, "MessageEncoderPipeline")
                .withSwitch(systemProp("mirai.message.encoder.pipeline.log.full", false))
        }
    }
}