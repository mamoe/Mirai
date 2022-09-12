/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

@file:JvmName("LoginSolver_common")

package net.mamoe.mirai.utils

import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.network.RetryLaterException
import net.mamoe.mirai.network.UnsupportedSmsLoginException
import net.mamoe.mirai.utils.LoginSolver.Companion.Default
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName

/**
 * 验证码, 设备锁解决器
 *
 * @see Default
 * @see BotConfiguration.loginSolver
 */
public abstract class LoginSolver {
    /**
     * 处理图片验证码, 返回图片验证码内容.
     *
     * 返回 `null` 以表示无法处理验证码, 将会刷新验证码或重试登录.
     *
     * ## 异常类型
     *
     * 抛出一个 [LoginFailedException] 以正常地终止登录, 并可建议系统进行重连或停止 bot (通过 [LoginFailedException.killBot]).
     * 例如抛出 [RetryLaterException] 可让 bot 重新进行一次登录.
     *
     * 抛出任意其他 [Throwable] 将视为验证码解决器的自身错误.
     *
     * @throws LoginFailedException
     */
    public abstract suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String?

    /**
     * 为 `true` 表示支持滑动验证码, 遇到滑动验证码时 mirai 会请求 [onSolveSliderCaptcha].
     * 否则会跳过滑动验证码并告诉服务器此客户端不支持, 有可能导致登录失败
     */
    public open val isSliderCaptchaSupported: Boolean get() = PlatformLoginSolverImplementations.isSliderCaptchaSupported

    /**
     * 处理滑动验证码.
     *
     * 返回 `null` 以表示无法处理验证码, 将会刷新验证码或重试登录.
     *
     * ## 异常类型
     *
     * 抛出一个 [LoginFailedException] 以正常地终止登录, 并可建议系统进行重连或停止 bot (通过 [LoginFailedException.killBot]).
     * 例如抛出 [RetryLaterException] 可让 bot 重新进行一次登录.
     *
     * 抛出任意其他 [Throwable] 将视为验证码解决器的自身错误.
     *
     * @throws LoginFailedException
     * @return 验证码解决成功后获得的 ticket.
     */
    public abstract suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String?

    /**
     * 处理设备验证.
     *
     * ## 异常类型
     *
     * 抛出一个 [LoginFailedException] 以正常地终止登录, 并可建议系统进行重连或停止 bot (通过 [LoginFailedException.killBot]).
     * 例如抛出 [RetryLaterException] 可让 bot 重新进行一次登录.
     *
     * 抛出任意其他 [Throwable] 将视为验证码解决器的自身错误.
     *
     * @since 验证结果, 可通过解决 [DeviceVerificationRequests] 获得.
     * @throws LoginFailedException
     * @since 2.13
     */
    public open suspend fun onSolveDeviceVerification(
        bot: Bot,
        requests: DeviceVerificationRequests,
    ): DeviceVerificationResult {
        requests.fallback?.let { fallback ->
            @Suppress("DEPRECATION")
            (onSolveUnsafeDeviceLoginVerify(bot, fallback.url))
            return fallback.solved()
        }
        throw UnsupportedSmsLoginException("This login session requires SMS verification, but current LoginSolver($this) does not support it.")
    }

    /**
     * 处理不安全设备验证.
     *
     * 返回值保留给将来使用. 目前在处理完成后返回任意内容 (包含 `null`) 均视为处理成功.
     *
     * ## 异常类型
     *
     * 抛出一个 [LoginFailedException] 以正常地终止登录, 并可建议系统进行重连或停止 bot (通过 [LoginFailedException.killBot]).
     * 例如抛出 [RetryLaterException] 可让 bot 重新进行一次登录.
     *
     * 抛出任意其他 [Throwable] 将视为验证码解决器的自身错误.
     *
     * @return 任意内容. 返回值保留以供未来更新.
     * @throws LoginFailedException
     */
    @Deprecated(
        "Please use onSolveDeviceVerification instead",
        level = DeprecationLevel.WARNING,
        replaceWith = ReplaceWith("onSolveDeviceVerification(bot, url, null)")
    ) // softly
    @DeprecatedSinceMirai(warningSince = "2.13") // for hidden
    public abstract suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String?

    public companion object {
        /**
         * 当前平台默认的 [LoginSolver]。
         *
         * 检测策略:
         * 1. 若是 `mirai-core-api-android` 或 `android.util.Log` 存在, 返回 `null`.
         * 2. 检测 JVM 属性 `mirai.no-desktop`. 若存在, 返回 `StandardCharImageLoginSolver`
         * 3. 检测 JVM 桌面环境, 若支持, 返回 `SwingSolver`
         * 4. 返回 `StandardCharImageLoginSolver`
         *
         * @return `SwingSolver` 或 `StandardCharImageLoginSolver` 或 `null`
         */
        @JvmField
        public val Default: LoginSolver? = PlatformLoginSolverImplementations.default

        @Suppress("unused")
        @Deprecated("Binary compatibility", level = DeprecationLevel.HIDDEN)
        public fun getDefault(): LoginSolver = Default
            ?: error("LoginSolver is not provided by default on your platform. Please specify by BotConfiguration.loginSolver")
    }
}

internal expect object PlatformLoginSolverImplementations {
    val isSliderCaptchaSupported: Boolean
    val default: LoginSolver?
}

/**
 * 属性 [sms] 为短信验证码验证方式, [fallback] 为其他验证方式.
 * 两个属性至少有一个不为 `null`, 在不为 `null` 时表示支持该验证方式. 可任意选用偏好的验证方式.
 *
 * @since 2.13
 */
@NotStableForInheritance
public interface DeviceVerificationRequests {
    /**
     * 短信验证码方式. 在不为 `null` 时表示支持该验证方式.
     */
    public val sms: SmsRequest?

    /**
     * 其他验证方式. 在不为 `null` 时表示支持该验证方式.
     */
    public val fallback: FallbackRequest?

    /**
     * 服务器要求使用短信验证码. 此时可能仍可以尝试 [fallback].
     */
    public val preferSms: Boolean


    /**
     * 服务器要求短信验证时提供的账号绑定的手机信息. 使用 [requestSms] 来请求发送验证码
     *
     * @since 2.13
     * @see LoginSolver.onSolveDeviceVerification
     */
    @NotStableForInheritance
    public interface SmsRequest {
        /**
         * 手机号归属国家代码, 如中国为 86.
         * 在获取失败时会返回 `null`，但通常会获取到
         */
        public val countryCode: String?

        /**
         * 手机号码, 部分数字会被隐藏, 示例: `123*******1`.
         * 在获取失败时会返回 `null`, 但通常会获取到
         */
        public val phoneNumber: String?

        /**
         * 请求服务器发送短信到验证手机号
         *
         * @throws RetryLaterException 当请求过于频繁, 服务器拒绝请求时抛出
         */
        @JvmBlockingBridge
        public suspend fun requestSms()

        /**
         * 通知此请求已被解决. 获取 [DeviceVerificationResult] 用于返回 [LoginSolver.onSolveDeviceVerification].
         */
        public fun solved(code: String): DeviceVerificationResult
    }

    /**
     * 其他验证方式.
     *
     * @since 2.13
     * @see LoginSolver.onSolveDeviceVerification
     */
    @NotStableForInheritance
    public interface FallbackRequest {
        /**
         * HTTP URL. 可能需要在 QQ 浏览器中打开并人工操作.
         */
        public val url: String

        /**
         * 通知此请求已被解决. 获取 [DeviceVerificationResult] 用于返回 [LoginSolver.onSolveDeviceVerification].
         */
        public fun solved(): DeviceVerificationResult
    }
}

/**
 * 设备验证的验证结果. 请不要自行实现此接口, 而是通过解决 [DeviceVerificationRequests] 中的其中一种验证获得.
 *
 * @since 2.13
 * @see LoginSolver.onSolveDeviceVerification
 */
@NotStableForInheritance
public interface DeviceVerificationResult
