/*
 * Copyright 2019-2023 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.utils

import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol
import net.mamoe.mirai.utils.EnumMap
import net.mamoe.mirai.utils.toUHexString

internal class MiraiProtocolInternal(
    var apkId: String,
    var id: Long,
    var ver: String,
    var buildVer: String,
    var sdkVer: String,
    var miscBitMap: Int,
    var subSigMap: Int,
    var mainSigMap: Int,
    var sign: String,
    var buildTime: Long,
    var ssoVersion: Int,
    var appKey: String,
    var supportsQRLogin: Boolean,

    // don't change property signatures, used externally.
) {
    internal companion object {
        // don't change signature
        internal val protocols = EnumMap<MiraiProtocol, MiraiProtocolInternal>(MiraiProtocol::class)

        // don't change signature
        operator fun get(protocol: MiraiProtocol): MiraiProtocolInternal =
            protocols[protocol] ?: error("Internal Error: Missing protocol $protocol")

        init {
            //Updated from MiraiGo (2023/6/18)
            protocols[MiraiProtocol.ANDROID_PHONE] = MiraiProtocolInternal(
                apkId = "com.tencent.mobileqq",
                id = 537163098,
                ver = "8.9.58",
                buildVer = "8.9.58.11170",
                sdkVer = "6.0.0.2545",
                miscBitMap = 150470524,
                subSigMap = 0x10400,
                mainSigMap = 34869344 or 192,
                sign = "A6 B7 45 BF 24 A2 C2 77 52 77 16 F6 F3 6E B6 8D",
                buildTime = 1684467300L,
                ssoVersion = 20,
                appKey = "0S200MNJT807V3GE",
                supportsQRLogin = false,
            )
            //Updated from MiraiGo (2023/6/18)
            protocols[MiraiProtocol.ANDROID_PAD] = MiraiProtocolInternal(
                apkId = "com.tencent.mobileqq",
                id = 537161402,
                ver = "8.9.58",
                buildVer = "8.9.58.11170",
                sdkVer = "6.0.0.2545",
                miscBitMap = 150470524,
                subSigMap = 0x10400,
                mainSigMap = 34869344 or 192,
                sign = "A6 B7 45 BF 24 A2 C2 77 52 77 16 F6 F3 6E B6 8D",
                buildTime = 1684467300L,
                ssoVersion = 20,
                appKey = "0S200MNJT807V3GE",
                supportsQRLogin = false,
            )
            //Updated from MiraiGo (2023/3/24)
            protocols[MiraiProtocol.ANDROID_WATCH] = MiraiProtocolInternal(
                apkId = "com.tencent.qqlite",
                id = 537065138,
                ver = "2.0.8",
                buildVer = "2.0.8",
                sdkVer = "6.0.0.2365",
                miscBitMap = 16252796,
                subSigMap = 0x10400,
                mainSigMap = 16724722,
                sign = "A6 B7 45 BF 24 A2 C2 77 52 77 16 F6 F3 6E B6 8D",
                buildTime = 1559564731L,
                ssoVersion = 5,
                appKey = "",
                supportsQRLogin = true,
            )
            protocols[MiraiProtocol.IPAD] = MiraiProtocolInternal(
                apkId = "com.tencent.minihd.qq",
                id = 537151363,
                ver = "8.9.33",
                buildVer = "8.9.33.614",
                sdkVer = "6.0.0.2433",
                miscBitMap = 150470524,
                subSigMap = 66560,
                mainSigMap = 1970400,
                sign = "AA 39 78 F4 1F D9 6F F9 91 4A 66 9E 18 64 74 C7",
                buildTime = 1640921786L,
                ssoVersion = 12,
                appKey = "",
                supportsQRLogin = false,
            )
            protocols[MiraiProtocol.MACOS] = MiraiProtocolInternal(
                apkId = "com.tencent.qq",
                id = 537128930,
                ver = "6.8.2",
                buildVer = "6.8.2.21241",
                sdkVer = "6.2.0.1023",
                miscBitMap = 0x7ffc,
                subSigMap = 66560,
                mainSigMap = 1970400,
                sign = "com.tencent.qq".encodeToByteArray().toUHexString(" "),
                buildTime = 1647227495L,
                ssoVersion = 7,
                appKey = "",
                supportsQRLogin = true,
            )
        }

        inline val MiraiProtocol.asInternal: MiraiProtocolInternal get() = get(this)
    }
}
