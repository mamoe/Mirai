/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.notice.processors

import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.internal.BotAccount
import net.mamoe.mirai.internal.QQAndroidBot
import net.mamoe.mirai.internal.contact.FriendImpl
import net.mamoe.mirai.internal.contact.GroupImpl
import net.mamoe.mirai.internal.contact.NormalMemberImpl
import net.mamoe.mirai.internal.contact.StrangerImpl
import net.mamoe.mirai.internal.contact.info.FriendInfoImpl
import net.mamoe.mirai.internal.contact.info.GroupInfoImpl
import net.mamoe.mirai.internal.contact.info.MemberInfoImpl
import net.mamoe.mirai.internal.contact.info.StrangerInfoImpl
import net.mamoe.mirai.internal.network.components.LoggingPacketHandlerAdapter
import net.mamoe.mirai.internal.network.components.NoticeProcessorPipeline.Companion.noticeProcessorPipeline
import net.mamoe.mirai.internal.network.components.NoticeProcessorPipelineImpl
import net.mamoe.mirai.internal.network.components.PacketLoggingStrategyImpl
import net.mamoe.mirai.internal.network.components.ProcessResult
import net.mamoe.mirai.internal.network.framework.AbstractNettyNHTest
import net.mamoe.mirai.internal.network.protocol.packet.IncomingPacket
import net.mamoe.mirai.internal.utils.io.ProtocolStruct
import net.mamoe.mirai.utils.TypeSafeMap
import net.mamoe.mirai.utils.cast
import net.mamoe.mirai.utils.currentTimeSeconds
import net.mamoe.mirai.utils.hexToUBytes


/**
 * To add breakpoint, see [NoticeProcessorPipelineImpl.process]
 */
internal abstract class AbstractNoticeProcessorTest : AbstractNettyNHTest(), GroupExtensions {
    init {
        System.setProperty("mirai.network.notice.pipeline.log.full", "true")
    }

    protected object UseTestContext {
        val EMPTY_BYTE_ARRAY get() = net.mamoe.mirai.utils.EMPTY_BYTE_ARRAY
        fun String.hexToBytes() = hexToUBytes().toByteArray()
    }

    protected suspend inline fun use(
        attributes: TypeSafeMap = TypeSafeMap(),
        block: UseTestContext.() -> ProtocolStruct
    ): ProcessResult {
        val handler = LoggingPacketHandlerAdapter(PacketLoggingStrategyImpl(bot), bot.logger)
        return bot.components.noticeProcessorPipeline.process(bot, block(UseTestContext), attributes).also { list ->
            for (packet in list) {
                handler.handlePacket(IncomingPacket("test", packet))
            }
        }
    }

    fun setBot(id: Long): QQAndroidBot {
        bot = createBot(BotAccount(id, "a"))
        return bot
    }
}

internal interface GroupExtensions {

    @Suppress("TestFunctionName")
    fun GroupInfo(
        uin: Long,
        owner: Long,
        groupCode: Long,
        memo: String = "",
        name: String,
        allowMemberInvite: Boolean = false,
        allowAnonymousChat: Boolean = false,
        autoApprove: Boolean = false,
        confessTalk: Boolean = false,
        muteAll: Boolean = false,
        botMuteTimestamp: Int = 0,
    ): GroupInfoImpl =
        GroupInfoImpl(
            uin, owner, groupCode, memo, name,
            allowMemberInvite, allowAnonymousChat, autoApprove, confessTalk, muteAll,
            botMuteTimestamp
        )

    fun Bot.addGroup(group: Group) {
        groups.delegate.add(group)
    }

    fun Bot.addFriend(friend: Friend) {
        friends.delegate.add(friend)
    }

    fun Bot.addFriend(id: Long, nick: String = "friend$id", remark: String = ""): FriendImpl {
        return FriendImpl(bot.cast(), bot.coroutineContext, FriendInfoImpl(id, nick, remark)).also {
            friends.delegate.add(it)
        }
    }

    fun Bot.addStranger(id: Long, nick: String = "stranger$id", fromGroupId: Long = 0): StrangerImpl {
        return StrangerImpl(bot.cast(), bot.coroutineContext, StrangerInfoImpl(id, nick, fromGroupId)).also {
            strangers.delegate.add(it)
        }
    }

    fun Group.addMember(member: NormalMember) {
        members.delegate.add(member)
    }


    fun Bot.addGroup(
        id: Long,
        owner: Long,
        botPermission: MemberPermission = MemberPermission.MEMBER,
        memo: String = "",
        name: String = "Test Group",
        allowMemberInvite: Boolean = false,
        allowAnonymousChat: Boolean = false,
        autoApprove: Boolean = false,
        confessTalk: Boolean = false,
        muteAll: Boolean = false,
        botMuteTimestamp: Int = 0,
    ): GroupImpl {
        val impl = GroupImpl(
            bot.cast(), coroutineContext, id,
            GroupInfo(
                Mirai.calculateGroupUinByGroupCode(id), owner, id, memo, name, allowMemberInvite,
                allowAnonymousChat, autoApprove, confessTalk, muteAll, botMuteTimestamp
            ),
            ContactList(),
        )
        addGroup(impl)
        impl.botAsMember = impl.addMember(bot.id, nick = bot.nick, permission = botPermission)
        return impl
    }

    fun Bot.addGroup(
        id: Long,
        info: GroupInfoImpl,
        botPermission: MemberPermission = MemberPermission.MEMBER,
    ): Group {
        val impl = GroupImpl(
            bot.cast(), coroutineContext, id, info,
            ContactList(),
        )
        addGroup(impl)
        impl.botAsMember = impl.addMember(bot.id, nick = bot.nick, permission = botPermission)
        return impl
    }

    fun Group.addMember(
        id: Long,
        nick: String = "user$id",
        permission: MemberPermission,
        remark: String = "",
        nameCard: String = "",
        specialTitle: String = "",
        muteTimestamp: Int = 0,
        anonymousId: String? = null,
        joinTimestamp: Int = currentTimeSeconds().toInt(),
        lastSpeakTimestamp: Int = 0,
        isOfficialBot: Boolean = false,
    ): NormalMemberImpl {
        val member = NormalMemberImpl(
            this.cast(), this.coroutineContext,
            MemberInfoImpl(
                id, nick, permission, remark, nameCard,
                specialTitle, muteTimestamp, anonymousId, joinTimestamp, lastSpeakTimestamp, isOfficialBot
            )
        )
        members.delegate.add(
            member
        )
        return member
    }

    fun Group.addMember(
        id: Long,
        info: MemberInfoImpl,
    ): Group {
        members.delegate.add(NormalMemberImpl(this.cast(), this.coroutineContext, info))
        return this
    }
}