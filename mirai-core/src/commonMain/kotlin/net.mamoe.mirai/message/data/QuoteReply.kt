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
@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.mamoe.mirai.message.data

import kotlinx.coroutines.Job
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.MiraiExperimentalAPI
import net.mamoe.mirai.utils.SinceMirai
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmSynthetic


/**
 * 引用回复.
 *
 * 支持引用任何一条消息发送给任何人.
 *
 * #### [source] 的类型:
 * - 在发送引用回复时, [source] 类型为 [OnlineMessageSource] 或 [OfflineMessageSource]
 * - 在接收引用回复时, [source] 类型一定为 [OfflineMessageSource]
 *
 * #### 原消息内容
 * 引用回复的原消息内容完全由 [source] 中 [MessageSource.originalMessage] 控制, 客户端不会自行寻找原消息.
 *
 * #### 客户端内跳转
 * 客户端在跳转原消息时, 会通过 [MessageSource.id] 等 metadata
 *
 * @see MessageSource 获取有关消息源的更多信息
 */
@OptIn(MiraiExperimentalAPI::class)
@SinceMirai("0.33.0")
class QuoteReply(val source: MessageSource) : Message, MessageMetadata, ConstrainSingle<QuoteReply> {
    companion object Key : Message.Key<QuoteReply> {
        override val typeName: String
            get() = "QuoteReply"
    }

    override val key: Message.Key<QuoteReply> get() = Key

    override fun toString(): String = "[mirai:quote:${source.id},${source.internalId}]"
    override fun contentToString(): String = ""
    override fun equals(other: Any?): Boolean = other is QuoteReply && other.source == this.source
    override fun hashCode(): Int = source.hashCode()
}

/**
 * @see MessageSource.id
 */
@get:JvmSynthetic
inline val QuoteReply.id: Int
    get() = source.id

/**
 * @see MessageSource.internalId
 */
@SinceMirai("0.39.2")
@get:JvmSynthetic
inline val QuoteReply.internalId: Int
    get() = source.internalId

/**
 * @see MessageSource.fromId
 */
@get:JvmSynthetic
inline val QuoteReply.fromId: Long
    get() = source.fromId

/**
 * @see MessageSource.targetId
 */
@get:JvmSynthetic
inline val QuoteReply.targetId: Long
    get() = source.targetId

/**
 * @see MessageSource.originalMessage
 */
@get:JvmSynthetic
inline val QuoteReply.originalMessage: MessageChain
    get() = source.originalMessage

/**
 * @see MessageSource.time
 */
@get:JvmSynthetic
inline val QuoteReply.time: Int
    get() = source.time

/**
 * @see MessageSource.bot
 */
@get:JvmSynthetic
inline val QuoteReply.bot: Bot
    get() = source.bot


@JvmSynthetic
suspend inline fun QuoteReply.recall() = this.source.recall()

/**
 * 在一段时间后撤回这条消息.
 */
@JvmOverloads
inline fun QuoteReply.recallIn(
    millis: Long,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job = this.source.recallIn(millis, coroutineContext)