/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("unused")

package net.mamoe.mirai.event

import kotlinx.coroutines.*
import net.mamoe.mirai.utils.SinceMirai
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass

/**
 * 挂起当前协程, 监听事件 [E], 并尝试从这个事件中**同步**一个值, 在超时时抛出 [TimeoutCancellationException]
 *
 * @param timeoutMillis 超时. 单位为毫秒. `-1` 为不限制.
 * @param mapper 过滤转换器. 返回非 null 则代表得到了需要的值. [syncFromEvent] 会返回这个值
 *
 * @see asyncFromEvent 本函数的异步版本
 *
 * @throws TimeoutCancellationException 在超时后抛出.
 * @throws Throwable 当 [mapper] 抛出任何异常时, 本函数会抛出该异常
 */
@JvmSynthetic
@SinceMirai("0.39.0")
suspend inline fun <reified E : Event, R : Any> syncFromEvent(
    timeoutMillis: Long = -1,
    crossinline mapper: suspend E.(E) -> R?
): R {
    require(timeoutMillis == -1L || timeoutMillis > 0) { "timeoutMillis must be -1 or > 0" }

    return if (timeoutMillis == -1L) {
        coroutineScope {
            syncFromEventImpl<E, R>(E::class, this, mapper)
        }
    } else {
        withTimeout(timeoutMillis) {
            syncFromEventImpl<E, R>(E::class, this, mapper)
        }
    }
}

/**
 * 挂起当前协程, 监听这个事件, 并尝试从这个事件中获取一个值, 在超时时返回 `null`
 *
 * @param timeoutMillis 超时. 单位为毫秒. `-1` 为不限制
 * @param mapper 过滤转换器. 返回非 null 则代表得到了需要的值.
 *
 * @return 超时返回 `null`, 否则返回 [mapper] 返回的第一个非 `null` 值.
 *
 * @see asyncFromEvent 本函数的异步版本
 * @throws Throwable 当 [mapper] 抛出任何异常时, 本函数会抛出该异常
 */
@JvmSynthetic
@SinceMirai("0.39.0")
suspend inline fun <reified E : Event, R : Any> syncFromEventOrNull(
    timeoutMillis: Long,
    crossinline mapper: suspend E.(E) -> R?
): R? {
    require(timeoutMillis > 0) { "timeoutMillis must be > 0" }

    return withTimeoutOrNull(timeoutMillis) {
        syncFromEventImpl<E, R>(E::class, this, mapper)
    }
}

/**
 * 异步监听这个事件, 并尝试从这个事件中获取一个值.
 *
 * 若 [mapper] 抛出的异常将会被传递给 [Deferred.await] 抛出.
 *
 * @param timeoutMillis 超时. 单位为毫秒. `-1` 为不限制
 * @param coroutineContext 额外的 [CoroutineContext]
 * @param mapper 过滤转换器. 返回非 `null` 则代表得到了需要的值. [syncFromEvent] 会返回这个值
 */
@JvmSynthetic
@Suppress("DeferredIsResult")
@SinceMirai("0.39.0")
inline fun <reified E : Event, R : Any> CoroutineScope.asyncFromEventOrNull(
    timeoutMillis: Long,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline mapper: suspend E.(E) -> R?
): Deferred<R?> {
    require(timeoutMillis == -1L || timeoutMillis > 0) { "timeoutMillis must be -1 or > 0" }
    return this.async(coroutineContext) {
        syncFromEventOrNull(timeoutMillis, mapper)
    }
}


/**
 * 异步监听这个事件, 并尝试从这个事件中获取一个值.
 *
 * 若 [mapper] 抛出的异常将会被传递给 [Deferred.await] 抛出.
 *
 * @param timeoutMillis 超时. 单位为毫秒. `-1` 为不限制
 * @param coroutineContext 额外的 [CoroutineContext]
 * @param mapper 过滤转换器. 返回非 null 则代表得到了需要的值. [syncFromEvent] 会返回这个值
 */
@JvmSynthetic
@Suppress("DeferredIsResult")
@SinceMirai("0.39.0")
inline fun <reified E : Event, R : Any> CoroutineScope.asyncFromEvent(
    timeoutMillis: Long = -1,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline mapper: suspend E.(E) -> R?
): Deferred<R> {
    require(timeoutMillis == -1L || timeoutMillis > 0) { "timeoutMillis must be -1 or > 0" }
    return this.async(coroutineContext) {
        syncFromEvent(timeoutMillis, mapper)
    }
}


//////////////
//// internal
//////////////

@JvmSynthetic
@PublishedApi
internal suspend inline fun <E : Event, R> syncFromEventImpl(
    eventClass: KClass<E>,
    coroutineScope: CoroutineScope,
    crossinline mapper: suspend E.(E) -> R?
): R = suspendCancellableCoroutine { cont ->
    coroutineScope.subscribe(eventClass) {
        cont.resumeWith(kotlin.runCatching {
            mapper.invoke(this, it) ?: return@subscribe ListeningStatus.LISTENING
        })
        return@subscribe ListeningStatus.STOPPED
    }
}