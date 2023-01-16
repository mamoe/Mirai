/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.internal.network.protocol.packet.login.wtlogin

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.ktor.utils.io.core.*
import net.mamoe.mirai.internal.QQAndroidBot
import net.mamoe.mirai.internal.network.LoginExtraData
import net.mamoe.mirai.internal.network.QQAndroidClient
import net.mamoe.mirai.internal.network.WLoginSigInfo
import net.mamoe.mirai.internal.network.protocol.packet.Tlv
import net.mamoe.mirai.internal.network.protocol.packet.login.WtLogin
import net.mamoe.mirai.internal.network.protocol.packet.t145
import net.mamoe.mirai.internal.utils.io.writeShortLVByteArray
import net.mamoe.mirai.utils.*


internal inline fun WtLoginExt.analysisTlv0x531(
    t531: ByteArray,
    handler: (a1: ByteArray, noPicSig: ByteArray) -> Unit
) {
    val map = t531.toReadPacket().withUse { _readTLVMap() }

    val t106 = map[0x106]
    val t16a = map[0x16a]
    val t113 = map[0x113]
    val t10c = map[0x10c]

    if (t106 != null && t16a != null && t113 != null && t10c != null) {
        handler(t106 + t10c, t16a)
    }
}

/**
 * @see WtLogin
 */
internal interface WtLoginExt { // so as not to register to global extension

    fun onErrorMessage(type: Int, tlvMap: TlvMap, bot: QQAndroidBot): WtLogin.Login.LoginPacketResponse.Error? {
        return tlvMap[0x149]?.read {
            discardExact(2) //type
            val title: String = readUShortLVString()
            val content: String = readUShortLVString()
            val otherInfo: String = readUShortLVString()

            WtLogin.Login.LoginPacketResponse.Error(bot, type, title, content, otherInfo)
        } ?: tlvMap[0x146]?.read {
            discardExact(2) // ver
            discardExact(2)  // code

            val title = readUShortLVString()
            val message = readUShortLVString()
            val errorInfo = readUShortLVString()

            WtLogin.Login.LoginPacketResponse.Error(bot, type, title, message, errorInfo)
        }
    }

    fun TlvMap.getOrEmpty(key: Int): ByteArray {
        return this[key] ?: byteArrayOf()
    }

    /**
     * @throws error
     */
    fun QQAndroidClient.parseWFastLoginInfoDataOutA1(t169: ByteArray): ByteReadPacket {
        val map = t169.toReadPacket().withUse { _readTLVMap() }

        val t106 = map[0x106]
        val t10c = map[0x10c]
        val t16a = map[0x16a]

        check(t106 != null) { "getWFastLoginInfoDataOutA1: Cannot find tlv 0x106!!" }
        check(t10c != null) { "getWFastLoginInfoDataOutA1: Cannot find tlv 0x10c!!" }
        check(t16a != null) { "getWFastLoginInfoDataOutA1: Cannot find tlv 0x16a!!" }

        return buildPacket {
            writeByte(64)
            writeShort(4)

            // TLV
            writeShort(0x106)
            writeShortLVByteArray(t106)

            writeShort(0x10c)
            writeShortLVByteArray(t10c)

            writeShort(0x16a)
            writeShortLVByteArray(t16a)

            t145(device.guid)
        }
    }

    /**
     * login extra data
     *
     * oicq/wlogin_sdk/request/oicq_request.java:1445
     */
    fun QQAndroidClient.analysisTlv537(t537: ByteArray) = t537.read {
        //discardExact(2)
        discardExact(1)
        repeat(readByte().toInt()) {
            loginExtraData.add(
                LoginExtraData( // args are to correct order
                    uin = readLong(),
                    ip = readBytes(readByte().toInt() and 0xff),
                    time = readInt(), // correct
                    version = readInt()
                )
            )
        }
    }

    /**
     * Encrypt sig and key for pic downloading
     */
    fun QQAndroidClient.analysisTlv11d(t11d: ByteArray): WLoginSigInfo.EncryptedDownloadSession = t11d.read {
        val appid = readInt().toLong().and(4294967295L)
        val stKey = ByteArray(16)
        readAvailable(stKey)
        val stSigLength = readUShort().toInt()
        val stSig = ByteArray(stSigLength)
        readAvailable(stSig)
        WLoginSigInfo.EncryptedDownloadSession(
            appid,
            stKey,
            stSig
        )
    }

    /**
     * pwd flag
     */
    fun QQAndroidClient.analysisTlv186(t186: ByteArray) = t186.read {
        discardExact(1)
        pwdFlag = readByte().toInt() == 1
    }

    /**
     * 设置 [QQAndroidClient.uin]
     */
    fun QQAndroidClient.analysisTlv113(t113: ByteArray) = t113.read {
        _uin = readUInt().toLong()

        /*
        // nothing to do

          if (!asyncContext.ifQQLoginInQim(class_1048.productType)) {
              this.field_61436.method_62330(this.field_61436.field_63973, this.field_61436.uin);
          }
         */
    }

    /**
     * 设置 [QQAndroidClient.timeDifference] 和 [QQAndroidClient.ipFromT149]
     */
    fun QQAndroidClient.analysisTlv130(t130: ByteArray) = t130.read {
        discardExact(2)
        timeDifference = readUInt().toLong() - currentTimeSeconds()
        ipFromT149 = readBytes(4)
    }

    fun QQAndroidClient.analysisTlv150(t150: ByteArray) {
        this.t150 = Tlv(t150)
    }

    fun QQAndroidClient.analysisTlv546(t546: ByteArray) {
        val version: Byte
        val algorithmType: Byte
        val hashType: Byte
        var hasResult: Byte
        val maxIndex: Short
        val reserveBytes: ByteArray
        val inputBigNumArr: ByteArray
        val targetHashArr: ByteArray
        val reserveHashArr: ByteArray
        var resultArr: ByteArray = EMPTY_BYTE_ARRAY;
        var costTimeMS: Int = 0;
        var recursiveDepth: Int = 0;
        var failed = false

        fun getPadRemaining(bigNumArr: ByteArray, bound: Short): Int {
            if (bound > 32) {
                return 1
            }
            var maxLoopCount = 255
            var index = 0
            while (maxLoopCount >= 0 && index < bound) {
                if (bigNumArr[maxLoopCount / 8].toInt() and (1 shl maxLoopCount) % 8 != 0) {
                    return 2
                }
                maxLoopCount--
                index++
            }
            return 0
        }

        fun createBigInteger(bigIntArr: ByteArray): BigInteger {
            val sign = if (bigIntArr[0] < 0) {
                Sign.NEGATIVE
            } else {
                if (bigIntArr.firstOrNull { b -> b != 0.toByte() } != null) {
                    Sign.POSITIVE
                } else {
                    Sign.ZERO
                }

            }
            return BigInteger.fromByteArray(bigIntArr, sign)
        }

        fun calcType1(bigNumArrIn: ByteArray, maxLength: Short) {
            bot.logger.info("Calculating Type 1 Pow....")

            var bigIntArrClone = bigNumArrIn.copyOf()
            val originLength = bigIntArrClone.size
            var bigInteger = createBigInteger(bigIntArrClone)
            while (true) {
                if (getPadRemaining(bigIntArrClone.sha256().copyOf(32), maxLength) == 0) {
                    resultArr = bigIntArrClone
                    return
                }
                recursiveDepth++
                bigInteger = bigInteger.add(BigInteger.ONE)
                bigIntArrClone = bigInteger.toByteArray()
                if (bigIntArrClone.size > originLength) {
                    failed = true
                    return
                }
            }
        }

        fun calcType2(bigNumArrIn: ByteArray, hashTarget: ByteArray) {
            bot.logger.info("Calculating Type 2 Pow....")
            var bigIntArrClone = bigNumArrIn.copyOf()
            val originLength = bigIntArrClone.size
            var bigInteger = createBigInteger(bigIntArrClone)
            while (true) {
                if (bigIntArrClone.sha256().copyOf(32).contentEquals(hashTarget)) {
                    resultArr = bigIntArrClone
                    return
                }
                recursiveDepth++
                bigInteger = bigInteger.add(BigInteger.ONE)
                bigIntArrClone = bigInteger.toByteArray()
                if (bigIntArrClone.size > originLength) {
                    failed = true
                    return
                }

            }
        }

        t546.toReadPacket().apply {
            version = readByte()
            algorithmType = readByte()
            hashType = readByte()
            hasResult = readByte()
            maxIndex = readShort()
            reserveBytes = readBytes(2)
            inputBigNumArr = readBytes(readShort().toInt())
            targetHashArr = readBytes(readShort().toInt())
            reserveHashArr = readBytes(readShort().toInt())
            //TODO: remove it since its useless
            if (hasResult == 1.toByte()) {
                resultArr = readBytes(readShort().toInt())
                costTimeMS = readInt()
                recursiveDepth = readInt()
            }
        }
        val startTimeMS: Long = currentTimeMillis()
        costTimeMS = 0
        recursiveDepth = 0
        if (hashType == 1.toByte()) {
            when (algorithmType.toInt()) {
                1 -> calcType1(inputBigNumArr, maxIndex)
                2 -> calcType2(inputBigNumArr, targetHashArr)
                else -> {
                    failed = true
                    bot.logger.warning("Unsupported tlv546 algorithm type:${algorithmType}")
                }
            }
        } else {
            failed = true
            bot.logger.warning("Unsupported tlv546 hash type:${hashType}")
        }
        if (!failed) {
            costTimeMS = (currentTimeMillis() - startTimeMS).toInt()
            hasResult = 1.toByte()
            this.t547 = buildPacket {
                writeByte(version)
                writeByte(algorithmType)
                writeByte(hashType)
                writeByte(hasResult)
                writeShort(maxIndex)
                writeFully(reserveBytes)
                writeShortLVByteArray(inputBigNumArr)
                writeShortLVByteArray(targetHashArr)
                writeShortLVByteArray(reserveHashArr)
                writeShortLVByteArray(resultArr)
                writeInt(costTimeMS)
                writeInt(recursiveDepth)
            }.readBytes()
        } else {
            bot.logger.warning("Calc pow failed, it may affect login!")
        }

    }

    fun QQAndroidClient.analysisTlv161(t161: ByteArray) {
        val tlv = t161.toReadPacket().apply { discardExact(2) }.withUse { _readTLVMap() }

        tlv[0x173]?.let { analysisTlv173(it) }
        tlv[0x17f]?.let { analysisTlv17f(it) }
        tlv[0x172]?.let { rollbackSig = it }
    }

    /**
     * server host
     */
    fun QQAndroidClient.analysisTlv173(t173: ByteArray) {
        t173.read {
            val type = readByte()
            val host = readUShortLVString()
            val port = readShort()

            bot.logger.warning("服务器: host=$host, port=$port, type=$type")
            // SEE oicq_request.java at method analysisT173
        }
    }

    /**
     * ipv6 address
     */
    fun QQAndroidClient.analysisTlv17f(t17f: ByteArray) {
        t17f.read {
            val type = readByte()
            val host = readUShortLVString()
            val port = readShort()

            bot.logger.warning("服务器 ipv6: host=$host, port=$port, type=$type")
            // SEE oicq_request.java at method analysisT17f
        }
    }

    fun Input.readUShortLVString(): String = String(this.readUShortLVByteArray())
}


internal fun ByteArray?.orEmpty(size: Int = 0): ByteArray {
    return this ?: if (size == 0) EMPTY_BYTE_ARRAY else ByteArray(size)
}