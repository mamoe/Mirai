/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("EXPERIMENTAL_API_USAGE", "unused", "FunctionName", "NOTHING_TO_INLINE", "UnusedImport")

package net.mamoe.mirai

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.io.OutputStream
import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.IoBuffer
import kotlinx.io.core.use
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.data.AddFriendResult
import net.mamoe.mirai.data.FriendInfo
import net.mamoe.mirai.data.GroupInfo
import net.mamoe.mirai.data.MemberInfo
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.network.BotNetworkHandler
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.*
import net.mamoe.mirai.utils.io.transferTo
import kotlin.jvm.JvmStatic

/**
 * 机器人对象. 一个机器人实例登录一个 QQ 账号.
 * Mirai 为多账号设计, 可同时维护多个机器人.
 *
 * 注: Bot 为全协程实现, 没有其他任务时若不使用 [join], 主线程将会退出.
 *
 * @see Contact 联系人
 * @see kotlinx.coroutines.isActive 判断 [Bot] 是否正常运行中. (在线, 且没有被 [close])
 */
@UseExperimental(MiraiInternalAPI::class)
abstract class Bot : CoroutineScope {
    companion object {
        /**
         * 复制一份此时的 [Bot] 实例列表.
         */
        @JvmStatic
        val instances: List<WeakRef<Bot>>
            get() = BotImpl.instances.toList()

        /**
         * 遍历每一个 [Bot] 实例
         */
        inline fun forEachInstance(block: (Bot) -> Unit) = BotImpl.forEachInstance(block)

        /**
         * 获取一个 [Bot] 实例, 找不到则 [NoSuchElementException]
         */
        @JvmStatic
        fun instanceWhose(qq: Long): Bot = BotImpl.instanceWhose(qq = qq)
    }

    /**
     * [Bot] 运行的 [Context].
     *
     * 在 JVM 的默认实现为 `class ContextImpl : Context`
     * 在 Android 实现为 `android.content.Context`
     */
    abstract val context: Context

    /**
     * 账号信息
     */
    @MiraiInternalAPI
    abstract val account: BotAccount

    /**
     * QQ 号码. 实际类型为 uint
     */
    abstract val uin: Long

    /**
     * 昵称
     */
    @MiraiExperimentalAPI("还未支持")
    val nick: String
        get() = ""// TODO("bot 昵称获取")

    /**
     * 日志记录器
     */
    abstract val logger: MiraiLogger

    // region contacts

    abstract val selfQQ: QQ

    /**
     * 机器人的好友列表. 它将与服务器同步更新
     */
    abstract val qqs: ContactList<QQ>

    /**
     * 获取一个好友对象. 若没有这个好友, 则会抛出异常 [NoSuchElementException]
     */
    @Deprecated(message = "这个函数有歧义. 它获取的是好友, 却名为 getQQ", replaceWith = ReplaceWith("getFriend(id)"))
    fun getQQ(id: Long): QQ = getFriend(id)

    /**
     * 获取一个好友或一个群.
     * 在一些情况下这可能会造成歧义. 请考虑后使用.
     */
    operator fun get(id: Long): Contact {
        return this.qqs.getOrNull(id) ?: this.groups.getOrNull(id) ?: throw NoSuchElementException("contact id $id")
    }

    /**
     * 判断是否有这个 id 的好友或群.
     * 在一些情况下这可能会造成歧义. 请考虑后使用.
     */
    operator fun contains(id: Long): Boolean {
        return id in this.qqs || id in this.groups
    }

    /**
     * 获取一个好友对象. 若没有这个好友, 则会抛出异常 [NoSuchElementException]
     */
    fun getFriend(id: Long): QQ {
        if (id == uin) return selfQQ
        return qqs.delegate.getOrNull(id)
            ?: throw NoSuchElementException("No such friend $id for bot ${this.uin}")
    }

    /**
     * 构造一个 [QQ] 对象. 它持有对 [Bot] 的弱引用([WeakRef]).
     *
     * [Bot] 无法管理这个对象, 但这个对象会以 [Bot] 的 [Job] 作为父 Job.
     * 因此, 当 [Bot] 被关闭后, 这个对象也会被关闭.
     */
    abstract fun QQ(friendInfo: FriendInfo): QQ

    /**
     * 机器人加入的群列表.
     */
    abstract val groups: ContactList<Group>

    /**
     * 获取一个机器人加入的群.
     *
     * @throws NoSuchElementException 当不存在这个群时
     */
    fun getGroup(id: Long): Group {
        return groups.delegate.getOrNull(id)
            ?: throw NoSuchElementException("No such group $id for bot ${this.uin}")
    }

    /**
     * 向服务器查询群列表. 返回值前 32 bits 为 uin, 后 32 bits 为 groupCode
     */
    abstract suspend fun queryGroupList(): Sequence<Long>

    /**
     * 向服务器查询群资料. 获得的仅为当前时刻的资料.
     * 请优先使用 [getGroup] 然后查看群资料.
     */
    abstract suspend fun queryGroupInfo(groupCode: Long): GroupInfo

    /**
     * 向服务器查询群成员列表.
     * 请优先使用 [getGroup], [Group.members] 查看群成员.
     *
     * 这个函数很慢. 请不要频繁使用.
     *
     * @see Group.calculateGroupUinByGroupCode 使用 groupCode 计算 groupUin
     */
    abstract suspend fun queryGroupMemberList(groupUin: Long, groupCode: Long, ownerId: Long): Sequence<MemberInfo>

    // endregion

    // region network

    /**
     * 网络模块
     */
    abstract val network: BotNetworkHandler

    /**
     * 挂起直到 [Bot] 下线.
     */
    suspend inline fun join() = network.join()

    @Deprecated("使用 join()", ReplaceWith("this.join()"), level = DeprecationLevel.HIDDEN)
    suspend inline fun awaitDisconnection() = join()

    /**
     * 登录, 或重新登录.
     * 这个函数总是关闭一切现有网路任务, 然后重新登录并重新缓存好友列表和群列表.
     *
     * 一般情况下不需要重新登录. Mirai 能够自动处理掉线情况.
     *
     * 最终调用 [net.mamoe.mirai.network.BotNetworkHandler.relogin]
     *
     * @throws LoginFailedException
     */
    abstract suspend fun login()
    // endregion


    // region actions

    @Deprecated("内存使用效率十分低下", ReplaceWith("this.download()"), DeprecationLevel.WARNING)
    abstract suspend fun Image.downloadAsByteArray(): ByteArray

    /**
     * 将图片下载到内存中 (使用 [IoBuffer.Pool])
     */
    abstract suspend fun Image.download(): ByteReadPacket

    /**
     * 添加一个好友
     *
     * @param message 若需要验证请求时的验证消息.
     * @param remark 好友备注
     */
    abstract suspend fun addFriend(id: Long, message: String? = null, remark: String? = null): AddFriendResult

    /**
     * 同意来自陌生人的加好友请求
     */
    abstract suspend fun approveFriendAddRequest(id: Long, remark: String?)

    // endregion

    /**
     * 关闭这个 [Bot], 立即取消 [Bot] 的 [kotlinx.coroutines.SupervisorJob].
     * 之后 [kotlinx.coroutines.isActive] 将会返回 `false`.
     *
     * **注意:** 不可重新登录. 必须重新实例化一个 [Bot].
     *
     * @param cause 原因. 为 null 时视为正常关闭, 非 null 时视为异常关闭
     *
     * @see closeAndJoin 取消并 [Bot.join], 以确保 [Bot] 相关的活动被完全关闭
     */
    abstract fun close(cause: Throwable? = null)

    // region extensions

    final override fun toString(): String {
        return "Bot($uin)"
    }

    /**
     * 需要调用者自行 close [output]
     */
    suspend inline fun Image.downloadTo(output: OutputStream) =
        download().use { input -> input.transferTo(output) }

    // endregion
}

/**
 * 关闭这个 [Bot], 停止一切相关活动. 所有引用都会被释放.
 *
 * 注: 不可重新登录. 必须重新实例化一个 [Bot].
 *
 * @param cause 原因. 为 null 时视为正常关闭, 非 null 时视为异常关闭
 */
suspend inline fun Bot.closeAndJoin(cause: Throwable? = null) {
    close(cause)
    coroutineContext[Job]?.join()
}

inline fun Bot.containsFriend(id: Long): Boolean = id in this.qqs

inline fun Bot.containsGroup(id: Long): Boolean = id in this.groups

inline fun Bot.getFriendOrNull(id: Long): QQ? = this.qqs.getOrNull(id)

inline fun Bot.getGroupOrNull(id: Long): Group? = this.groups.getOrNull(id)