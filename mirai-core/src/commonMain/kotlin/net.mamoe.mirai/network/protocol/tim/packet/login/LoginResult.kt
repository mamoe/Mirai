package net.mamoe.mirai.network.protocol.tim.packet.login

/**
 * 登录结果. 除 [SUCCESS] 外均为失败.
 * @see LoginResult.requireSuccess 要求成功
 */
enum class LoginResult {
    /**
     * 登录成功
     */
    SUCCESS,

    /**
     * 密码错误
     */
    WRONG_PASSWORD,

    /**
     * 被冻结
     */
    BLOCKED,

    /**
     * QQ 号码输入有误
     */
    UNKNOWN_QQ_NUMBER,

    /**
     * 账号开启了设备锁. 暂不支持设备锁登录
     */
    DEVICE_LOCK,

    /**
     * 账号被回收
     */
    TAKEN_BACK,

    /**
     * 未知. 更换服务器或等几分钟再登录可能解决.
     */
    UNKNOWN,

    /**
     * 未知. 更换服务器或等几分钟再登录可能解决.
     */
    INTERNAL_ERROR,

    /**
     * 超时
     */
    TIMEOUT,
}

/**
 * 如果 [this] 不为 [LoginResult.SUCCESS] 就抛出消息为 [lazyMessage] 的 [IllegalStateException]
 */
fun LoginResult.requireSuccess(lazyMessage: (LoginResult) -> String) {
    if (this != LoginResult.SUCCESS) error(lazyMessage(this))
}


/**
 * 如果 [this] 不为 [LoginResult.SUCCESS] 就抛出消息为 "Login failed $this" 的 [IllegalStateException]
 */
fun LoginResult.requireSuccess() {
    if (this != LoginResult.SUCCESS) error("Login failed: $this")
}