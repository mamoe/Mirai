/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.contact.active

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.active.*
import net.mamoe.mirai.data.GroupHonorType
import net.mamoe.mirai.internal.QQAndroidBot
import net.mamoe.mirai.internal.network.components.HttpClientProvider
import net.mamoe.mirai.internal.network.psKey
import net.mamoe.mirai.internal.network.sKey
import net.mamoe.mirai.utils.*

@Serializable
internal data class SetResult(
    @SerialName("ec") override val errorCode: Int = 0,
    @SerialName("em") override val errorMessage: String? = null,
    @SerialName("errcode") val errCode: Int?
) : CheckableResponseA(), JsonStruct

/**
 * 群等级信息
 */
@Serializable
internal data class GroupLevelInfo(
    @SerialName("ec") override val errorCode: Int = 0,
    @SerialName("em") override val errorMessage: String? = null,
    @SerialName("errcode") val errCode: Int?,
    @SerialName("levelflag") val levelFlag: Int,
    @SerialName("levelname") val levelName: Map<String, String>
) : CheckableResponseA(), JsonStruct

/**
 * 群统计信息
 */
@MiraiExperimentalApi
@Serializable
internal data class GroupActiveData(
    @SerialName("ec") override val errorCode: Int = 0,
    @SerialName("em") override val errorMessage: String? = null,
    @SerialName("errcode") val errCode: Int?,
    @SerialName("ginfo") val info: ActiveInfo,
    @SerialName("query") val query: Int? = 0,
    @SerialName("role") val role: Int? = 0
) : CheckableResponseA(), JsonStruct {

    @Serializable
    data class Situation(
        @SerialName("date") val date: String,
        @SerialName("num") val num: Int
    )

    @Serializable
    data class MostActive(
        @SerialName("name") val name: String,  // 名称 不完整
        @SerialName("sentences_num") val sentencesNum: Int,   // 发言数
        @SerialName("sta") val sta: Int = 0,
        @SerialName("uin") val uin: Long = 0
    )

    @Serializable
    data class ActiveInfo(
        @SerialName("g_act_num") val actNum: List<Situation>? = null,    //发言人数列表
        @SerialName("g_createtime") val createTime: Int? = 0,
        @SerialName("g_exit_num") val exitNum: List<Situation>? = null,  //退群人数列表
        @SerialName("g_join_num") val joinNum: List<Situation>? = null,
        @SerialName("g_mem_num") val memNum: List<Situation>? = null,   //人数变化
        @SerialName("g_most_act") val mostAct: List<MostActive>? = null,  //发言排行
        @SerialName("g_sentences") val sentences: List<Situation>? = null,
        @SerialName("gc") val gc: Int? = null,
        @SerialName("gn") val gn: String? = null,
        @SerialName("gowner") val owner: String? = null,
        @SerialName("isEnd") val isEnd: Int
    )
}


@Serializable
internal data class GroupHonorListData(

    @SerialName("gc")
    val gc: String?,

    @SerialName("type")
    val type: JsonElement,

    @SerialName("uin")
    val uin: String?,

    @SerialName("talkativeList")
    val talkativeList: List<Actor>? = null,

    @SerialName("currentTalkative")
    val currentTalkative: Current? = null,

    @SerialName("actorList")
    val actorList: List<Actor>? = null,

    @SerialName("legendList")
    val legendList: List<Actor>? = null,

    @SerialName("newbieList")
    val newbieList: List<Actor>? = null,

    @SerialName("strongnewbieList")
    val strongNewbieList: List<Actor>? = null,

    @SerialName("emotionList")
    val emotionList: List<Actor>? = null,

    @SerialName("richerList")
    val richerList: List<Actor>? = null,

    @SerialName("currentRicher")
    val currentRicher: Current? = null,

    @SerialName("redpacketHonnorList")
    val redpacketHonnorList: List<Actor>? = null,

    @SerialName("currentRedpacketHonnor")
    val currentRedpacketHonnor: Current? = null,

    @SerialName("levelname")
    val levelName: LevelName? = null,

    @SerialName("manageList")
    val manageList: List<Tag>? = null,

    @SerialName("exclusiveList")
    val exclusiveList: List<Tag>? = null,

    @SerialName("activeObj")
    val activeObj: Map<String, List<Tag>>? = null, // Key为活跃等级名, 如`冒泡`

    @SerialName("showActiveObj")
    val showActiveObj: Map<String, Boolean>? = null,

    @SerialName("myTitle")
    val myTitle: String?,

    @SerialName("myIndex")
    val myIndex: Int? = 0,

    @SerialName("myAvatar")
    val myAvatar: String?,

    @SerialName("hasServerError")
    val hasServerError: Boolean?,

    @SerialName("hwExcellentList")
    val hwExcellentList: List<Actor>? = null
) : JsonStruct {

    @Serializable
    data class Actor(
        @SerialName("uin")
        val uin: Long,

        @SerialName("avatar")
        val avatar: String,

        @SerialName("name")
        val name: String,

        @SerialName("desc")
        val desc: String,

        @SerialName("btnText")
        val btnText: String,

        @SerialName("text")
        val text: String,

        @SerialName("icon")
        val icon: Int? = null
    )

    @Serializable
    data class Current(
        @SerialName("uin")
        val uin: Long,

        @SerialName("day_count")
        val dayCount: Int,

        @SerialName("avatar")
        val avatar: String,

        @SerialName("avatar_size")
        val avatarSize: Int,

        @SerialName("nick")
        val nick: String
    )

    @Serializable
    data class LevelName(
        @SerialName("lvln1")
        val lv1: String? = null,

        @SerialName("lvln2")
        val lv2: String? = null,

        @SerialName("lvln3")
        val lv3: String? = null,

        @SerialName("lvln4")
        val lv4: String? = null,

        @SerialName("lvln5")
        val lv5: String? = null,

        @SerialName("lvln6")
        val lv6: String? = null
    )

    @Serializable
    data class Tag(
        @SerialName("uin")
        val uin: Long,

        @SerialName("avatar")
        val avatar: String,

        @SerialName("name")
        val name: String,

        @SerialName("btnText")
        val btnText: String,

        @SerialName("text")
        val text: String,

        @SerialName("tag")
        val tag: String,  // 头衔

        @SerialName("tagColor")
        val tagColor: String
    )
}

@Suppress("DEPRECATION_ERROR")
internal object GroupActiveProtocol {

    suspend fun QQAndroidBot.getRawGroupLevelInfo(
        groupCode: Long
    ): Either<DeserializationFailure, GroupLevelInfo> {
        return components[HttpClientProvider].getHttpClient().get {
            url("https://qinfo.clt.qq.com/cgi-bin/qun_info/get_group_level_info")
            parameter("gc", groupCode)
            parameter("bkn", client.wLoginSigInfo.bkn)
            parameter("src", "qinfo_v3")

            headers {
                // ktor bug
                append(
                    "cookie",
                    "uin=o${id}; skey=${sKey}"
                )
            }
        }.bodyAsText().loadSafelyAs(GroupLevelInfo.serializer())
    }

    suspend fun QQAndroidBot.setGroupLevelInfo(
        groupCode: Long,
        titles: Map<Int, String>
    ): Either<DeserializationFailure, SetResult> {
        return components[HttpClientProvider].getHttpClient().post {
            url("https://qinfo.clt.qq.com/cgi-bin/qun_info/set_group_level_info")
            setBody(FormDataContent(Parameters.build {
                titles.forEach { (index, name) ->
                    append("lvln$index", name)
                }
                append("gc", groupCode.toString())
                append("src", "qinfo_v3")
                append("bkn", client.wLoginSigInfo.bkn.toString())
            }))

            headers {
                // ktor bug
                append(
                    "cookie",
                    "uin=o${id}; skey=${sKey}"
                )
            }
        }.bodyAsText().loadSafelyAs(SetResult.serializer())
    }

    suspend fun QQAndroidBot.setGroupLevelInfo(
        groupCode: Long,
        show: Boolean
    ): Either<DeserializationFailure, SetResult> {
        return components[HttpClientProvider].getHttpClient().post {
            url("https://qinfo.clt.qq.com/cgi-bin/qun_info/set_group_setting")
            setBody(FormDataContent(Parameters.build {
                append("levelflag", if (show) "1" else "0")
                append("gc", groupCode.toString())
                append("src", "qinfo_v3")
                append("bkn", client.wLoginSigInfo.bkn.toString())
            }))

            headers {
                // ktor bug
                append(
                    "cookie",
                    "uin=o${id}; skey=${sKey}"
                )
            }
        }.bodyAsText().loadSafelyAs(SetResult.serializer())
    }

    suspend fun QQAndroidBot.getRawGroupActiveData(
        groupCode: Long,
        page: Int? = null
    ): Either<DeserializationFailure, GroupActiveData> {
        return components[HttpClientProvider].getHttpClient().get {
            url("https://qqweb.qq.com/c/activedata/get_mygroup_data")
            parameter("bkn", client.wLoginSigInfo.bkn)
            parameter("gc", groupCode)
            parameter("page", page)
            headers {
                // ktor bug
                append(
                    "cookie",
                    "uin=o${id}; skey=${sKey}; p_uin=o${id}; p_skey=${psKey(host)};"
                )
            }
        }.bodyAsText().loadSafelyAs(GroupActiveData.serializer())
    }

    suspend fun QQAndroidBot.getRawGroupHonorListData(
        groupId: Long,
        type: GroupHonorType
    ): Either<DeserializationFailure, GroupHonorListData> {
        val html = components[HttpClientProvider].getHttpClient().get {
            url("https://qun.qq.com/interactive/honorlist")
            parameter("gc", groupId)
            parameter(
                "type", when (type) {
                    GroupHonorType.BRONZE -> "bronze"
                    GroupHonorType.SILVER -> "silver"
                    GroupHonorType.GOLDEN -> "golden"
                    GroupHonorType.WHIRLWIND -> "whirlwind"
                    else -> type.value
                }
            )
            headers {
                // ktor bug
                append(
                    "cookie",
                    "uin=o${id};" +
                        " skey=${sKey};" +
                        " p_uin=o${id};" +
                        " p_skey=${psKey(host)}; "
                )
            }
        }.bodyAsText()
        val jsonText = html.substringAfter("window.__INITIAL_STATE__=").substringBefore("</script>")
        return jsonText.loadSafelyAs(GroupHonorListData.serializer())
    }

    @Suppress("INVISIBLE_MEMBER")
    fun GroupActiveData.MostActive.toActiveRecord(group: Group): ActiveRecord {
        return ActiveRecord(
            memberId = uin,
            memberName = name,
            periodDays = sentencesNum,
            messagesCount = sta,
            member = group.get(id = uin)
        )
    }

    @Suppress("INVISIBLE_MEMBER")
    fun GroupActiveData.ActiveInfo.toActiveChart(): ActiveChart {
        return ActiveChart(
            actives = actNum?.associate { it.date to it.num }.orEmpty(),
            sentences = sentences?.associate { it.date to it.num }.orEmpty(),
            members = memNum?.associate { it.date to it.num }.orEmpty(),
            join = joinNum?.associate { it.date to it.num }.orEmpty(),
            exit = exitNum?.associate { it.date to it.num }.orEmpty()
        )
    }

    @Suppress("INVISIBLE_MEMBER")
    fun GroupHonorListData.toActiveHonorList(type: GroupHonorType, group: Group): ActiveHonorList {
        return when (type) {
            GroupHonorType.TALKATIVE -> ActiveHonorList(
                type = type,
                current = currentTalkative?.let {
                    ActiveHonorCurrent(
                        memberName = it.nick,
                        memberId = it.uin,
                        avatar = it.avatar + it.avatarSize,
                        member = group.get(id = it.uin),
                        count = it.dayCount
                    )
                },
                records = talkativeList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.PERFORMER -> ActiveHonorList(
                type = type,
                current = null,
                records = actorList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.LEGEND -> ActiveHonorList(
                type = type,
                current = null,
                records = legendList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.STRONG_NEWBIE -> ActiveHonorList(
                type = type,
                current = null,
                records = strongNewbieList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.EMOTION -> ActiveHonorList(
                type = type,
                current = null,
                records = emotionList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.BRONZE, GroupHonorType.SILVER, GroupHonorType.GOLDEN, GroupHonorType.WHIRLWIND -> ActiveHonorList(
                type = type,
                current = null,
                records = actorList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.RICHER -> ActiveHonorList(
                type = type,
                current = currentRicher?.let {
                    ActiveHonorCurrent(
                        memberName = it.nick,
                        memberId = it.uin,
                        avatar = it.avatar + it.avatarSize,
                        member = group.get(id = it.uin),
                        count = it.dayCount
                    )
                },
                records = richerList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
            GroupHonorType.RED_PACKET -> ActiveHonorList(
                type = type,
                current = currentRedpacketHonnor?.let {
                    ActiveHonorCurrent(
                        memberName = it.nick,
                        memberId = it.uin,
                        avatar = it.avatar + it.avatarSize,
                        member = group.get(id = it.uin),
                        count = it.dayCount
                    )
                },
                records = redpacketHonnorList?.map {
                    ActiveHonorRecord(
                        memberName = it.name,
                        memberId = it.uin,
                        avatar = it.avatar,
                        member = group.get(id = it.uin),
                        description = it.desc
                    )
                }.orEmpty()
            )
        }
    }
}