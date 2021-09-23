/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.internal.network.components

import net.mamoe.mirai.internal.QQAndroidBot
import net.mamoe.mirai.internal.message.contextualBugReportException
import net.mamoe.mirai.internal.network.Packet
import net.mamoe.mirai.internal.network.ParseErrorPacket
import net.mamoe.mirai.internal.network.component.ComponentKey
import net.mamoe.mirai.internal.network.component.ComponentStorage
import net.mamoe.mirai.internal.network.notice.BotAware
import net.mamoe.mirai.internal.network.notice.NewContactSupport
import net.mamoe.mirai.internal.network.notice.decoders.DecodedNotifyMsgBody
import net.mamoe.mirai.internal.network.notice.decoders.MsgType0x2DC
import net.mamoe.mirai.internal.network.protocol.data.jce.MsgInfo
import net.mamoe.mirai.internal.network.protocol.data.jce.MsgType0x210
import net.mamoe.mirai.internal.network.protocol.data.jce.RequestPushStatus
import net.mamoe.mirai.internal.network.protocol.data.proto.MsgComm
import net.mamoe.mirai.internal.network.protocol.data.proto.MsgOnlinePush
import net.mamoe.mirai.internal.network.protocol.data.proto.OnlinePushTrans.PbMsgInfo
import net.mamoe.mirai.internal.network.protocol.data.proto.Structmsg
import net.mamoe.mirai.internal.network.protocol.packet.chat.receive.MessageSvcPbGetMsg
import net.mamoe.mirai.internal.network.protocol.packet.chat.receive.OnlinePushPbPushTransMsg
import net.mamoe.mirai.internal.network.toPacket
import net.mamoe.mirai.internal.utils.io.ProtocolStruct
import net.mamoe.mirai.utils.*
import java.io.Closeable
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass

internal typealias ProcessResult = Collection<Packet>

/**
 * Centralized processor pipeline for [MessageSvcPbGetMsg] and [OnlinePushPbPushTransMsg]
 */
internal interface NoticeProcessorPipeline {
    val processors: Collection<NoticeProcessor>

    fun interface DisposableRegistry : Closeable {
        fun dispose()

        override fun close() {
            dispose()
        }
    }

    fun registerProcessor(processor: NoticeProcessor): DisposableRegistry

    /**
     * Process [data] into [Packet]s. Exceptions are wrapped into [ParseErrorPacket]
     */
    suspend fun process(
        bot: QQAndroidBot,
        data: ProtocolStruct,
        attributes: TypeSafeMap = TypeSafeMap.EMPTY
    ): ProcessResult

    companion object : ComponentKey<NoticeProcessorPipeline> {
        val ComponentStorage.noticeProcessorPipeline get() = get(NoticeProcessorPipeline)

        @JvmStatic
        suspend inline fun QQAndroidBot.processPacketThroughPipeline(
            data: ProtocolStruct,
            attributes: TypeSafeMap = TypeSafeMap.EMPTY,
        ): Packet {
            return components.noticeProcessorPipeline.process(this, data, attributes).toPacket()
        }
    }
}

@JvmInline
internal value class MutableProcessResult(
    val data: MutableCollection<Packet>
)

internal interface NoticePipelineContext : BotAware, NewContactSupport {
    override val bot: QQAndroidBot

    val attributes: TypeSafeMap


    val isConsumed: Boolean

    /**
     * Marks the input as consumed so that there will not be warnings like 'Unknown type xxx'. This will not stop the pipeline.
     *
     * If this is executed, make sure you provided all information important for debugging.
     *
     * You need to invoke [markAsConsumed] if your implementation includes some `else` branch which covers all situations,
     * and throws a [contextualBugReportException] or logs something.
     */
    @ConsumptionMarker
    fun NoticeProcessor.markAsConsumed(marker: Any = this)

    /**
     * Marks the input as not consumed, if it was marked by this [NoticeProcessor].
     */
    @ConsumptionMarker
    fun NoticeProcessor.markNotConsumed(marker: Any = this)

    @DslMarker
    annotation class ConsumptionMarker // to give an explicit color.


    val collected: MutableProcessResult

    // DSL to simplify some expressions
    operator fun MutableProcessResult.plusAssign(packet: Packet?) {
        if (packet != null) collect(packet)
    }


    /**
     * Collect a result.
     */
    fun collect(packet: Packet)

    /**
     * Collect results.
     */
    fun collect(packets: Iterable<Packet>)

    /**
     * Fire the [data] into the processor pipeline, and collect the results to current [collected].
     *
     * @param attributes extra attributes
     * @return result collected from processors. This would also have been collected to this context (where you call [processAlso]).
     */
    suspend fun processAlso(data: ProtocolStruct, attributes: TypeSafeMap = TypeSafeMap.EMPTY): ProcessResult

    companion object {
        val KEY_FROM_SYNC = TypeKey<Boolean>("fromSync")
        val KEY_MSG_INFO = TypeKey<MsgInfo>("msgInfo")

        val NoticePipelineContext.fromSync get() = attributes[KEY_FROM_SYNC]

        /**
         * 来自 [MsgInfo] 的数据, 即 [MsgType0x210], [MsgType0x2DC] 的处理过程之中可以使用
         */
        val NoticePipelineContext.msgInfo get() = attributes[KEY_MSG_INFO]
    }
}

internal abstract class AbstractNoticePipelineContext(
    override val bot: QQAndroidBot, override val attributes: TypeSafeMap,
) : NoticePipelineContext {
    private val consumers: Stack<Any> = Stack()

    override val isConsumed: Boolean get() = consumers.isNotEmpty()
    override fun NoticeProcessor.markAsConsumed(marker: Any) {
        traceLogging.info { "markAsConsumed: marker=$marker" }
        consumers.push(marker)
    }

    override fun NoticeProcessor.markNotConsumed(marker: Any) {
        if (consumers.peek() === marker) {
            consumers.pop()
            traceLogging.info { "markNotConsumed: Y, marker=$marker" }
        } else {
            traceLogging.info { "markNotConsumed: N, marker=$marker" }
        }
    }

    override val collected = MutableProcessResult(ConcurrentLinkedQueue())

    override fun collect(packet: Packet) {
        collected.data.add(packet)
        traceLogging.info { "collect: $packet" }
    }

    override fun collect(packets: Iterable<Packet>) {
        this.collected.data.addAll(packets)
        traceLogging.info {
            val list = packets.toList()
            "collect: [${list.size}] ${list.joinToString()}"
        }
    }

    abstract override suspend fun processAlso(data: ProtocolStruct, attributes: TypeSafeMap): ProcessResult
}


internal inline val NoticePipelineContext.context get() = this

private val traceLogging: MiraiLogger by lazy {
    MiraiLogger.Factory.create(NoticeProcessorPipelineImpl::class, "NoticeProcessorPipeline")
        .withSwitch(systemProp("mirai.network.notice.pipeline.log.full", false))
}

internal open class NoticeProcessorPipelineImpl protected constructor() : NoticeProcessorPipeline {
    /**
     * Must be ordered
     */
    override val processors = ConcurrentLinkedQueue<NoticeProcessor>()

    override fun registerProcessor(processor: NoticeProcessor): NoticeProcessorPipeline.DisposableRegistry {
        processors.add(processor)
        return NoticeProcessorPipeline.DisposableRegistry {
            processors.remove(processor)
        }
    }


    open inner class ContextImpl(
        bot: QQAndroidBot, attributes: TypeSafeMap,
    ) : AbstractNoticePipelineContext(bot, attributes) {
        override suspend fun processAlso(data: ProtocolStruct, attributes: TypeSafeMap): ProcessResult {
            traceLogging.info { "processAlso: data=$data" }
            return process(bot, data, this.attributes + attributes).also {
                this.collected.data += it
                traceLogging.info { "processAlso: result=$it" }
            }
        }
    }


    override suspend fun process(bot: QQAndroidBot, data: ProtocolStruct, attributes: TypeSafeMap): ProcessResult {
        traceLogging.info { "process: data=$data" }
        val context = createContext(bot, attributes)

        val diff = if (traceLogging.isEnabled) CollectionDiff<Packet>() else null
        diff?.save(context.collected.data)

        for (processor in processors) {

            val result = kotlin.runCatching {
                processor.process(context, data)
            }.onFailure { e ->
                context.collect(
                    ParseErrorPacket(
                        data,
                        IllegalStateException(
                            "Exception in $processor while processing packet ${packetToString(data)}.",
                            e,
                        ),
                    ),
                )
            }

            diff?.run {
                val diffPackets = subtractAndSave(context.collected.data)

                traceLogging.info {
                    "Finished ${
                        processor.toString().replace("net.mamoe.mirai.internal.network.notice.", "")
                    }, success=${result.isSuccess}, consumed=${context.isConsumed}, diff=$diffPackets"
                }
            }
        }
        return context.collected.data
    }

    protected open fun createContext(
        bot: QQAndroidBot,
        attributes: TypeSafeMap
    ): NoticePipelineContext = ContextImpl(bot, attributes)

    protected open fun packetToString(data: Any?): String =
        data.toDebugString("mirai.network.notice.pipeline.log.full")


    companion object {
        fun create(vararg processors: NoticeProcessor): NoticeProcessorPipelineImpl =
            NoticeProcessorPipelineImpl().apply {
                for (processor in processors) {
                    registerProcessor(processor)
                }
            }
    }
}

///////////////////////////////////////////////////////////////////////////
// NoticeProcessor
///////////////////////////////////////////////////////////////////////////

/**
 * A processor handling some specific type of message.
 */
internal interface NoticeProcessor {
    suspend fun process(context: NoticePipelineContext, data: Any?)
}

internal abstract class AnyNoticeProcessor : SimpleNoticeProcessor<ProtocolStruct>(type())

internal abstract class SimpleNoticeProcessor<in T : ProtocolStruct>(
    private val type: KClass<T>,
) : NoticeProcessor {

    final override suspend fun process(context: NoticePipelineContext, data: Any?) {
        if (type.isInstance(data)) {
            context.processImpl(data.uncheckedCast())
        }
    }

    protected abstract suspend fun NoticePipelineContext.processImpl(data: T)

    companion object {
        @JvmStatic
        protected inline fun <reified T : Any> type(): KClass<T> = T::class
    }
}

internal abstract class MsgCommonMsgProcessor : SimpleNoticeProcessor<MsgComm.Msg>(type()) {
    abstract override suspend fun NoticePipelineContext.processImpl(data: MsgComm.Msg)
}

internal abstract class MixedNoticeProcessor : AnyNoticeProcessor() {
    final override suspend fun NoticePipelineContext.processImpl(data: ProtocolStruct) {
        when (data) {
            is PbMsgInfo -> processImpl(data)
            is MsgOnlinePush.PbPushMsg -> processImpl(data)
            is MsgComm.Msg -> processImpl(data)
            is MsgType0x210 -> processImpl(data)
            is MsgType0x2DC -> processImpl(data)
            is Structmsg.StructMsg -> processImpl(data)
            is RequestPushStatus -> processImpl(data)
            is DecodedNotifyMsgBody -> processImpl(data)
        }
    }

    protected open suspend fun NoticePipelineContext.processImpl(data: MsgType0x210) {} // 528
    protected open suspend fun NoticePipelineContext.processImpl(data: MsgType0x2DC) {} // 732
    protected open suspend fun NoticePipelineContext.processImpl(data: PbMsgInfo) {}
    protected open suspend fun NoticePipelineContext.processImpl(data: MsgOnlinePush.PbPushMsg) {}
    protected open suspend fun NoticePipelineContext.processImpl(data: MsgComm.Msg) {}
    protected open suspend fun NoticePipelineContext.processImpl(data: Structmsg.StructMsg) {}
    protected open suspend fun NoticePipelineContext.processImpl(data: RequestPushStatus) {}

    protected open suspend fun NoticePipelineContext.processImpl(data: DecodedNotifyMsgBody) {}
}