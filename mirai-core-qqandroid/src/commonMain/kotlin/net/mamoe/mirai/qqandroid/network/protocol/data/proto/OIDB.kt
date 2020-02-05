package net.mamoe.mirai.qqandroid.network.protocol.data.proto

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable
import net.mamoe.mirai.qqandroid.io.ProtoBuf
import net.mamoe.mirai.qqandroid.network.protocol.packet.EMPTY_BYTE_ARRAY

@Serializable
class Oidb0x88d : ProtoBuf {
    @Serializable
    class GroupExInfoOnly(
        @SerialId(1) val tribeId: Int = 0,
        @SerialId(2) val moneyForAddGroup: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqGroupInfo(
        @SerialId(1) val groupCode: Long = 0L,
        @SerialId(2) val stgroupinfo: Oidb0x88d.GroupInfo? = null,
        @SerialId(3) val lastGetGroupNameTime: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspGroupInfo(
        @SerialId(1) val groupCode: Long = 0L,
        @SerialId(2) val result: Int = 0,
        @SerialId(3) val stgroupinfo: Oidb0x88d.GroupInfo? = null
    ) : ProtoBuf

    @Serializable
    class GroupGeoInfo(
        @SerialId(1) val owneruin: Long = 0L,
        @SerialId(2) val settime: Int = 0,
        @SerialId(3) val cityid: Int = 0,
        @SerialId(4) val int64Longitude: Long = 0L,
        @SerialId(5) val int64Latitude: Long = 0L,
        @SerialId(6) val geocontent: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(7) val poiId: Long = 0L
    ) : ProtoBuf

    @Serializable
    class TagRecord(
        @SerialId(1) val fromUin: Long = 0L,
        @SerialId(2) val groupCode: Long = 0L,
        @SerialId(3) val tagId: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val setTime: Long = 0L,
        @SerialId(5) val goodNum: Int = 0,
        @SerialId(6) val badNum: Int = 0,
        @SerialId(7) val tagLen: Int = 0,
        @SerialId(8) val tagValue: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class GroupInfo(
        @SerialId(1) val groupOwner: Long? = null,
        @SerialId(2) val groupCreateTime: Int? = null,
        @SerialId(3) val groupFlag: Int? = null,
        @SerialId(4) val groupFlagExt: Int? = null,
        @SerialId(5) val groupMemberMaxNum: Int? = null,
        @SerialId(6) val groupMemberNum: Int? = null,
        @SerialId(7) val groupOption: Int? = null,
        @SerialId(8) val groupClassExt: Int? = null,
        @SerialId(9) val groupSpecialClass: Int? = null,
        @SerialId(10) val groupLevel: Int? = null,
        @SerialId(11) val groupFace: Int? = null,
        @SerialId(12) val groupDefaultPage: Int? = null,
        @SerialId(13) val groupInfoSeq: Int? = null,
        @SerialId(14) val groupRoamingTime: Int? = null,
        @SerialId(15) val ingGroupName: ByteArray? = null,
        @SerialId(16) val ingGroupMemo: ByteArray? = null,
        @SerialId(17) val ingGroupFingerMemo: ByteArray? = null,
        @SerialId(18) val ingGroupClassText: ByteArray? = null,
        @SerialId(19) val groupAllianceCode: List<Int>? = null,
        @SerialId(20) val groupExtraAdmNum: Int? = null,
        @SerialId(21) val groupUin: Long? = null,
        @SerialId(22) val groupCurMsgSeq: Int? = null,
        @SerialId(23) val groupLastMsgTime: Int? = null,
        @SerialId(24) val ingGroupQuestion: ByteArray? = null,
        @SerialId(25) val ingGroupAnswer: ByteArray? = null,
        @SerialId(26) val groupVisitorMaxNum: Int? = null,
        @SerialId(27) val groupVisitorCurNum: Int? = null,
        @SerialId(28) val levelNameSeq: Int? = null,
        @SerialId(29) val groupAdminMaxNum: Int? = null,
        @SerialId(30) val groupAioSkinTimestamp: Int? = null,
        @SerialId(31) val groupBoardSkinTimestamp: Int? = null,
        @SerialId(32) val ingGroupAioSkinUrl: ByteArray? = null,
        @SerialId(33) val ingGroupBoardSkinUrl: ByteArray? = null,
        @SerialId(34) val groupCoverSkinTimestamp: Int? = null,
        @SerialId(35) val ingGroupCoverSkinUrl: ByteArray? = null,
        @SerialId(36) val groupGrade: Int? = null,
        @SerialId(37) val activeMemberNum: Int? = null,
        @SerialId(38) val certificationType: Int? = null,
        @SerialId(39) val ingCertificationText: ByteArray? = null,
        @SerialId(40) val ingGroupRichFingerMemo: ByteArray? = null,
        @SerialId(41) val tagRecord: List<TagRecord>? = null,
        @SerialId(42) val groupGeoInfo: GroupGeoInfo? = null,
        @SerialId(43) val headPortraitSeq: Int? = null,
        @SerialId(44) val msgHeadPortrait: GroupHeadPortrait? = null,
        @SerialId(45) val shutupTimestamp: Int? = null,
        @SerialId(46) val shutupTimestampMe: Int? = null,
        @SerialId(47) val createSourceFlag: Int? = null,
        @SerialId(48) val cmduinMsgSeq: Int? = null,
        @SerialId(49) val cmduinJoinTime: Int? = null,
        @SerialId(50) val cmduinUinFlag: Int? = null,
        @SerialId(51) val cmduinFlagEx: Int? = null,
        @SerialId(52) val cmduinNewMobileFlag: Int? = null,
        @SerialId(53) val cmduinReadMsgSeq: Int? = null,
        @SerialId(54) val cmduinLastMsgTime: Int? = null,
        @SerialId(55) val groupTypeFlag: Int? = null,
        @SerialId(56) val appPrivilegeFlag: Int? = null,
        @SerialId(57) val stGroupExInfo: GroupExInfoOnly? = null,
        @SerialId(58) val groupSecLevel: Int? = null,
        @SerialId(59) val groupSecLevelInfo: Int? = null,
        @SerialId(60) val cmduinPrivilege: Int? = null,
        @SerialId(61) val ingPoidInfo: ByteArray? = null,
        @SerialId(62) val cmduinFlagEx2: Int? = null,
        @SerialId(63) val confUin: Long? = null,
        @SerialId(64) val confMaxMsgSeq: Int? = null,
        @SerialId(65) val confToGroupTime: Int? = null,
        @SerialId(66) val passwordRedbagTime: Int? = null,
        @SerialId(67) val subscriptionUin: Long? = null,
        @SerialId(68) val memberListChangeSeq: Int? = null,
        @SerialId(69) val membercardSeq: Int? = null,
        @SerialId(70) val rootId: Long? = null,
        @SerialId(71) val parentId: Long? = null,
        @SerialId(72) val teamSeq: Int? = null,
        @SerialId(73) val historyMsgBeginTime: Long? = null,
        @SerialId(74) val inviteNoAuthNumLimit: Long? = null,
        @SerialId(75) val cmduinHistoryMsgSeq: Int? = null,
        @SerialId(76) val cmduinJoinMsgSeq: Int? = null,
        @SerialId(77) val groupFlagext3: Int? = null,
        @SerialId(78) val groupOpenAppid: Int? = null,
        @SerialId(79) val isConfGroup: Int? = null,
        @SerialId(80) val isModifyConfGroupFace: Int? = null,
        @SerialId(81) val isModifyConfGroupName: Int? = null,
        @SerialId(82) val noFingerOpenFlag: Int? = null,
        @SerialId(83) val noCodeFingerOpenFlag: Int? = null,
        @SerialId(84) val autoAgreeJoinGroupUserNumForNormalGroup: Int? = null,
        @SerialId(85) val autoAgreeJoinGroupUserNumForConfGroup: Int? = null,
        @SerialId(86) val isAllowConfGroupMemberNick: Int? = null,
        @SerialId(87) val isAllowConfGroupMemberAtAll: Int? = null,
        @SerialId(88) val isAllowConfGroupMemberModifyGroupName: Int? = null,
        @SerialId(89) val ingLongGroupName: ByteArray? = null,
        @SerialId(90) val cmduinJoinRealMsgSeq: Int? = null,
        @SerialId(91) val isGroupFreeze: Int? = null,
        @SerialId(92) val msgLimitFrequency: Int? = null,
        @SerialId(93) val joinGroupAuth: ByteArray? = null,
        @SerialId(94) val hlGuildAppid: Int? = null,
        @SerialId(95) val hlGuildSubType: Int? = null,
        @SerialId(96) val hlGuildOrgid: Int? = null,
        @SerialId(97) val isAllowHlGuildBinary: Int? = null,
        @SerialId(98) val cmduinRingtoneId: Int? = null,
        @SerialId(99) val groupFlagext4: Int? = null,
        @SerialId(100) val groupFreezeReason: Int? = null
    ) : ProtoBuf

    @Serializable
    class GroupHeadPortraitInfo(
        @SerialId(1) val uint32PicId: Int = 0,
        @SerialId(2) val leftX: Int = 0,
        @SerialId(3) val leftY: Int = 0,
        @SerialId(4) val rightX: Int = 0,
        @SerialId(5) val rightY: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val stzrspgroupinfo: List<Oidb0x88d.RspGroupInfo>? = null,
        @SerialId(2) val errorinfo: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val stzreqgroupinfo: List<Oidb0x88d.ReqGroupInfo>? = null,
        @SerialId(3) val pcClientVersion: Int = 0
    ) : ProtoBuf

    @Serializable
    class GroupHeadPortrait(
        @SerialId(1) val picCnt: Int = 0,
        @SerialId(2) val msgInfo: List<Oidb0x88d.GroupHeadPortraitInfo>? = null,
        @SerialId(3) val defaultId: Int = 0,
        @SerialId(4) val verifyingPicCnt: Int = 0,
        @SerialId(5) val msgVerifyingpicInfo: List<Oidb0x88d.GroupHeadPortraitInfo>? = null
    ) : ProtoBuf
}

@Serializable
class Oidb0x89a : ProtoBuf {
    @Serializable
    class GroupNewGuidelinesInfo(
        @SerialId(1) val boolEnabled: Boolean = false,
        @SerialId(2) val ingContent: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class Groupinfo(
        @SerialId(1) val groupExtAdmNum: Int? = null,
        @SerialId(2) val flag: Int? = null,
        @SerialId(3) val ingGroupName: ByteArray? = null,
        @SerialId(4) val ingGroupMemo: ByteArray? = null,
        @SerialId(5) val ingGroupFingerMemo: ByteArray? = null,
        @SerialId(6) val ingGroupAioSkinUrl: ByteArray? = null,
        @SerialId(7) val ingGroupBoardSkinUrl: ByteArray? = null,
        @SerialId(8) val ingGroupCoverSkinUrl: ByteArray? = null,
        @SerialId(9) val groupGrade: Int? = null,
        @SerialId(10) val activeMemberNum: Int? = null,
        @SerialId(11) val certificationType: Int? = null,
        @SerialId(12) val ingCertificationText: ByteArray? = null,
        @SerialId(13) val ingGroupRichFingerMemo: ByteArray? = null,
        @SerialId(14) val stGroupNewguidelines: Oidb0x89a.GroupNewGuidelinesInfo? = null,
        @SerialId(15) val groupFace: Int? = null,
        @SerialId(16) val addOption: Int? = null,
        @SerialId(17) val shutupTime: Int? = null,
        @SerialId(18) val groupTypeFlag: Int? = null,
        @SerialId(19) val stringGroupTag: List<ByteArray>? = null,
        @SerialId(20) val msgGroupGeoInfo: Oidb0x89a.GroupGeoInfo? = null,
        @SerialId(21) val groupClassExt: Int? = null,
        @SerialId(22) val ingGroupClassText: ByteArray? = null,
        @SerialId(23) val appPrivilegeFlag: Int? = null,
        @SerialId(24) val appPrivilegeMask: Int? = null,
        @SerialId(25) val stGroupExInfo: Oidb0x89a.GroupExInfoOnly? = null,
        @SerialId(26) val groupSecLevel: Int? = null,
        @SerialId(27) val groupSecLevelInfo: Int? = null,
        @SerialId(28) val subscriptionUin: Long? = null,
        @SerialId(29) val allowMemberInvite: Int? = null,
        @SerialId(30) val ingGroupQuestion: ByteArray? = null,
        @SerialId(31) val ingGroupAnswer: ByteArray? = null,
        @SerialId(32) val groupFlagext3: Int? = null,
        @SerialId(33) val groupFlagext3Mask: Int? = null,
        @SerialId(34) val groupOpenAppid: Int? = null,
        @SerialId(35) val noFingerOpenFlag: Int? = null,
        @SerialId(36) val noCodeFingerOpenFlag: Int? = null,
        @SerialId(37) val rootId: Long? = null,
        @SerialId(38) val msgLimitFrequency: Int? = null
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val groupCode: Long = 0L,
        @SerialId(2) val errorinfo: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class GroupExInfoOnly(
        @SerialId(1) val tribeId: Int = 0,
        @SerialId(2) val moneyForAddGroup: Int = 0
    ) : ProtoBuf

    @Serializable
    class GroupGeoInfo(
        @SerialId(1) val cityId: Int = 0,
        @SerialId(2) val longtitude: Long = 0L,
        @SerialId(3) val latitude: Long = 0L,
        @SerialId(4) val ingGeoContent: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val poiId: Long = 0L
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val groupCode: Long = 0L,
        @SerialId(2) val stGroupInfo: Oidb0x89a.Groupinfo? = null,
        @SerialId(3) val originalOperatorUin: Long = 0L,
        @SerialId(4) val reqGroupOpenAppid: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x7cb : ProtoBuf {
    @Serializable
    class ConfigItem(
        @SerialId(1) val id: Int = 0,
        @SerialId(2) val config: String = ""
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val timeStamp: Int = 0,
        @SerialId(2) val timeGap: Int = 0,
        @SerialId(3) val commentConfigs: List<Cmd0x7cb.CommentConfig>? = null,
        @SerialId(4) val attendTipsToA: String = "",
        @SerialId(5) val firstMsgTips: String = "",
        @SerialId(6) val cancleConfig: List<Cmd0x7cb.ConfigItem>? = null,
        @SerialId(7) val msgDateRequest: Cmd0x7cb.DateRequest? = null,
        @SerialId(8) val msgHotLocale: List<ByteArray>? = null,//List<AppointDefine.LocaleInfo>
        @SerialId(9) val msgTopicList: List<Cmd0x7cb.TopicConfig>? = null,
        @SerialId(10) val travelMsgTips: String = "",
        @SerialId(11) val travelProfileTips: String = "",
        @SerialId(12) val travelAttenTips: String = "",
        @SerialId(13) val topicDefault: Int = 0
    ) : ProtoBuf

    @Serializable
    class CommentConfig(
        @SerialId(1) val appointSubject: Int = 0,
        @SerialId(2) val msgConfigs: List<Cmd0x7cb.ConfigItem>? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val timeStamp: Int = 0
    ) : ProtoBuf

    @Serializable
    class DateRequest(
        @SerialId(1) val time: Int = 0,
        @SerialId(2) val errMsg: String = ""
    ) : ProtoBuf

    @Serializable
    class TopicConfig(
        @SerialId(1) val topicId: Int = 0,
        @SerialId(2) val topicName: String = "",
        @SerialId(3) val deadline: Int = 0,
        @SerialId(4) val errDeadline: String = ""
    ) : ProtoBuf
}

@Serializable
class Oidb0x87a : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val country: String = "",
        @SerialId(2) val telephone: String = "",
        @SerialId(3) val resendInterval: Int = 0,
        @SerialId(4) val guid: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val country: String = "",
        @SerialId(2) val telephone: String = "",
        @SerialId(3) val guid: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val enumButype: Int /* enum */ = 0
    ) : ProtoBuf
}

@Serializable
class GroupAppPb : ProtoBuf {
    @Serializable
    class ClientInfo(
        @SerialId(1) val platform: Int = 0,
        @SerialId(2) val version: String = ""
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val fullList: GroupAppPb.AppList? = null,
        @SerialId(2) val groupGrayList: GroupAppPb.AppList? = null,
        @SerialId(3) val redPointList: GroupAppPb.AppList? = null,
        @SerialId(4) val cacheInterval: Int = 0
    ) : ProtoBuf

    @Serializable
    class AppList(
        @SerialId(1) val hash: String = "",
        @SerialId(2) val infos: List<GroupAppPb.AppInfo>? = null
    ) : ProtoBuf

    @Serializable
    class AppInfo(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val icon: String = "",
        @SerialId(3) val name: String = "",
        @SerialId(4) val url: String = "",
        @SerialId(5) val isGray: Int = 0,
        @SerialId(6) val iconSimpleDay: String = "",
        @SerialId(7) val iconSimpleNight: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val client: GroupAppPb.ClientInfo? = null,
        @SerialId(2) val groupId: Long = 0L,
        @SerialId(3) val groupType: Int = 0,
        @SerialId(4) val fullListHash: String = "",
        @SerialId(5) val groupGrayListHash: String = ""
    ) : ProtoBuf
}

@Serializable
class Oidb0xc34 : ProtoBuf {
    @Serializable
    class RspBody : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Cmd0x5fd : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val msgComment: AppointDefine.DateComment? = null,
        @SerialId(2) val maxFetchCount: Int = 0,
        @SerialId(3) val lastCommentId: String = ""
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgComment: List<AppointDefine.DateComment>? = null,
        @SerialId(2) val errorTips: String = "",
        @SerialId(3) val clearCacheFlag: Int = 0,
        @SerialId(4) val commentWording: String = "",
        @SerialId(5) val commentNum: Int = 0
    ) : ProtoBuf
}

@Serializable
class Oidb0xbcb : ProtoBuf {
    @Serializable
    class CheckUrlReqItem(
        @SerialId(1) val url: String = "",
        @SerialId(2) val refer: String = "",
        @SerialId(3) val plateform: String = "",
        @SerialId(4) val qqPfTo: String = "",
        @SerialId(5) val msgType: Int = 0,
        @SerialId(6) val msgFrom: Int = 0,
        @SerialId(7) val msgChatid: Long = 0L,
        @SerialId(8) val serviceType: Long = 0L,
        @SerialId(9) val sendUin: Long = 0L,
        @SerialId(10) val reqType: String = ""
    ) : ProtoBuf

    @Serializable
    class CheckUrlRsp(
        @SerialId(1) val results: List<Oidb0xbcb.UrlCheckResult>? = null,
        @SerialId(2) val nextReqDuration: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(9) val notUseCache: Int = 0,
        @SerialId(10) val checkUrlReq: Oidb0xbcb.CheckUrlReq? = null
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(10) val checkUrlRsp: Oidb0xbcb.CheckUrlRsp? = null
    ) : ProtoBuf

    @Serializable
    class CheckUrlReq(
        @SerialId(1) val url: List<String> = listOf(),
        @SerialId(2) val refer: String = "",
        @SerialId(3) val plateform: String = "",
        @SerialId(4) val qqPfTo: String = "",
        @SerialId(5) val msgType: Int = 0,
        @SerialId(6) val msgFrom: Int = 0,
        @SerialId(7) val msgChatid: Long = 0L,
        @SerialId(8) val serviceType: Long = 0L,
        @SerialId(9) val sendUin: Long = 0L,
        @SerialId(10) val reqType: String = "",
        @SerialId(11) val originalUrl: String = ""
    ) : ProtoBuf

    @Serializable
    class UrlCheckResult(
        @SerialId(1) val url: String = "",
        @SerialId(2) val result: Int = 0,
        @SerialId(3) val jumpResult: Int = 0,
        @SerialId(4) val jumpUrl: String = "",
        @SerialId(5) val level: Int = 0,
        @SerialId(6) val subLevel: Int = 0,
        @SerialId(7) val umrtype: Int = 0,
        @SerialId(8) val retFrom: Int = 0,
        @SerialId(9) val operationBit: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Oidb0xbfe : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val receiveStatus: Int = 0,
        @SerialId(2) val jumpUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val flag: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Oidb0xbe8 : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val enumOpCode: Int /* enum */ = 1,
        @SerialId(3) val rspOfPopupFlag: Int = 0,
        @SerialId(4) val popupCountNow: Int = 0
    ) : ProtoBuf

    @Serializable
    class PopupResult(
        @SerialId(1) val popupResult: Int = 0,
        @SerialId(2) val popupFieldid: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val enumOpCode: Int /* enum */ = 1,
        @SerialId(3) val reqOfPopupFlag: Int = 0,
        @SerialId(4) val rstOfPopupFlag: Int = 0,
        @SerialId(5) val mqq808WelcomepageFlag: Int = 0,
        @SerialId(6) val msgPopupResult: List<Oidb0xbe8.PopupResult>? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x7de : ProtoBuf {
    @Serializable
    class UserProfile(
        @SerialId(1) val msgPublisherInfo: AppointDefine.PublisherInfo? = null,
        @SerialId(2) val msgAppointsInfo: AppointDefine.AppointInfo? = null,
        @SerialId(3) val msgVistorInfo: List<AppointDefine.StrangerInfo>? = null
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgHead: Cmd0x7de.BusiRespHead? = null,
        @SerialId(2) val msgUserList: List<Cmd0x7de.UserProfile>? = null,
        @SerialId(3) val ended: Int = 0,
        @SerialId(4) val cookie: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class BusiRespHead(
        @SerialId(1) val int32Version: Int = 1,
        @SerialId(2) val int32Seq: Int = 0,
        @SerialId(3) val int32ReplyCode: Int = 0,
        @SerialId(4) val result: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val msgHead: Cmd0x7de.BusiReqHead? = null,
        @SerialId(2) val msgLbsInfo: AppointDefine.LBSInfo? = null,
        @SerialId(3) val time: Int = 0,
        @SerialId(4) val subject: Int = 0,
        @SerialId(5) val gender: Int = 0,
        @SerialId(6) val ageLow: Int = 0,
        @SerialId(7) val ageUp: Int = 0,
        @SerialId(8) val profession: Int = 0,
        @SerialId(9) val cookie: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(10) val msgDestination: AppointDefine.LocaleInfo? = null
    ) : ProtoBuf

    @Serializable
    class BusiReqHead(
        @SerialId(1) val int32Version: Int = 1,
        @SerialId(2) val int32Seq: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x7a8 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val reqUin: Long = 0L,
        @SerialId(11) val onlyObtained: Int = 0,
        @SerialId(12) val readReport: Int = 0,
        @SerialId(13) val sortType: Int = 0,
        @SerialId(14) val onlyNew: Int = 0,
        @SerialId(15) val filterMedalIds: List<Int>? = null,
        @SerialId(16) val onlySummary: Int = 0,
        @SerialId(17) val doScan: Int = 0,
        @SerialId(18) val startTimestamp: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val nick: String = "",
        @SerialId(2) val metalRank: Int = 0,
        @SerialId(3) val friCount: Int = 0,
        @SerialId(4) val metalCount: Int = 0,
        @SerialId(5) val metalTotal: Int = 0,
        @SerialId(6) val msgMedal: List<Common.MedalInfo>? = null,
        @SerialId(8) val totalPoint: Int = 0,
        @SerialId(9) val int32NewCount: Int = 0,
        @SerialId(10) val int32UpgradeCount: Int = 0,
        @SerialId(11) val promptParams: String = "",
        @SerialId(12) val now: Int = 0
    ) : ProtoBuf

    @Serializable
    class MedalNews(
        @SerialId(1) val friUin: Long = 0L,
        @SerialId(2) val friNick: String = "",
        @SerialId(3) val msgMedal: Common.MedalInfo? = null
    ) : ProtoBuf
}


@Serializable
class Cmd0x5fe : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val msgAppointId: AppointDefine.AppointID? = null,
        @SerialId(2) val commentId: String = "",
        @SerialId(3) val fetchOldCount: Int = 0,
        @SerialId(4) val fetchNewCount: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgComment: List<AppointDefine.DateComment>? = null,
        @SerialId(2) val errorTips: String = "",
        @SerialId(3) val fetchOldOver: Int = 0,
        @SerialId(4) val fetchNewOver: Int = 0
    ) : ProtoBuf
}

@Serializable
class Oidb0xc35 : ProtoBuf {
    @Serializable
    class RspBody : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val msgExposeInfo: List<Oidb0xc35.ExposeItem>? = null
    ) : ProtoBuf

    @Serializable
    class ExposeItem(
        @SerialId(1) val friend: Long = 0L,
        @SerialId(2) val pageId: Int = 0,
        @SerialId(3) val entranceId: Int = 0,
        @SerialId(4) val actionId: Int = 0,
        @SerialId(5) val exposeCount: Int = 0,
        @SerialId(6) val exposeTime: Int = 0,
        @SerialId(7) val algoBuffer: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(8) val addition: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf
}

@Serializable
class Oidb0xc0d : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val completedTaskStamp: Long = 0L,
        @SerialId(2) val errMsg: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val taskType: Int = 0,
        @SerialId(3) val taskPoint: Int = 0
    ) : ProtoBuf
}

@Serializable
class OidbSso : ProtoBuf {
    @Serializable
    class OIDBSSOPkg(
        @SerialId(1) val command: Int = 0,
        @SerialId(2) val serviceType: Int = 0,
        @SerialId(3) val result: Int = 0,
        @SerialId(4) val bodybuffer: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val errorMsg: String = "",
        @SerialId(6) val clientVersion: String = ""
    ) : ProtoBuf
}

@Serializable
class Cmd0xc83 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(101) val fromUin: Long = 0L,
        @SerialId(102) val toUin: Long = 0L,
        @SerialId(103) val op: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(101) val result: Int = 0,
        @SerialId(102) val retryInterval: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0xccb : ProtoBuf {
    @Serializable
    class GroupMsgInfo(
        @SerialId(1) val msgSeq: Int = 0,
        @SerialId(2) val roamFlag: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val type: Int = 0,
        @SerialId(2) val destUin: Long = 0L,
        @SerialId(3) val groupCode: Long = 0L,
        @SerialId(4) val c2cMsg: List<Cmd0xccb.C2cMsgInfo>? = null,
        @SerialId(5) val groupMsg: List<Cmd0xccb.GroupMsgInfo>? = null
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val type: Int = 0,
        @SerialId(2) val destUin: Long = 0L,
        @SerialId(3) val groupCode: Long = 0L,
        @SerialId(4) val c2cMsg: List<Cmd0xccb.C2cMsgInfo>? = null,
        @SerialId(5) val groupMsg: List<Cmd0xccb.GroupMsgInfo>? = null,
        @SerialId(6) val resId: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class C2cMsgInfo(
        @SerialId(1) val msgSeq: Int = 0,
        @SerialId(2) val msgTime: Int = 0,
        @SerialId(3) val msgRandom: Int = 0,
        @SerialId(4) val roamFlag: Int = 0
    ) : ProtoBuf
}

@Serializable
class Oidb0xd84 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val xmitinfo: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val xmitinfo: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf
}

@Serializable
class Oidb0x5e1 : ProtoBuf {
    @Serializable
    class UdcUinData(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(4) val openid: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20002) val nick: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20003) val country: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20004) val province: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20009) val gender: Int = 0,
        @SerialId(20014) val allow: Int = 0,
        @SerialId(20015) val faceId: Int = 0,
        @SerialId(20020) val city: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20027) val commonPlace1: Int = 0,
        @SerialId(20030) val mss3Bitmapextra: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20031) val birthday: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20032) val cityId: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(20033) val lang1: Int = 0,
        @SerialId(20034) val lang2: Int = 0,
        @SerialId(20035) val lang3: Int = 0,
        @SerialId(20041) val cityZoneId: Int = 0,
        @SerialId(20056) val oin: Int = 0,
        @SerialId(20059) val bubbleId: Int = 0,
        @SerialId(21001) val mss2Identity: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(21002) val mss1Service: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(21003) val lflag: Int = 0,
        @SerialId(21004) val extFlag: Int = 0,
        @SerialId(21006) val basicSvrFlag: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(21007) val basicCliFlag: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(24101) val pengyouRealname: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(24103) val pengyouGender: Int = 0,
        @SerialId(24118) val pengyouFlag: Int = 0,
        @SerialId(26004) val fullBirthday: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(26005) val fullAge: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(26010) val simpleUpdateTime: Int = 0,
        @SerialId(26011) val mssUpdateTime: Int = 0,
        @SerialId(27022) val groupMemCreditFlag: Int = 0,
        @SerialId(27025) val faceAddonId: Long = 0L,
        @SerialId(27026) val musicGene: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(40323) val fileShareBit: Int = 0,
        @SerialId(40404) val recommendPrivacyCtrl: Int = 0,
        @SerialId(40505) val oldFriendChat: Int = 0,
        @SerialId(40602) val businessBit: Int = 0,
        @SerialId(41305) val crmBit: Int = 0,
        @SerialId(41810) val forbidFileshareBit: Int = 0,
        @SerialId(42333) val userLoginGuardFace: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(11) val msgUinData: List<Oidb0x5e1.UdcUinData>? = null,
        @SerialId(12) val uint64UnfinishedUins: List<Long>? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uint64Uins: List<Long>? = null,
        @SerialId(2) val startTime: Int = 0,
        @SerialId(3) val maxPackageSize: Int = 0,
        @SerialId(4) val bytesOpenid: List<ByteArray>? = null,
        @SerialId(5) val appid: Int = 0,
        @SerialId(20002) val reqNick: Int = 0,
        @SerialId(20003) val reqCountry: Int = 0,
        @SerialId(20004) val reqProvince: Int = 0,
        @SerialId(20009) val reqGender: Int = 0,
        @SerialId(20014) val reqAllow: Int = 0,
        @SerialId(20015) val reqFaceId: Int = 0,
        @SerialId(20020) val reqCity: Int = 0,
        @SerialId(20027) val reqCommonPlace1: Int = 0,
        @SerialId(20030) val reqMss3Bitmapextra: Int = 0,
        @SerialId(20031) val reqBirthday: Int = 0,
        @SerialId(20032) val reqCityId: Int = 0,
        @SerialId(20033) val reqLang1: Int = 0,
        @SerialId(20034) val reqLang2: Int = 0,
        @SerialId(20035) val reqLang3: Int = 0,
        @SerialId(20041) val reqCityZoneId: Int = 0,
        @SerialId(20056) val reqOin: Int = 0,
        @SerialId(20059) val reqBubbleId: Int = 0,
        @SerialId(21001) val reqMss2Identity: Int = 0,
        @SerialId(21002) val reqMss1Service: Int = 0,
        @SerialId(21003) val reqLflag: Int = 0,
        @SerialId(21004) val reqExtFlag: Int = 0,
        @SerialId(21006) val reqBasicSvrFlag: Int = 0,
        @SerialId(21007) val reqBasicCliFlag: Int = 0,
        @SerialId(24101) val reqPengyouRealname: Int = 0,
        @SerialId(24103) val reqPengyouGender: Int = 0,
        @SerialId(24118) val reqPengyouFlag: Int = 0,
        @SerialId(26004) val reqFullBirthday: Int = 0,
        @SerialId(26005) val reqFullAge: Int = 0,
        @SerialId(26010) val reqSimpleUpdateTime: Int = 0,
        @SerialId(26011) val reqMssUpdateTime: Int = 0,
        @SerialId(27022) val reqGroupMemCreditFlag: Int = 0,
        @SerialId(27025) val reqFaceAddonId: Int = 0,
        @SerialId(27026) val reqMusicGene: Int = 0,
        @SerialId(40323) val reqFileShareBit: Int = 0,
        @SerialId(40404) val reqRecommendPrivacyCtrlBit: Int = 0,
        @SerialId(40505) val reqOldFriendChatBit: Int = 0,
        @SerialId(40602) val reqBusinessBit: Int = 0,
        @SerialId(41305) val reqCrmBit: Int = 0,
        @SerialId(41810) val reqForbidFileshareBit: Int = 0,
        @SerialId(42333) val userLoginGuardFace: Int = 0
    ) : ProtoBuf
}

@Serializable
class Oidb0xc90 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val communityBid: List<Long>? = null,
        @SerialId(2) val page: Int = 0
    ) : ProtoBuf

    @Serializable
    class CommunityWebInfo(
        @SerialId(1) val communityInfoItem: List<Oidb0xc90.CommunityConfigInfo>? = null,
        @SerialId(2) val page: Int = 0,
        @SerialId(3) val end: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val communityInfoItem: List<Oidb0xc90.CommunityConfigInfo>? = null,
        @SerialId(2) val jumpConcernCommunityUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val communityTitleWording: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val moreUrlWording: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val webCommunityInfo: Oidb0xc90.CommunityWebInfo? = null,
        @SerialId(6) val jumpCommunityChannelUrl: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class CommunityConfigInfo(
        @SerialId(1) val jumpHomePageUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val name: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val picUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val dynamicCount: Int = 0,
        @SerialId(5) val communityBid: Long = 0L,
        @SerialId(6) val followStatus: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0xd8a : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val retcode: Int = 0,
        @SerialId(2) val res: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val cmd: Int = 0,
        @SerialId(3) val body: String = "",
        @SerialId(4) val clientInfo: Cmd0xd8a.ClientInfo? = null
    ) : ProtoBuf

    @Serializable
    class ClientInfo(
        @SerialId(1) val implat: Int = 0,
        @SerialId(2) val ingClientver: String = ""
    ) : ProtoBuf
}

@Serializable
class Oidb0xb6f : ProtoBuf {
    @Serializable
    class ReportFreqRspBody(
        @SerialId(1) val identity: Oidb0xb6f.Identity? = null,
        @SerialId(4) val remainTimes: Long = 0L,
        @SerialId(5) val expireTime: Int = 0
    ) : ProtoBuf

    @Serializable
    class Identity(
        @SerialId(1) val apiName: String = "",
        @SerialId(2) val appid: Int = 0,
        @SerialId(3) val apptype: Int = 0,
        @SerialId(4) val bizid: Int = 0,
        @SerialId(10) val intExt1: Long = 0L,
        @SerialId(20) val ext1: String = ""
    ) : ProtoBuf

    @Serializable
    class ThresholdInfo(
        @SerialId(1) val thresholdPerMinute: Long = 0L,
        @SerialId(2) val thresholdPerDay: Long = 0L,
        @SerialId(3) val thresholdPerHour: Long = 0L,
        @SerialId(4) val thresholdPerWeek: Long = 0L
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val reportFreqRsp: Oidb0xb6f.ReportFreqRspBody? = null
    ) : ProtoBuf

    @Serializable
    class ReportFreqReqBody(
        @SerialId(1) val identity: Oidb0xb6f.Identity? = null,
        @SerialId(2) val invokeTimes: Long = 1L
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val reportFreqReq: Oidb0xb6f.ReportFreqReqBody? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x7dc : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val seq: Int = 0,
        @SerialId(2) val wording: String = "",
        @SerialId(3) val msgAppointInfo: List<AppointDefine.AppointInfo>? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val seq: Int = 0,
        @SerialId(2) val msgAppointment: AppointDefine.AppointContent? = null,
        @SerialId(3) val msgLbsInfo: AppointDefine.LBSInfo? = null,
        @SerialId(4) val overwrite: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x7cd : ProtoBuf {
    @Serializable
    class AppointBrife(
        @SerialId(1) val msgPublisherInfo: AppointDefine.PublisherInfo? = null,
        @SerialId(2) val msgAppointsInfo: AppointDefine.AppointInfo? = null
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val stamp: Int = 0,
        @SerialId(2) val over: Int = 0,
        @SerialId(3) val next: Int = 0,
        @SerialId(4) val msgAppointsInfo: List<Cmd0x7cd.AppointBrife>? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val stamp: Int = 0,
        @SerialId(2) val start: Int = 0,
        @SerialId(3) val want: Int = 0,
        @SerialId(4) val msgLbsInfo: AppointDefine.LBSInfo? = null,
        @SerialId(5) val msgAppointIds: List<AppointDefine.AppointID>? = null,
        @SerialId(6) val appointOperation: Int = 0,
        @SerialId(100) val requestUin: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Oidb0xc0c : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val isTaskCompleted: Int = 0,
        @SerialId(2) val taskPoint: Int = 0,
        @SerialId(3) val guideWording: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val needShowProgress: Int = 0,
        @SerialId(5) val originalProgress: Int = 0,
        @SerialId(6) val nowProgress: Int = 0,
        @SerialId(7) val totalProgress: Int = 0,
        @SerialId(8) val needExecTask: Int = 0
    ) : ProtoBuf

    @Serializable
    class VideoSrcType(
        @SerialId(1) val sourceType: Int = 0,
        @SerialId(2) val videoFromType: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val taskType: Int = 0,
        @SerialId(3) val rowkey: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val feedsId: Long = 0L,
        @SerialId(5) val msgVideoFromType: Oidb0xc0c.VideoSrcType? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x5fb : ProtoBuf {
    @Serializable
    class ReqInfo(
        @SerialId(3) val time: Int = 0,
        @SerialId(4) val subject: Int = 0,
        @SerialId(5) val gender: Int = 0,
        @SerialId(6) val ageLow: Int = 0,
        @SerialId(7) val ageUp: Int = 0,
        @SerialId(8) val profession: Int = 0,
        @SerialId(9) val cookie: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(10) val msgDestination: AppointDefine.LocaleInfo? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val msgHead: Cmd0x5fb.BusiReqHead? = null,
        @SerialId(2) val msgLbsInfo: AppointDefine.LBSInfo? = null,
        @SerialId(3) val reqInfo: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class BusiRespHead(
        @SerialId(1) val int32Version: Int = 1,
        @SerialId(2) val int32Seq: Int = 0,
        @SerialId(3) val int32ReplyCode: Int = 0,
        @SerialId(4) val result: String = ""
    ) : ProtoBuf

    @Serializable
    class UserProfile(
        @SerialId(1) val int64Id: Long = 0L,
        @SerialId(2) val int32IdType: Int = 0,
        @SerialId(3) val url: String = "",
        @SerialId(4) val int32PicType: Int = 0,
        @SerialId(5) val int32SubPicType: Int = 0,
        @SerialId(6) val title: String = "",
        @SerialId(7) val content: String = "",
        @SerialId(8) val content2: String = "",
        @SerialId(9) val picUrl: String = ""
    ) : ProtoBuf

    @Serializable
    class BusiReqHead(
        @SerialId(1) val int32Version: Int = 1,
        @SerialId(2) val int32Seq: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgHead: Cmd0x5fb.BusiRespHead? = null,
        @SerialId(2) val msgUserList: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf
}

@Serializable
class Oidb0xb61 : ProtoBuf {
    @Serializable
    class GetAppinfoReq(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val appType: Int = 0,
        @SerialId(3) val platform: Int = 0
    ) : ProtoBuf

    @Serializable
    class GetPkgUrlReq(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val appType: Int = 0,
        @SerialId(3) val appVersion: Int = 0,
        @SerialId(4) val platform: Int = 0,
        @SerialId(5) val sysVersion: String = "",
        @SerialId(6) val qqVersion: String = ""
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(2) val nextReqDuration: Int = 0,
        @SerialId(10) val getAppinfoRsp: Oidb0xb61.GetAppinfoRsp? = null,
        @SerialId(11) val getMqqappUrlRsp: Oidb0xb61.GetPkgUrlRsp? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(10) val getAppinfoReq: Oidb0xb61.GetAppinfoReq? = null,
        @SerialId(11) val getMqqappUrlReq: Oidb0xb61.GetPkgUrlReq? = null
    ) : ProtoBuf

    @Serializable
    class GetAppinfoRsp(
        @SerialId(1) val appinfo: Qqconnect.Appinfo? = null
    ) : ProtoBuf

    @Serializable
    class GetPkgUrlRsp(
        @SerialId(1) val appVersion: Int = 0,
        @SerialId(2) val pkgUrl: String = ""
    ) : ProtoBuf
}

@Serializable
class Oidb0xb60 : ProtoBuf {
    @Serializable
    class GetPrivilegeReq(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val appType: Int = 3
    ) : ProtoBuf

    @Serializable
    class CheckUrlReq(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val appType: Int = 0,
        @SerialId(3) val url: String = ""
    ) : ProtoBuf

    @Serializable
    class ClientInfo(
        @SerialId(1) val platform: Int = 0,
        @SerialId(2) val sdkVersion: String = "",
        @SerialId(3) val androidPackageName: String = "",
        @SerialId(4) val androidSignature: String = "",
        @SerialId(5) val iosBundleId: String = "",
        @SerialId(6) val pcSign: String = ""
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(10) val getPrivilegeRsp: Oidb0xb60.GetPrivilegeRsp? = null,
        @SerialId(11) val checkUrlRsp: Oidb0xb60.CheckUrlRsp? = null
    ) : ProtoBuf

    @Serializable
    class CheckUrlRsp(
        @SerialId(1) val isAuthed: Boolean = false,
        @SerialId(2) val nextReqDuration: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val clientInfo: Oidb0xb60.ClientInfo? = null,
        @SerialId(10) val getPrivilegeReq: Oidb0xb60.GetPrivilegeReq? = null,
        @SerialId(11) val checkUrlReq: Oidb0xb60.CheckUrlReq? = null
    ) : ProtoBuf

    @Serializable
    class GetPrivilegeRsp(
        @SerialId(1) val apiGroups: List<Int>? = null,
        @SerialId(2) val nextReqDuration: Int = 0,
        @SerialId(3) val apiNames: List<String> = listOf()
    ) : ProtoBuf
}

@Serializable
class Cmd0x5fc : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val lastEventId: Long = 0L,
        @SerialId(2) val readEventId: Long = 0L,
        @SerialId(3) val fetchCount: Int = 0,
        @SerialId(4) val lastNearbyEventId: Long = 0L,
        @SerialId(5) val readNearbyEventId: Long = 0L,
        @SerialId(6) val fetchNearbyEventCount: Int = 0,
        @SerialId(7) val lastFeedEventId: Long = 0L,
        @SerialId(8) val readFeedEventId: Long = 0L,
        @SerialId(9) val fetchFeedEventCount: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgEventList: List<AppointDefine.DateEvent>? = null,
        @SerialId(2) val actAppointIds: List<AppointDefine.AppointID>? = null,
        @SerialId(3) val maxEventId: Long = 0L,
        @SerialId(4) val errorTips: String = "",
        @SerialId(5) val msgNearbyEventList: List<AppointDefine.NearbyEvent>? = null,
        @SerialId(6) val msgFeedEventList: List<AppointDefine.FeedEvent>? = null,
        @SerialId(7) val maxFreshEventId: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Oidb0xc33 : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val result: Int = 0,
        @SerialId(2) val nextGap: Int = 0,
        @SerialId(3) val newUser: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody : ProtoBuf
}

@Serializable
class Oidb0xc0b : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val isOpenCoinEntry: Int = 0,
        @SerialId(2) val canGetCoinCount: Int = 0,
        @SerialId(3) val coinIconUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val lastCompletedTaskStamp: Long = 0L,
        @SerialId(6) val cmsWording: List<Oidb0xc0b.KanDianCMSActivityInfo>? = null,
        @SerialId(7) val lastCmsActivityStamp: Long = 0L,
        @SerialId(8) val msgKandianCoinRemind: Oidb0xc0b.KanDianCoinRemind? = null,
        @SerialId(9) val msgKandianTaskRemind: Oidb0xc0b.KanDianTaskRemind? = null
    ) : ProtoBuf

    @Serializable
    class KanDianCoinRemind(
        @SerialId(1) val wording: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class KanDianTaskRemind(
        @SerialId(1) val wording: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val jumpUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val taskType: Int = 0
    ) : ProtoBuf

    @Serializable
    class KanDianCMSActivityInfo(
        @SerialId(1) val activityId: Long = 0L,
        @SerialId(2) val wording: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val pictureUrl: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Cmd0xc85 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(101) val fromUin: Long = 0L,
        @SerialId(102) val toUin: Long = 0L,
        @SerialId(103) val op: Int = 0,
        @SerialId(104) val intervalDays: Int = 0
    ) : ProtoBuf

    @Serializable
    class InteractionDetailInfo(
        @SerialId(101) val continuousRecordDays: Int = 0,
        @SerialId(102) val sendDayTime: Int = 0,
        @SerialId(103) val recvDayTime: Int = 0,
        @SerialId(104) val sendRecord: String = "",
        @SerialId(105) val recvRecord: String = ""
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(101) val result: Int = 0,
        @SerialId(102) val recentInteractionTime: Int = 0,
        @SerialId(103) val interactionDetailInfo: Cmd0xc85.InteractionDetailInfo? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x7ce : ProtoBuf {
    @Serializable
    class AppintDetail(
        @SerialId(1) val msgPublisherInfo: AppointDefine.PublisherInfo? = null,
        @SerialId(2) val msgAppointsInfo: AppointDefine.AppointInfo? = null,
        @SerialId(3) val score: Int = 0,
        @SerialId(4) val joinOver: Int = 0,
        @SerialId(5) val joinNext: Int = 0,
        @SerialId(6) val msgStrangerInfo: List<AppointDefine.StrangerInfo>? = null,
        @SerialId(7) val viewOver: Int = 0,
        @SerialId(8) val viewNext: Int = 0,
        @SerialId(9) val msgVistorInfo: List<AppointDefine.StrangerInfo>? = null,
        @SerialId(10) val meJoin: Int = 0,
        @SerialId(12) val canProfile: Int = 0,
        @SerialId(13) val profileErrmsg: String = "",
        @SerialId(14) val canAio: Int = 0,
        @SerialId(15) val aioErrmsg: String = "",
        @SerialId(16) val sigC2C: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(17) val uin: Long = 0L,
        @SerialId(18) val limited: Int = 0,
        @SerialId(19) val msgCommentList: List<AppointDefine.DateComment>? = null,
        @SerialId(20) val commentOver: Int = 0,
        @SerialId(23) val meInvited: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgAppointsInfo: List<Cmd0x7ce.AppintDetail>? = null,
        @SerialId(2) val secureFlag: Int = 0,
        @SerialId(3) val secureTips: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val appointIds: List<AppointDefine.AppointID>? = null,
        @SerialId(2) val joinStart: Int = 0,
        @SerialId(3) val joinWant: Int = 0,
        @SerialId(4) val viewStart: Int = 0,
        @SerialId(5) val viewWant: Int = 0,
        @SerialId(6) val msgLbsInfo: AppointDefine.LBSInfo? = null,
        @SerialId(7) val uint64Uins: List<Long>? = null,
        @SerialId(8) val viewCommentCount: Int = 0,
        @SerialId(100) val requestUin: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Cmd0x7db : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(2) val msgAppointInfo: AppointDefine.AppointInfo? = null,
        @SerialId(3) val sigC2C: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val appointAction: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val msgAppointId: AppointDefine.AppointID? = null,
        @SerialId(2) val appointAction: Int = 0,
        @SerialId(3) val overwrite: Int = 0,
        @SerialId(4) val msgAppointIds: List<AppointDefine.AppointID>? = null
    ) : ProtoBuf
}

@Serializable
class Oidb0xc6c : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val msgGroupInfo: List<Oidb0xc6c.GroupInfo>? = null
    ) : ProtoBuf

    @Serializable
    class GroupInfo(
        @SerialId(1) val groupUin: Long = 0L,
        @SerialId(2) val groupCode: Long = 0L
    ) : ProtoBuf

    @Serializable
    class RspBody : ProtoBuf
}

@Serializable
class Oidb0xc05 : ProtoBuf {
    @Serializable
    class GetAuthAppListReq(
        @SerialId(1) val start: Int = 0,
        @SerialId(2) val limit: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(10) val getCreateAppListRsp: Oidb0xc05.GetCreateAppListRsp? = null,
        @SerialId(11) val getAuthAppListRsp: Oidb0xc05.GetAuthAppListRsp? = null
    ) : ProtoBuf

    @Serializable
    class GetCreateAppListRsp(
        @SerialId(1) val totalCount: Int = 0,
        @SerialId(2) val appinfos: List<Qqconnect.Appinfo>? = null
    ) : ProtoBuf

    @Serializable
    class GetAuthAppListRsp(
        @SerialId(1) val totalCount: Int = 0,
        @SerialId(2) val appinfos: List<Qqconnect.Appinfo>? = null,
        @SerialId(3) val curIndex: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(10) val getCreateAppListReq: Oidb0xc05.GetCreateAppListReq? = null,
        @SerialId(11) val getAuthAppListReq: Oidb0xc05.GetAuthAppListReq? = null
    ) : ProtoBuf

    @Serializable
    class GetCreateAppListReq(
        @SerialId(1) val start: Int = 0,
        @SerialId(2) val limit: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x7da : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val msgAppointIds: List<AppointDefine.AppointID>? = null,
        @SerialId(2) val appointOperation: Int = 0,
        @SerialId(3) val operationReason: Int = 0,
        @SerialId(4) val overwrite: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(2) val msgAppointInfo: List<AppointDefine.AppointInfo>? = null,
        @SerialId(3) val operationReason: Int = 0
    ) : ProtoBuf
}

@Serializable
class Qqconnect : ProtoBuf {
    @Serializable
    class MobileAppInfo(
        @SerialId(11) val androidAppInfo: List<Qqconnect.AndroidAppInfo>? = null,
        @SerialId(12) val iosAppInfo: List<Qqconnect.IOSAppInfo>? = null
    ) : ProtoBuf

    @Serializable
    class TemplateMsgConfig(
        @SerialId(1) val serviceMsgUin: Long = 0L,
        @SerialId(2) val publicMsgUin: Long = 0L,
        @SerialId(3) val campMsgUin: Long = 0L
    ) : ProtoBuf

    @Serializable
    class Appinfo(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val appType: Int = 0,
        @SerialId(3) val platform: Int = 0,
        @SerialId(4) val appName: String = "",
        @SerialId(5) val appKey: String = "",
        @SerialId(6) val appState: Int = 0,
        @SerialId(7) val iphoneUrlScheme: String = "",
        @SerialId(8) val androidPackName: String = "",
        @SerialId(9) val iconUrl: String = "",
        @SerialId(10) val sourceUrl: String = "",
        @SerialId(11) val iconSmallUrl: String = "",
        @SerialId(12) val iconMiddleUrl: String = "",
        @SerialId(13) val tencentDocsAppinfo: Qqconnect.TencentDocsAppinfo? = null,
        @SerialId(21) val developerUin: Long = 0L,
        @SerialId(22) val appClass: Int = 0,
        @SerialId(23) val appSubclass: Int = 0,
        @SerialId(24) val remark: String = "",
        @SerialId(25) val iconMiniUrl: String = "",
        @SerialId(26) val authTime: Long = 0L,
        @SerialId(27) val appUrl: String = "",
        @SerialId(28) val universalLink: String = "",
        @SerialId(29) val qqconnectFeature: Int = 0,
        @SerialId(30) val isHatchery: Int = 0,
        @SerialId(31) val testUinList: List<Long>? = null,
        @SerialId(100) val templateMsgConfig: Qqconnect.TemplateMsgConfig? = null,
        @SerialId(101) val miniAppInfo: Qqconnect.MiniAppInfo? = null,
        @SerialId(102) val webAppInfo: Qqconnect.WebAppInfo? = null,
        @SerialId(103) val mobileAppInfo: Qqconnect.MobileAppInfo? = null
    ) : ProtoBuf

    @Serializable
    class ConnectClientInfo(
        @SerialId(1) val platform: Int = 0,
        @SerialId(2) val sdkVersion: String = "",
        @SerialId(3) val systemName: String = "",
        @SerialId(4) val systemVersion: String = "",
        @SerialId(21) val androidPackageName: String = "",
        @SerialId(22) val androidSignature: String = "",
        @SerialId(31) val iosBundleId: String = "",
        @SerialId(32) val iosDeviceId: String = "",
        @SerialId(33) val iosAppToken: String = "",
        @SerialId(41) val pcSign: String = ""
    ) : ProtoBuf

    @Serializable
    class TencentDocsAppinfo(
        @SerialId(1) val openTypes: String = "",
        @SerialId(2) val opts: String = "",
        @SerialId(3) val ejs: String = "",
        @SerialId(4) val callbackUrlTest: String = "",
        @SerialId(5) val callbackUrl: String = "",
        @SerialId(6) val domain: String = "",
        @SerialId(7) val userinfoCallback: String = "",
        @SerialId(8) val userinfoCallbackTest: String = ""
    ) : ProtoBuf

    @Serializable
    class WebAppInfo(
        @SerialId(1) val websiteUrl: String = "",
        @SerialId(2) val provider: String = "",
        @SerialId(3) val icp: String = "",
        @SerialId(4) val callbackUrl: String = ""
    ) : ProtoBuf

    @Serializable
    class IOSAppInfo(
        @SerialId(1) val bundleId: String = "",
        @SerialId(2) val urlScheme: String = "",
        @SerialId(3) val storeId: String = ""
    ) : ProtoBuf

    @Serializable
    class MsgUinInfo(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val msgType: Int = 0,
        @SerialId(3) val appid: Int = 0,
        @SerialId(4) val appType: Int = 0,
        @SerialId(5) val ctime: Int = 0,
        @SerialId(6) val mtime: Int = 0,
        @SerialId(7) val mpType: Int = 0,
        @SerialId(100) val nick: String = "",
        @SerialId(101) val faceUrl: String = ""
    ) : ProtoBuf

    @Serializable
    class MiniAppInfo(
        @SerialId(1) val superUin: Long = 0L,
        @SerialId(11) val ownerType: Int = 0,
        @SerialId(12) val ownerName: String = "",
        @SerialId(13) val ownerIdCardType: Int = 0,
        @SerialId(14) val ownerIdCard: String = "",
        @SerialId(15) val ownerStatus: Int = 0
    ) : ProtoBuf

    @Serializable
    class AndroidAppInfo(
        @SerialId(1) val packName: String = "",
        @SerialId(2) val packSign: String = "",
        @SerialId(3) val apkDownUrl: String = ""
    ) : ProtoBuf
}

@Serializable
class Sync : ProtoBuf {
    @Serializable
    class SyncAppointmentReq(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val msgAppointment: AppointDefine.AppointContent? = null,
        @SerialId(3) val msgGpsInfo: AppointDefine.GPS? = null
    ) : ProtoBuf

    @Serializable
    class SyncAppointmentRsp(
        @SerialId(1) val result: Int = 0
    ) : ProtoBuf
}

@Serializable
class Oidb0xc26 : ProtoBuf {
    @Serializable
    class RgoupLabel(
        @SerialId(1) val name: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val enumType: Int /* enum */ = 1,
        @SerialId(3) val textColor: Oidb0xc26.RgroupColor? = null,
        @SerialId(4) val edgingColor: Oidb0xc26.RgroupColor? = null,
        @SerialId(5) val labelAttr: Int = 0,
        @SerialId(6) val labelType: Int = 0
    ) : ProtoBuf

    @Serializable
    class AddFriendSource(
        @SerialId(1) val source: Int = 0,
        @SerialId(2) val subSource: Int = 0
    ) : ProtoBuf

    @Serializable
    class Label(
        @SerialId(1) val name: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val textColor: Oidb0xc26.Color? = null,
        @SerialId(3) val edgingColor: Oidb0xc26.Color? = null,
        @SerialId(4) val labelType: Int = 0
    ) : ProtoBuf

    @Serializable
    class EntryDelay(
        @SerialId(1) val emEntry: Int /* enum */ = 1,
        @SerialId(2) val delay: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgPersons: List<Oidb0xc26.MayKnowPerson>? = null,
        @SerialId(2) val entryInuse: List<Int> = listOf(),
        @SerialId(3) val entryClose: List<Int> = listOf(),
        @SerialId(4) val nextGap: Int = 0,
        @SerialId(5) val timestamp: Int = 0,
        @SerialId(6) val msgUp: Int = 0,
        @SerialId(7) val entryDelays: List<Oidb0xc26.EntryDelay>? = null,
        @SerialId(8) val listSwitch: Int = 0,
        @SerialId(9) val addPageListSwitch: Int = 0,
        @SerialId(10) val emRspDataType: Int /* enum */ = 1,
        @SerialId(11) val msgRgroupItems: List<Oidb0xc26.RecommendInfo>? = null,
        @SerialId(12) val boolIsNewuser: Boolean = false,
        @SerialId(13) val msgTables: List<Oidb0xc26.TabInfo>? = null,
        @SerialId(14) val cookies: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class TabInfo(
        @SerialId(1) val tabId: Int = 0,
        @SerialId(2) val recommendCount: Int = 0,
        @SerialId(3) val tableName: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val iconUrlSelect: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val iconUrlUnselect: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(6) val backgroundColorSelect: Oidb0xc26.Color? = null,
        @SerialId(7) val backgroundColorUnselect: Oidb0xc26.Color? = null
    ) : ProtoBuf

    @Serializable
    class MayKnowPerson(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val msgIosSource: Oidb0xc26.AddFriendSource? = null,
        @SerialId(3) val msgAndroidSource: Oidb0xc26.AddFriendSource? = null,
        @SerialId(4) val reason: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val additive: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(6) val nick: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(7) val remark: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(8) val country: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(9) val province: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(10) val city: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(11) val age: Int = 0,
        @SerialId(12) val catelogue: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(13) val alghrithm: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(14) val richbuffer: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(15) val qzone: Int = 0,
        @SerialId(16) val gender: Int = 0,
        @SerialId(17) val mobileName: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(18) val token: String = "",
        @SerialId(19) val onlineState: Int = 0,
        @SerialId(20) val msgLabels: List<Oidb0xc26.Label>? = null,
        @SerialId(21) val sourceid: Int = 0
    ) : ProtoBuf

    @Serializable
    class RecommendInfo(
        @SerialId(1) val woring: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val msgGroups: List<Oidb0xc26.RgroupInfo>? = null
    ) : ProtoBuf

    @Serializable
    class RgroupInfo(
        @SerialId(1) val groupCode: Long = 0L,
        @SerialId(2) val ownerUin: Long = 0L,
        @SerialId(3) val groupName: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val groupMemo: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val memberNum: Int = 0,
        @SerialId(6) val groupLabel: List<Oidb0xc26.RgoupLabel>? = null,
        @SerialId(7) val groupFlagExt: Int = 0,
        @SerialId(8) val groupFlag: Int = 0,
        @SerialId(9) val source: Int /* enum */ = 1,
        @SerialId(10) val tagWording: Oidb0xc26.RgoupLabel? = null,
        @SerialId(11) val algorithm: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(12) val joinGroupAuth: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(13) val activity: Int = 0,
        @SerialId(14) val memberMaxNum: Int = 0,
        @SerialId(15) val int32UinPrivilege: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val filterUins: List<Long>? = null,
        @SerialId(2) val phoneBook: Int = 0,
        @SerialId(3) val expectedUins: List<Long>? = null,
        @SerialId(4) val emEntry: Int /* enum */ = 1,
        @SerialId(5) val fetchRgroup: Int = 0,
        @SerialId(6) val tabId: Int = 0,
        @SerialId(7) val want: Int = 80,
        @SerialId(8) val cookies: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class RgroupColor(
        @SerialId(1) val r: Int = 0,
        @SerialId(2) val g: Int = 0,
        @SerialId(3) val b: Int = 0
    ) : ProtoBuf

    @Serializable
    class Color(
        @SerialId(1) val r: Int = 0,
        @SerialId(2) val g: Int = 0,
        @SerialId(3) val b: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0xac6 : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val results: List<Cmd0xac6.OperateResult>? = null,
        @SerialId(4) val metalCount: Int = 0,
        @SerialId(5) val metalTotal: Int = 0,
        @SerialId(9) val int32NewCount: Int = 0,
        @SerialId(10) val int32UpgradeCount: Int = 0,
        @SerialId(11) val promptParams: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val medals: List<Cmd0xac6.MedalReport>? = null,
        @SerialId(2) val clean: Int = 0
    ) : ProtoBuf

    @Serializable
    class MedalReport(
        @SerialId(1) val id: Int = 0,
        @SerialId(2) val level: Int = 0
    ) : ProtoBuf

    @Serializable
    class OperateResult(
        @SerialId(1) val id: Int = 0,
        @SerialId(2) val int32Result: Int = 0,
        @SerialId(3) val errmsg: String = ""
    ) : ProtoBuf
}

@Serializable
class Oidb0xd32 : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val openid: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val xmitinfo: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class XmitInfo(
        @SerialId(1) val signature: String = "",
        @SerialId(2) val appid: String = "",
        @SerialId(3) val groupid: String = "",
        @SerialId(4) val nonce: String = "",
        @SerialId(5) val timestamp: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x7cf : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val stamp: Int = 0,
        @SerialId(2) val start: Int = 0,
        @SerialId(3) val want: Int = 0,
        @SerialId(4) val reqValidOnly: Int = 0,
        @SerialId(5) val msgAppointIds: List<AppointDefine.AppointID>? = null,
        @SerialId(6) val appointOperation: Int = 0,
        @SerialId(100) val requestUin: Long = 0L
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val stamp: Int = 0,
        @SerialId(2) val over: Int = 0,
        @SerialId(3) val next: Int = 0,
        @SerialId(4) val msgAppointsInfo: List<AppointDefine.AppointInfo>? = null,
        @SerialId(5) val unreadCount: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0xac7 : ProtoBuf {
    @Serializable
    class DeviceInfo(
        @SerialId(1) val din: Long = 0L,
        @SerialId(2) val name: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val extd: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val cmd: Int = 0,
        @SerialId(2) val din: Long = 0L,
        @SerialId(3) val extd: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val msgBinderSig: Cmd0xac7.BinderSig? = null
    ) : ProtoBuf

    @Serializable
    class ReceiveMessageDevices(
        @SerialId(1) val devices: List<Cmd0xac7.DeviceInfo>? = null
    ) : ProtoBuf

    @Serializable
    class BinderSig(
        @SerialId(1) val type: Int = 0,
        @SerialId(2) val uin: Long = 0L,
        @SerialId(3) val sig: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf
}

@Serializable
class Cmd0x5fa : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val msgStrangerInfo: List<AppointDefine.StrangerInfo>? = null,
        @SerialId(2) val reachStart: Int = 0,
        @SerialId(3) val reachEnd: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val appointIds: AppointDefine.AppointID? = null,
        @SerialId(2) val referIdx: Int = 0,
        @SerialId(3) val getReferRec: Int = 0,
        @SerialId(4) val reqNextCount: Int = 0,
        @SerialId(5) val reqPrevCount: Int = 0
    ) : ProtoBuf
}

@Serializable
class FavoriteCKVData : ProtoBuf {
    @Serializable
    class PicInfo(
        @SerialId(1) val uri: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val md5: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val sha1: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val name: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val note: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(6) val width: Int = 0,
        @SerialId(7) val height: Int = 0,
        @SerialId(8) val size: Int = 0,
        @SerialId(9) val type: Int = 0,
        @SerialId(10) val msgOwner: FavoriteCKVData.Author? = null,
        @SerialId(11) val picId: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class KandianFavoriteItem(
        @SerialId(1) val msgFavoriteExtInfo: FavoriteCKVData.KandianFavoriteBizData? = null,
        @SerialId(2) val bytesCid: List<ByteArray>? = null,
        @SerialId(3) val type: Int = 0,
        @SerialId(4) val status: Int = 0,
        @SerialId(5) val msgAuthor: FavoriteCKVData.Author? = null,
        @SerialId(6) val createTime: Long = 0L,
        @SerialId(7) val favoriteTime: Long = 0L,
        @SerialId(8) val modifyTime: Long = 0L,
        @SerialId(9) val dataSyncTime: Long = 0L,
        @SerialId(10) val msgFavoriteSummary: FavoriteCKVData.FavoriteSummary? = null
    ) : ProtoBuf

    @Serializable
    class LinkSummary(
        @SerialId(1) val uri: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val title: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val publisher: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val brief: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val msgPicInfo: List<FavoriteCKVData.PicInfo>? = null,
        @SerialId(6) val type: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(7) val resourceUri: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class UserFavoriteList(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val modifyTs: Long = 0L,
        @SerialId(100) val msgFavoriteItems: List<FavoriteCKVData.FavoriteItem>? = null
    ) : ProtoBuf

    @Serializable
    class FavoriteSummary(
        @SerialId(2) val msgLinkSummary: FavoriteCKVData.LinkSummary? = null
    ) : ProtoBuf

    @Serializable
    class FavoriteItem(
        @SerialId(1) val favoriteSource: Int = 0,
        @SerialId(100) val msgKandianFavoriteItem: FavoriteCKVData.KandianFavoriteItem? = null
    ) : ProtoBuf

    @Serializable
    class Author(
        @SerialId(1) val type: Int = 0,
        @SerialId(2) val numId: Long = 0L,
        @SerialId(3) val strId: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val groupId: Long = 0L,
        @SerialId(5) val groupName: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class KandianFavoriteBizData(
        @SerialId(1) val rowkey: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val type: Int = 0,
        @SerialId(3) val videoDuration: Int = 0,
        @SerialId(4) val picNum: Int = 0,
        @SerialId(5) val accountId: Long = 0L,
        @SerialId(6) val accountName: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(7) val videoType: Int = 0,
        @SerialId(8) val feedsId: Long = 0L,
        @SerialId(9) val feedsType: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x5ff : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val errorTips: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val msgAppointId: AppointDefine.AppointID? = null,
        @SerialId(2) val commentId: String = ""
    ) : ProtoBuf
}

@Serializable
class Oidb0xccd : ProtoBuf {
    @Serializable
    class Result(
        @SerialId(1) val appid: Int = 0,
        @SerialId(2) val errcode: Int = 0,
        @SerialId(3) val errmsg: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val int64Uin: Long = 0L,
        @SerialId(2) val appids: List<Int>? = null,
        @SerialId(3) val platform: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val errcode: Int = 0,
        @SerialId(2) val results: List<Oidb0xccd.Result>? = null
    ) : ProtoBuf
}

@Serializable
class Oidb0xc36 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val uint64Uins: List<Long>? = null
    ) : ProtoBuf

    @Serializable
    class RspBody : ProtoBuf
}

@Serializable
class Oidb0x87c : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val country: String = "",
        @SerialId(2) val telephone: String = "",
        @SerialId(3) val smsCode: String = "",
        @SerialId(4) val guid: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val enumButype: Int /* enum */ = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val country: String = "",
        @SerialId(2) val telephone: String = "",
        @SerialId(3) val keyType: Int = 0,
        @SerialId(4) val key: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val guid: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf
}

@Serializable
class Cmd0xbf2 : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val phoneAddrBook: List<Cmd0xbf2.PhoneAddrBook>? = null,
        @SerialId(2) val end: Int = 0,
        @SerialId(3) val nextIndex: Long = 0
    ) : ProtoBuf

    @Serializable
    class PhoneAddrBook(
        @SerialId(1) val phone: String = "",
        @SerialId(2) val nick: String = "",
        @SerialId(3) val headUrl: String = "",
        @SerialId(4) val longNick: String = ""
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val startIndex: Long = 0L,
        @SerialId(3) val num: Long = 0L
    ) : ProtoBuf
}

@Serializable
class Cmd0x6cd : ProtoBuf {
    @Serializable
    class RedpointInfo(
        @SerialId(1) val taskid: Int = 0,
        @SerialId(2) val curSeq: Long = 0L,
        @SerialId(3) val pullSeq: Long = 0L,
        @SerialId(4) val readSeq: Long = 0L,
        @SerialId(5) val pullTimes: Int = 0,
        @SerialId(6) val lastPullTime: Int = 0,
        @SerialId(7) val int32RemainedTime: Int = 0,
        @SerialId(8) val lastRecvTime: Int = 0,
        @SerialId(9) val fromId: Long = 0L,
        @SerialId(10) val enumRedpointType: Int /* enum */ = 1,
        @SerialId(11) val msgRedpointExtraInfo: Cmd0x6cd.RepointExtraInfo? = null,
        @SerialId(12) val configVersion: String = "",
        @SerialId(13) val doActivity: Int = 0,
        @SerialId(14) val msgUnreadMsg: List<Cmd0x6cd.MessageRec>? = null
    ) : ProtoBuf

    @Serializable
    class PullRedpointReq(
        @SerialId(1) val taskid: Int = 0,
        @SerialId(2) val lastPullSeq: Long = 0L
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val msgRedpoint: List<Cmd0x6cd.RedpointInfo>? = null,
        @SerialId(2) val unfinishedRedpoint: List<Cmd0x6cd.PullRedpointReq>? = null
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val lastPullRedpoint: List<Cmd0x6cd.PullRedpointReq>? = null,
        @SerialId(2) val unfinishedRedpoint: List<Cmd0x6cd.PullRedpointReq>? = null,
        @SerialId(3) val msgPullSingleTask: Cmd0x6cd.PullRedpointReq? = null,
        @SerialId(4) val retMsgRec: Int = 0
    ) : ProtoBuf

    @Serializable
    class MessageRec(
        @SerialId(1) val seq: Long = 0L,
        @SerialId(2) val time: Int = 0,
        @SerialId(3) val content: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class RepointExtraInfo(
        @SerialId(1) val count: Int = 0,
        @SerialId(2) val iconUrl: String = "",
        @SerialId(3) val tips: String = "",
        @SerialId(4) val data: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf
}

@Serializable
class Oidb0xd55 : ProtoBuf {
    @Serializable
    class CheckUserRsp(
        @SerialId(1) val openidUin: Long = 0L
    ) : ProtoBuf

    @Serializable
    class CheckMiniAppRsp : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val appid: Long = 0L,
        @SerialId(2) val appType: Int = 0,
        @SerialId(3) val srcId: Int = 0,
        @SerialId(4) val rawUrl: String = "",
        @SerialId(11) val checkAppSignReq: Oidb0xd55.CheckAppSignReq? = null,
        @SerialId(12) val checkUserReq: Oidb0xd55.CheckUserReq? = null,
        @SerialId(13) val checkMiniAppReq: Oidb0xd55.CheckMiniAppReq? = null
    ) : ProtoBuf

    @Serializable
    class CheckAppSignReq(
        @SerialId(1) val clientInfo: Qqconnect.ConnectClientInfo? = null
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val wording: String = "",
        @SerialId(11) val checkAppSignRsp: Oidb0xd55.CheckAppSignRsp? = null,
        @SerialId(12) val checkUserRsp: Oidb0xd55.CheckUserRsp? = null,
        @SerialId(13) val checkMiniAppRsp: Oidb0xd55.CheckMiniAppRsp? = null
    ) : ProtoBuf

    @Serializable
    class CheckUserReq(
        @SerialId(1) val openid: String = "",
        @SerialId(2) val needCheckSameUser: Int = 0
    ) : ProtoBuf

    @Serializable
    class CheckMiniAppReq(
        @SerialId(1) val miniAppAppid: Long = 0L,
        @SerialId(2) val needCheckBind: Int = 0
    ) : ProtoBuf

    @Serializable
    class CheckAppSignRsp(
        @SerialId(1) val iosAppToken: String = "",
        @SerialId(2) val iosUniversalLink: String = "",
        @SerialId(11) val optimizeSwitch: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0x8b4 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val gc: Long = 0L,
        @SerialId(2) val guin: Long = 0L,
        @SerialId(3) val flag: Int = 0,
        @SerialId(21) val dstUin: Long = 0L,
        @SerialId(22) val start: Int = 0,
        @SerialId(23) val cnt: Int = 0,
        @SerialId(24) val tag: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class GroupInfo(
        @SerialId(1) val gc: Long = 0L,
        @SerialId(2) val groupName: String = "",
        @SerialId(3) val faceUrl: String = "",
        @SerialId(4) val setDisplayTime: Int = 0,
        // @SerialId(5) val groupLabel: List<GroupLabel.Label>? = null,
        @SerialId(6) val textIntro: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(7) val richIntro: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class TagInfo(
        @SerialId(1) val dstUin: Long = 0L,
        @SerialId(2) val start: Int = 0,
        @SerialId(3) val cnt: Int = 0,
        @SerialId(4) val timestamp: Int = 0,
        @SerialId(5) val _0x7ddSeq: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val result: Int = 0,
        @SerialId(2) val flag: Int = 0,
        @SerialId(21) val tag: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(22) val groupInfo: List<Cmd0x8b4.GroupInfo>? = null,
        @SerialId(23) val textLabel: List<ByteArray>? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x682 : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val msgChatinfo: List<Cmd0x682.ChatInfo>? = null
    ) : ProtoBuf

    @Serializable
    class ChatInfo(
        @SerialId(1) val touin: Long = 0L,
        @SerialId(2) val chatflag: Int = 0,
        @SerialId(3) val goldflag: Int = 0,
        @SerialId(4) val totalexpcount: Int = 0,
        @SerialId(5) val curexpcount: Int = 0,
        @SerialId(6) val totalFlag: Int = 0,
        @SerialId(7) val curdayFlag: Int = 0,
        @SerialId(8) val expressTipsMsg: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(9) val expressMsg: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val uint64Touinlist: List<Long>? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x6f5 : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val qqVersion: String = "",
        @SerialId(2) val qqPlatform: Int = 0
    ) : ProtoBuf

    @Serializable
    class TaskInfo(
        @SerialId(1) val taskId: Int = 0,
        @SerialId(2) val appid: Int = 0,
        @SerialId(3) val passthroughLevel: Int = 0,
        @SerialId(4) val showLevel: Int = 0,
        @SerialId(5) val extra: Int = 0,
        @SerialId(6) val priority: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val configVersion: String = "",
        @SerialId(2) val taskInfo: List<Cmd0x6f5.TaskInfo>? = null
    ) : ProtoBuf
}

@Serializable
class Oidb0xb7e : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val topItem: List<Oidb0xb7e.DiandianTopConfig>? = null
    ) : ProtoBuf

    @Serializable
    class DiandianTopConfig(
        @SerialId(1) val jumpUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val title: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(3) val subTitle: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val subTitleColor: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val picUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(6) val type: Int = 0,
        @SerialId(7) val topicId: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody : ProtoBuf
}

@Serializable
class Oidb0xc2f : ProtoBuf {
    @Serializable
    class RspBody(
        @SerialId(1) val msgGetFollowUserRecommendListRsp: Oidb0xc2f.GetFollowUserRecommendListRsp? = null
    ) : ProtoBuf

    @Serializable
    class GetFollowUserRecommendListReq(
        @SerialId(1) val followedUin: Long = 0L
    ) : ProtoBuf

    @Serializable
    class RecommendAccountInfo(
        @SerialId(1) val uin: Long = 0L,
        @SerialId(2) val accountType: Int = 0,
        @SerialId(3) val nickName: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(4) val headImgUrl: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(5) val isVip: Int = 0,
        @SerialId(6) val isStar: Int = 0,
        @SerialId(7) val recommendReason: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class GetFollowUserRecommendListRsp(
        @SerialId(1) val msgRecommendList: List<Oidb0xc2f.RecommendAccountInfo>? = null,
        @SerialId(2) val jumpUrl: ByteArray = EMPTY_BYTE_ARRAY
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val msgGetFollowUserRecommendListReq: Oidb0xc2f.GetFollowUserRecommendListReq? = null
    ) : ProtoBuf
}

@Serializable
class Cmd0x7ca : ProtoBuf {
    @Serializable
    class ReqBody(
        @SerialId(1) val msgAppointId: AppointDefine.AppointID? = null,
        @SerialId(2) val tinyid: Long = 0L,
        @SerialId(3) val opType: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody(
        @SerialId(1) val sigC2C: ByteArray = EMPTY_BYTE_ARRAY,
        @SerialId(2) val peerUin: Long = 0L,
        @SerialId(3) val errorWording: String = "",
        @SerialId(4) val opType: Int = 0
    ) : ProtoBuf
}

@Serializable
class Cmd0xd40 : ProtoBuf {
    @Serializable
    class DeviceInfo(
        @SerialId(1) val os: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val dev: Cmd0xd40.DeviceInfo? = null,
        @SerialId(2) val src: Int = 0,
        @SerialId(3) val event: Int = 0,
        @SerialId(4) val redtype: Int = 0
    ) : ProtoBuf

    @Serializable
    class RspBody : ProtoBuf
}

@Serializable
class Cmd0x6ce : ProtoBuf {
    @Serializable
    class RspBody : ProtoBuf

    @Serializable
    class ReadRedpointReq(
        @SerialId(1) val taskid: Int = 0,
        @SerialId(2) val readSeq: Long = 0L,
        @SerialId(3) val appid: Int = 0
    ) : ProtoBuf

    @Serializable
    class ReqBody(
        @SerialId(1) val msgReadReq: List<Cmd0x6ce.ReadRedpointReq>? = null
    ) : ProtoBuf
}

