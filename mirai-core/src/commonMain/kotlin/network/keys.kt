/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.internal.network

import kotlinx.io.core.ByteReadPacket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.internal.network.protocol.packet.PacketLogger
import net.mamoe.mirai.internal.utils.crypto.TEA
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.network.NoServerAvailableException
import net.mamoe.mirai.utils.*


internal class ReserveUinInfo(
    val imgType: ByteArray,
    val imgFormat: ByteArray,
    val imgUrl: ByteArray
) {
    override fun toString(): String {
        return "ReserveUinInfo(imgType=${imgType.toUHexString()}, imgFormat=${imgFormat.toUHexString()}, imgUrl=${imgUrl.toUHexString()})"
    }
}

internal class WFastLoginInfo(
    val outA1: ByteReadPacket,
    var adUrl: String = "",
    var iconUrl: String = "",
    var profileUrl: String = "",
    var userJson: String = ""
) {
    override fun toString(): String {
        return "WFastLoginInfo(outA1=$outA1, adUrl='$adUrl', iconUrl='$iconUrl', profileUrl='$profileUrl', userJson='$userJson')"
    }
}

@Serializable
internal class WLoginSimpleInfo(
    val uin: Long, // uin
    val imgType: ByteArray,
    val imgFormat: ByteArray,
    val imgUrl: ByteArray,
    val mainDisplayName: ByteArray
) {
    override fun toString(): String {
        return "WLoginSimpleInfo(uin=$uin, imgType=${imgType.toUHexString()}, imgFormat=${imgFormat.toUHexString()}, imgUrl=${imgUrl.toUHexString()}, mainDisplayName=${mainDisplayName.toUHexString()})"
    }
}

@Serializable
internal class LoginExtraData(
    val uin: Long,
    val ip: ByteArray,
    val time: Int,
    val version: Int
) {
    override fun toString(): String {
        return "LoginExtraData(uin=$uin, ip=${ip.toUHexString()}, time=$time, version=$version)"
    }
}

@Serializable
internal class WLoginSigInfo(
    val uin: Long,
    var encryptA1: ByteArray?, // sigInfo[0]
    /**
     * WARNING, please check [QQAndroidClient.tlv16a]
     */
    var noPicSig: ByteArray?, // sigInfo[1]

    val simpleInfo: WLoginSimpleInfo,

    var appPri: Long,
    var a2ExpiryTime: Long,
    var loginBitmap: Long,
    var tgt: ByteArray,
    var a2CreationTime: Long,
    var tgtKey: ByteArray,
    var userStSig: KeyWithCreationTime,
    /**
     * TransEmpPacket 加密使用
     */
    var userStKey: ByteArray,
    var userStWebSig: KeyWithExpiry,
    var userA5: KeyWithCreationTime,
    var userA8: KeyWithExpiry,
    var lsKey: KeyWithExpiry,
    var sKey: KeyWithExpiry,
    var userSig64: KeyWithCreationTime,
    var openId: ByteArray,
    var openKey: KeyWithCreationTime,
    var vKey: KeyWithExpiry,
    var accessToken: KeyWithCreationTime,
    var d2: KeyWithExpiry,
    var d2Key: ByteArray,
    var sid: KeyWithExpiry,
    var aqSig: KeyWithCreationTime,
    var psKeyMap: PSKeyMap,
    var pt4TokenMap: MutableMap<String, KeyWithExpiry> = mutableMapOf(), // = Pt4TokenMap  maybe compiler bug
    var superKey: ByteArray,
    var payToken: ByteArray,
    var pf: ByteArray,
    var pfKey: ByteArray,
    var da2: ByteArray,
    // val pt4Token: ByteArray,
    var wtSessionTicket: KeyWithCreationTime,
    var wtSessionTicketKey: ByteArray,
    var deviceToken: ByteArray,
    var encryptedDownloadSession: EncryptedDownloadSession? = null
) {

    //图片加密下载
    //是否加密从bigdatachannel处得知
    @Serializable
    internal class EncryptedDownloadSession(
        val appId: Long,//1600000226L
        val stKey: ByteArray,
        val stSig: ByteArray
    )

    override fun toString(): String {
        return "WLoginSigInfo(uin=$uin, encryptA1=${encryptA1?.toUHexString()}, noPicSig=${noPicSig?.toUHexString()}, simpleInfo=$simpleInfo, appPri=$appPri, a2ExpiryTime=$a2ExpiryTime, loginBitmap=$loginBitmap, tgt=${tgt.toUHexString()}, a2CreationTime=$a2CreationTime, tgtKey=${tgtKey.toUHexString()}, userStSig=$userStSig, userStKey=${userStKey.toUHexString()}, userStWebSig=$userStWebSig, userA5=$userA5, userA8=$userA8, lsKey=$lsKey, sKey=$sKey, userSig64=$userSig64, openId=${openId.toUHexString()}, openKey=$openKey, vKey=$vKey, accessToken=$accessToken, d2=$d2, d2Key=${d2Key.toUHexString()}, sid=$sid, aqSig=$aqSig, psKey=$psKeyMap, superKey=${superKey.toUHexString()}, payToken=${payToken.toUHexString()}, pf=${pf.toUHexString()}, pfKey=${pfKey.toUHexString()}, da2=${da2.toUHexString()}, wtSessionTicket=$wtSessionTicket, wtSessionTicketKey=${wtSessionTicketKey.toUHexString()}, deviceToken=${deviceToken.toUHexString()})"
    }
}

internal typealias PSKeyMap = MutableMap<String, KeyWithExpiry>
internal typealias Pt4TokenMap = MutableMap<String, KeyWithExpiry>

internal fun parsePSKeyMapAndPt4TokenMap(
    data: ByteArray,
    creationTime: Long,
    expireTime: Long,
    outPSKeyMap: PSKeyMap,
    outPt4TokenMap: Pt4TokenMap
) =
    data.read {
        repeat(readShort().toInt()) {
            val domain = readUShortLVString()
            val psKey = readUShortLVByteArray()
            val pt4token = readUShortLVByteArray()

            when {
                psKey.isNotEmpty() -> outPSKeyMap[domain] = KeyWithExpiry(psKey, creationTime, expireTime)
                pt4token.isNotEmpty() -> outPt4TokenMap[domain] = KeyWithExpiry(pt4token, creationTime, expireTime)
            }
        }
    }

@Serializable
internal open class KeyWithExpiry(
    @SerialName("data1") override val data: ByteArray,
    @SerialName("creationTime1") override val creationTime: Long,
    val expireTime: Long
) : KeyWithCreationTime(data, creationTime) {
    override fun toString(): String {
        return "KeyWithExpiry(data=${data.toUHexString()}, creationTime=$creationTime)"
    }
}

@Serializable
internal open class KeyWithCreationTime(
    open val data: ByteArray,
    open val creationTime: Long
) {
    override fun toString(): String {
        return "KeyWithCreationTime(data=${data.toUHexString()}, creationTime=$creationTime)"
    }
}

internal suspend inline fun QQAndroidClient.useNextServers(crossinline block: suspend (host: String, port: Int) -> Unit) {
    if (bot.serverList.isEmpty()) {
        bot.bdhSyncer.loadServerListFromCache()
        if (bot.serverList.isEmpty()) {
            bot.serverList.addAll(DefaultServerList)
        }
    }
    retryCatchingExceptions(bot.serverList.size, except = LoginFailedException::class) l@{
        val pair = bot.serverList[0]
        runCatchingExceptions {
            block(pair.first, pair.second)
            return@l
        }.getOrElse {
            bot.serverList.remove(pair)
            if (it !is LoginFailedException) {
                // 不要重复打印.
                bot.logger.warning(it)
            }
            throw it
        }
    }.getOrElse {
        if (it is LoginFailedException) {
            throw it
        }
        bot.serverList.addAll(DefaultServerList)
        throw NoServerAvailableException(it)
    }
}


@Suppress("RemoveRedundantQualifierName") // bug
internal fun generateTgtgtKey(guid: ByteArray): ByteArray =
    (getRandomByteArray(16) + guid).md5()

internal inline fun <R> QQAndroidClient.tryDecryptOrNull(
    data: ByteArray,
    size: Int = data.size,
    mapper: (ByteArray) -> R
): R? {
    keys.forEach { (key, value) ->
        kotlin.runCatching {
            return mapper(TEA.decrypt(data, value, size).also { PacketLogger.verbose { "成功使用 $key 解密" } })
        }
    }
    return null
}

internal fun QQAndroidClient.allKeys() = mapOf(
    "16 zero" to ByteArray(16),
    "D2 key" to wLoginSigInfo.d2Key,
    "wtSessionTicketKey" to wLoginSigInfo.wtSessionTicketKey,
    "userStKey" to wLoginSigInfo.userStKey,
    "tgtgtKey" to tgtgtKey,
    "tgtKey" to wLoginSigInfo.tgtKey,
    "deviceToken" to wLoginSigInfo.deviceToken,
    "shareKeyCalculatedByConstPubKey" to ecdhWithPublicKey.keyPair.mockedShareKey
    //"t108" to wLoginSigInfo.t1,
    //"t10c" to t10c,
    //"t163" to t163
)
