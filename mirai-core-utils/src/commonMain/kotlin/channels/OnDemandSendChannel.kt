/*
 * Copyright 2019-2023 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.utils.channels

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import net.mamoe.mirai.utils.UtilsLogger
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * 按需供给的 [SendChannel].
 */
public interface OnDemandSendChannel<T, V> {
    /**
     * 挂起协程, 直到 [OnDemandReceiveChannel] [期望接收][OnDemandReceiveChannel.receiveOrNull]一个 [V], 届时将 [value] 传递给 [OnDemandReceiveChannel.receiveOrNull], 成为其返回值.
     *
     * 若在调用 [emit] 时已经有 [OnDemandReceiveChannel.receiveOrNull] 正在等待, 则该协程会立即[恢复][Continuation.resumeWith], [emit] 不会挂起.
     *
     * 若 [OnDemandReceiveChannel] 已经[完结][OnDemandReceiveChannel.finish], [OnDemandSendChannel.emit] 会抛出 [IllegalProducerStateException].
     *
     * @see OnDemandReceiveChannel.receiveOrNull
     */
    public suspend fun emit(value: V): T

    /**
     * 标记此 [OnDemandSendChannel] 在生产 [V] 的过程中出现异常.
     *
     * 这也会终止此 [OnDemandSendChannel], 若 [OnDemandReceiveChannel] 正在期待一个值, 则当它调用 [OnDemandReceiveChannel.receiveOrNull] 时, 它将得到一个 [ProducerFailureException].
     *
     * 在 [finishExceptionally] 之后若尝试调用 [OnDemandSendChannel.emit], [OnDemandReceiveChannel.receiveOrNull] 或 [OnDemandReceiveChannel.expectMore] 都会导致 [IllegalStateException].
     */
    public fun finishExceptionally(exception: Throwable)

    /**
     * 标记此 [OnDemandSendChannel] 已经没有更多 [V] 可生产.
     *
     * 这会终止此 [OnDemandSendChannel], 若 [OnDemandReceiveChannel] 正在期待一个值, 则当它调用 [OnDemandReceiveChannel.receiveOrNull] 时, 它将得到一个 [ProducerFailureException].
     *
     * 在 [finish] 之后若尝试调用 [OnDemandSendChannel.emit], [OnDemandReceiveChannel.receiveOrNull] 或 [OnDemandReceiveChannel.expectMore] 都会导致 [IllegalStateException].
     */
    public fun finish()
}


/**
 * 线程安全的按需接收通道.
 *
 * 与 [ReceiveChannel] 不同, [OnDemandReceiveChannel] 只有在调用 [expectMore] 后才会让[生产者][OnDemandSendChannel] 开始生产下一个 [V].
 */
public interface OnDemandReceiveChannel<T, V> {
    /**
     * 尝试从 [OnDemandSendChannel] [接收][OnDemandSendChannel.emit]一个 [V].
     * 当且仅当在 [OnDemandSendChannel] 已经[正常结束][OnDemandSendChannel.finish] 时返回 `null`.
     *
     * 若目前已有 [V], 此函数立即返回该 [V], 不会挂起.
     * 否则, 此函数将会挂起直到 [OnDemandSendChannel.emit].
     *
     * 当此函数被多个协程 (线程) 同时调用时, 只有一个协程会获得 [V], 其他协程将会挂起.
     *
     * 若在等待过程中 [OnDemandSendChannel] [异常结束][OnDemandSendChannel.finishExceptionally],
     * 本函数会立即恢复并抛出 [ProducerFailureException], 其 `cause` 为令 [OnDemandSendChannel] 的异常.
     *
     * 此挂起函数可被取消.
     * 如果在此函数挂起时当前协程的 [Job] 被取消或完结, 此函数会立即恢复并抛出 [CancellationException]. 此行为与 [Deferred.await] 相同.
     *
     * @throws ProducerFailureException 当 [OnDemandSendChannel.finishExceptionally] 时抛出.
     * @throws CancellationException 当协程被取消时抛出
     * @throws IllegalProducerStateException 当状态异常, 如未调用 [expectMore] 时抛出
     */
    @Throws(ProducerFailureException::class, CancellationException::class)
    public suspend fun receiveOrNull(): V?

    /**
     * 期待 [OnDemandSendChannel] 再生产一个 [V].
     * 期望生产后必须在之后调用 [receiveOrNull] 或 [finish] 来消耗生产的 [V].
     * 不可连续重复调用 [expectMore].
     *
     * 在成功发起期待后返回 `true`; 在 [OnDemandSendChannel] 已经[完结][OnDemandSendChannel.finish] 时返回 `false`.
     *
     * @throws IllegalProducerStateException 当 [expectMore] 被调用后, 没有调用 [receiveOrNull] 就又调用了 [expectMore] 时抛出
     */
    public fun expectMore(ticket: T): Boolean

    /**
     * 标记此 [OnDemandSendChannel] 已经不再需要更多的值.
     *
     * 如果 [OnDemandSendChannel] 仍在运行 (无论是挂起中还是正在计算下一个值), 都会正常地[取消][Job.cancel] [OnDemandSendChannel].
     *
     *
     * 在 [finish] 之后若尝试调用 [OnDemandSendChannel.emit], [OnDemandReceiveChannel.receiveOrNull] 或 [OnDemandReceiveChannel.expectMore] 都会导致 [IllegalStateException].
     */
    public fun finish()
}

@Suppress("FunctionName")
public fun <T, V> OnDemandChannel(
    parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    logger: UtilsLogger = UtilsLogger.noop(),
    producerCoroutine: suspend OnDemandSendChannel<T, V>.(initialTicket: T) -> Unit,
): OnDemandReceiveChannel<T, V> = CoroutineOnDemandReceiveChannel(parentCoroutineContext, logger, producerCoroutine)

