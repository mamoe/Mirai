@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS", "EXPERIMENTAL_API_USAGE")

package net.mamoe.mirai.message

import kotlinx.io.core.ByteReadPacket
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.data.EventPacket
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.*
import kotlin.jvm.JvmName

/**
 * 平台相关扩展
 */
@UseExperimental(MiraiInternalAPI::class)
expect abstract class MessagePacket<TSender : QQ, TSubject : Contact>(bot: Bot) : MessagePacketBase<TSender, TSubject>

@Suppress("NOTHING_TO_INLINE")
@MiraiInternalAPI
abstract class MessagePacketBase<TSender : QQ, TSubject : Contact>(_bot: Bot) : EventPacket, BotEvent() {
    override val bot: Bot by _bot.unsafeWeakRef()

    /**
     * 消息事件主体.
     *
     * 对于好友消息, 这个属性为 [QQ] 的实例;
     * 对于群消息, 这个属性为 [Group] 的实例
     *
     * 在回复消息时, 可通过 [subject] 作为回复对象
     */
    abstract val subject: TSubject

    /**
     * 发送人
     */
    abstract val sender: TSender

    abstract val message: MessageChain


    // region Send to subject

    /**
     * 给这个消息事件的主体发送消息
     * 对于好友消息事件, 这个方法将会给好友 ([subject]) 发送消息
     * 对于群消息事件, 这个方法将会给群 ([subject]) 发送消息
     */
    suspend inline fun reply(message: MessageChain) = subject.sendMessage(message)

    suspend inline fun reply(message: Message) = subject.sendMessage(message.toChain())
    suspend inline fun reply(plain: String) = subject.sendMessage(plain.singleChain())

    @JvmName("reply1")
    suspend inline fun String.reply() = reply(this)

    @JvmName("reply1")
    suspend inline fun Message.reply() = reply(this)

    @JvmName("reply1")
    suspend inline fun MessageChain.reply() = reply(this)

    suspend inline fun ExternalImage.send() = this.sendTo(subject)

    suspend inline fun ExternalImage.upload(): Image = this.upload(subject)
    suspend inline fun Image.send() = this.sendTo(subject)
    suspend inline fun Message.send() = this.sendTo(subject)
    suspend inline fun String.send() = this.toMessage().sendTo(subject)

    inline fun QQ.at(): At = At(this as Member)

    // endregion

    // region Image download
    suspend inline fun Image.downloadAsByteArray(): ByteArray = bot.run { downloadAsByteArray() }

    suspend inline fun Image.download(): ByteReadPacket = bot.run { download() }
    // endregion

    @Deprecated(message = "这个函数有歧义, 将在不久后删除", replaceWith = ReplaceWith("bot.getFriend(this.target)"))
    fun At.qq(): QQ = bot.getFriend(this.target)

    @Deprecated(message = "这个函数有歧义, 将在不久后删除", replaceWith = ReplaceWith("bot.getFriend(this.toLong())"))
    fun Int.qq(): QQ = bot.getFriend(this.coerceAtLeastOrFail(0).toLong())

    @Deprecated(message = "这个函数有歧义, 将在不久后删除", replaceWith = ReplaceWith("bot.getFriend(this)"))
    fun Long.qq(): QQ = bot.getFriend(this.coerceAtLeastOrFail(0))

    @Deprecated(message = "这个函数有歧义, 将在不久后删除", replaceWith = ReplaceWith("bot.getGroup(this)"))
    fun Long.group(): Group = bot.getGroup(this)
}