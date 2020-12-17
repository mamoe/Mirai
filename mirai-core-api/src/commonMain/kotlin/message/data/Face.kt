/*
 * Copyright 2019-2020 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:JvmMultifileClass
@file:JvmName("MessageUtils")

package net.mamoe.mirai.message.data

import kotlinx.serialization.Serializable
import net.mamoe.mirai.message.code.CodableMessage

/**
 * QQ 自带表情
 *
 * ## mirai 码支持
 * 格式: &#91;mirai:face:*[id]*&#93;
 */
@Serializable
public data class Face(public val id: Int) : // used in delegation
    MessageContent, CodableMessage {

    public override fun toString(): String = "[mirai:face:$id]";
    public val name: String = contentToString().let { it.substring(1, it.length - 1) }
    public override fun contentToString(): String = names.getOrElse(id) { "[表情]" }

    public override fun equals(other: Any?): Boolean = other is Face && other.id == this.id
    public override fun hashCode(): Int = id

    //Auto generated
    @Suppress("NonAsciiCharacters", "unused", "SpellCheckingInspection", "all")
    public companion object {
        public const val JING_YA: Int = 0
        public const val 惊讶: Int = JING_YA
        public const val PIE_ZUI: Int = 1
        public const val 撇嘴: Int = PIE_ZUI
        public const val SE: Int = 2
        public const val 色: Int = SE
        public const val FA_DAI: Int = 3
        public const val 发呆: Int = FA_DAI
        public const val DE_YI: Int = 4
        public const val 得意: Int = DE_YI
        public const val LIU_LEI: Int = 5
        public const val 流泪: Int = LIU_LEI
        public const val HAI_XIU: Int = 6
        public const val 害羞: Int = HAI_XIU
        public const val BI_ZUI: Int = 7
        public const val 闭嘴: Int = BI_ZUI
        public const val SHUI: Int = 8
        public const val 睡: Int = SHUI
        public const val DA_KU: Int = 9
        public const val 大哭: Int = DA_KU
        public const val GAN_GA: Int = 10
        public const val 尴尬: Int = GAN_GA
        public const val FA_NU: Int = 11
        public const val 发怒: Int = FA_NU
        public const val TIAO_PI: Int = 12
        public const val 调皮: Int = TIAO_PI
        public const val ZI_YA: Int = 13
        public const val 呲牙: Int = ZI_YA
        public const val WEI_XIAO: Int = 14
        public const val 微笑: Int = WEI_XIAO
        public const val NAN_GUO: Int = 15
        public const val 难过: Int = NAN_GUO
        public const val KU: Int = 16
        public const val 酷: Int = KU
        public const val ZHUA_KUANG: Int = 18
        public const val 抓狂: Int = ZHUA_KUANG
        public const val TU: Int = 19
        public const val 吐: Int = TU
        public const val TOU_XIAO: Int = 20
        public const val 偷笑: Int = TOU_XIAO
        public const val KE_AI: Int = 21
        public const val 可爱: Int = KE_AI
        public const val BAI_YAN: Int = 22
        public const val 白眼: Int = BAI_YAN
        public const val AO_MAN: Int = 23
        public const val 傲慢: Int = AO_MAN
        public const val JI_E: Int = 24
        public const val 饥饿: Int = JI_E
        public const val KUN: Int = 25
        public const val 困: Int = KUN
        public const val JING_KONG: Int = 26
        public const val 惊恐: Int = JING_KONG
        public const val LIU_HAN: Int = 27
        public const val 流汗: Int = LIU_HAN
        public const val HAN_XIAO: Int = 28
        public const val 憨笑: Int = HAN_XIAO
        public const val YOU_XIAN: Int = 29
        public const val 悠闲: Int = YOU_XIAN
        public const val FEN_DOU: Int = 30
        public const val 奋斗: Int = FEN_DOU
        public const val ZHOU_MA: Int = 31
        public const val 咒骂: Int = ZHOU_MA
        public const val YI_WEN: Int = 32
        public const val 疑问: Int = YI_WEN
        public const val XU: Int = 33
        public const val 嘘: Int = XU
        public const val YUN: Int = 34
        public const val 晕: Int = YUN
        public const val ZHE_MO: Int = 35
        public const val 折磨: Int = ZHE_MO
        public const val SHUAI: Int = 36
        public const val 衰: Int = SHUAI
        public const val KU_LOU: Int = 37
        public const val 骷髅: Int = KU_LOU
        public const val QIAO_DA: Int = 38
        public const val 敲打: Int = QIAO_DA
        public const val ZAI_JIAN: Int = 39
        public const val 再见: Int = ZAI_JIAN
        public const val FA_DOU: Int = 41
        public const val 发抖: Int = FA_DOU
        public const val AI_QING: Int = 42
        public const val 爱情: Int = AI_QING
        public const val TIAO_TIAO: Int = 43
        public const val 跳跳: Int = TIAO_TIAO
        public const val ZHU_TOU: Int = 46
        public const val 猪头: Int = ZHU_TOU
        public const val YONG_BAO: Int = 49
        public const val 拥抱: Int = YONG_BAO
        public const val DAN_GAO: Int = 53
        public const val 蛋糕: Int = DAN_GAO
        public const val SHAN_DIAN: Int = 54
        public const val 闪电: Int = SHAN_DIAN
        public const val ZHA_DAN: Int = 55
        public const val 炸弹: Int = ZHA_DAN
        public const val DAO: Int = 56
        public const val 刀: Int = DAO
        public const val ZU_QIU: Int = 57
        public const val 足球: Int = ZU_QIU
        public const val BIAN_BIAN: Int = 59
        public const val 便便: Int = BIAN_BIAN
        public const val KA_FEI: Int = 60
        public const val 咖啡: Int = KA_FEI
        public const val FAN: Int = 61
        public const val 饭: Int = FAN
        public const val MEI_GUI: Int = 63
        public const val 玫瑰: Int = MEI_GUI
        public const val DIAO_XIE: Int = 64
        public const val 凋谢: Int = DIAO_XIE
        public const val AI_XIN: Int = 66
        public const val 爱心: Int = AI_XIN
        public const val XIN_SUI: Int = 67
        public const val 心碎: Int = XIN_SUI
        public const val LI_WU: Int = 69
        public const val 礼物: Int = LI_WU
        public const val TAI_YANG: Int = 74
        public const val 太阳: Int = TAI_YANG
        public const val YUE_LIANG: Int = 75
        public const val 月亮: Int = YUE_LIANG
        public const val ZAN: Int = 76
        public const val 赞: Int = ZAN
        public const val CAI: Int = 77
        public const val 踩: Int = CAI
        public const val WO_SHOU: Int = 78
        public const val 握手: Int = WO_SHOU
        public const val SHENG_LI: Int = 79
        public const val 胜利: Int = SHENG_LI
        public const val FEI_WEN: Int = 85
        public const val 飞吻: Int = FEI_WEN
        public const val OU_HUO: Int = 86
        public const val 怄火: Int = OU_HUO
        public const val XI_GUA: Int = 89
        public const val 西瓜: Int = XI_GUA
        public const val LENG_HAN: Int = 96
        public const val 冷汗: Int = LENG_HAN
        public const val CA_HAN: Int = 97
        public const val 擦汗: Int = CA_HAN
        public const val KOU_BI: Int = 98
        public const val 抠鼻: Int = KOU_BI
        public const val GU_ZHANG: Int = 99
        public const val 鼓掌: Int = GU_ZHANG
        public const val QIU_DA_LE: Int = 100
        public const val 糗大了: Int = QIU_DA_LE
        public const val HUAI_XIAO: Int = 101
        public const val 坏笑: Int = HUAI_XIAO
        public const val ZUO_HENG_HENG: Int = 102
        public const val 左哼哼: Int = ZUO_HENG_HENG
        public const val YOU_HENG_HENG: Int = 103
        public const val 右哼哼: Int = YOU_HENG_HENG
        public const val HA_QIAN: Int = 104
        public const val 哈欠: Int = HA_QIAN
        public const val BI_SHI: Int = 105
        public const val 鄙视: Int = BI_SHI
        public const val WEI_QU: Int = 106
        public const val 委屈: Int = WEI_QU
        public const val KUAI_KU_LE: Int = 107
        public const val 快哭了: Int = KUAI_KU_LE
        public const val YIN_XIAN: Int = 108
        public const val 阴险: Int = YIN_XIAN
        public const val QIN_QIN: Int = 109
        public const val 亲亲: Int = QIN_QIN
        public const val XIA: Int = 110
        public const val 吓: Int = XIA
        public const val KE_LIAN: Int = 111
        public const val 可怜: Int = KE_LIAN
        public const val CAI_DAO: Int = 112
        public const val 菜刀: Int = CAI_DAO
        public const val PI_JIU: Int = 113
        public const val 啤酒: Int = PI_JIU
        public const val LAN_QIU: Int = 114
        public const val 篮球: Int = LAN_QIU
        public const val PING_PANG: Int = 115
        public const val 乒乓: Int = PING_PANG
        public const val SHI_AI: Int = 116
        public const val 示爱: Int = SHI_AI
        public const val PIAO_CHONG: Int = 117
        public const val 瓢虫: Int = PIAO_CHONG
        public const val BAO_QUAN: Int = 118
        public const val 抱拳: Int = BAO_QUAN
        public const val GOU_YIN: Int = 119
        public const val 勾引: Int = GOU_YIN
        public const val QUAN_TOU: Int = 120
        public const val 拳头: Int = QUAN_TOU
        public const val CHA_JIN: Int = 121
        public const val 差劲: Int = CHA_JIN
        public const val AI_NI: Int = 122
        public const val 爱你: Int = AI_NI
        public const val NO: Int = 123
        public const val OK: Int = 124
        public const val ZHUAN_QUAN: Int = 125
        public const val 转圈: Int = ZHUAN_QUAN
        public const val KE_TOU: Int = 126
        public const val 磕头: Int = KE_TOU
        public const val HUI_TOU: Int = 127
        public const val 回头: Int = HUI_TOU
        public const val TIAO_SHENG: Int = 128
        public const val 跳绳: Int = TIAO_SHENG
        public const val HUI_SHOU: Int = 129
        public const val 挥手: Int = HUI_SHOU
        public const val JI_DONG: Int = 130
        public const val 激动: Int = JI_DONG
        public const val JIE_WU: Int = 131
        public const val 街舞: Int = JIE_WU
        public const val XIAN_WEN: Int = 132
        public const val 献吻: Int = XIAN_WEN
        public const val ZUO_TAI_JI: Int = 133
        public const val 左太极: Int = ZUO_TAI_JI
        public const val YOU_TAI_JI: Int = 134
        public const val 右太极: Int = YOU_TAI_JI
        public const val SHUANG_XI: Int = 136
        public const val 双喜: Int = SHUANG_XI
        public const val BIAN_PAO: Int = 137
        public const val 鞭炮: Int = BIAN_PAO
        public const val DENG_LONG: Int = 138
        public const val 灯笼: Int = DENG_LONG
        public const val K_GE: Int = 140
        public const val K歌: Int = K_GE
        public const val HE_CAI: Int = 144
        public const val 喝彩: Int = HE_CAI
        public const val QI_DAO: Int = 145
        public const val 祈祷: Int = QI_DAO
        public const val BAO_JIN: Int = 146
        public const val 爆筋: Int = BAO_JIN
        public const val BANG_BANG_TANG: Int = 147
        public const val 棒棒糖: Int = BANG_BANG_TANG
        public const val HE_NAI: Int = 148
        public const val 喝奶: Int = HE_NAI
        public const val FEI_JI: Int = 151
        public const val 飞机: Int = FEI_JI
        public const val CHAO_PIAO: Int = 158
        public const val 钞票: Int = CHAO_PIAO
        public const val YAO: Int = 168
        public const val 药: Int = YAO
        public const val SHOU_QIANG: Int = 169
        public const val 手枪: Int = SHOU_QIANG
        public const val CHA: Int = 171
        public const val 茶: Int = CHA
        public const val ZHA_YAN_JING: Int = 172
        public const val 眨眼睛: Int = ZHA_YAN_JING
        public const val LEI_BEN: Int = 173
        public const val 泪奔: Int = LEI_BEN
        public const val WU_NAI: Int = 174
        public const val 无奈: Int = WU_NAI
        public const val MAI_MENG: Int = 175
        public const val 卖萌: Int = MAI_MENG
        public const val XIAO_JIU_JIE: Int = 176
        public const val 小纠结: Int = XIAO_JIU_JIE
        public const val PEN_XIE: Int = 177
        public const val 喷血: Int = PEN_XIE
        public const val XIE_YAN_XIAO: Int = 178
        public const val 斜眼笑: Int = XIE_YAN_XIAO
        public const val doge: Int = 179
        public const val JING_XI: Int = 180
        public const val 惊喜: Int = JING_XI
        public const val SAO_RAO: Int = 181
        public const val 骚扰: Int = SAO_RAO
        public const val XIAO_KU: Int = 182
        public const val 笑哭: Int = XIAO_KU
        public const val WO_ZUI_MEI: Int = 183
        public const val 我最美: Int = WO_ZUI_MEI
        public const val HE_XIE: Int = 184
        public const val 河蟹: Int = HE_XIE
        public const val YANG_TUO: Int = 185
        public const val 羊驼: Int = YANG_TUO
        public const val YOU_LING: Int = 187
        public const val 幽灵: Int = YOU_LING
        public const val DAN: Int = 188
        public const val 蛋: Int = DAN
        public const val JU_HUA: Int = 190
        public const val 菊花: Int = JU_HUA
        public const val HONG_BAO: Int = 192
        public const val 红包: Int = HONG_BAO
        public const val DA_XIAO: Int = 193
        public const val 大笑: Int = DA_XIAO
        public const val BU_KAI_XIN: Int = 194
        public const val 不开心: Int = BU_KAI_XIN
        public const val LENG_MO: Int = 197
        public const val 冷漠: Int = LENG_MO
        public const val E: Int = 198
        public const val 呃: Int = E
        public const val HAO_BANG: Int = 199
        public const val 好棒: Int = HAO_BANG
        public const val BAI_TUO: Int = 200
        public const val 拜托: Int = BAI_TUO
        public const val DIAN_ZAN: Int = 201
        public const val 点赞: Int = DIAN_ZAN
        public const val WU_LIAO: Int = 202
        public const val 无聊: Int = WU_LIAO
        public const val TUO_LIAN: Int = 203
        public const val 托脸: Int = TUO_LIAN
        public const val CHI: Int = 204
        public const val 吃: Int = CHI
        public const val SONG_HUA: Int = 205
        public const val 送花: Int = SONG_HUA
        public const val HAI_PA: Int = 206
        public const val 害怕: Int = HAI_PA
        public const val HUA_CHI: Int = 207
        public const val 花痴: Int = HUA_CHI
        public const val XIAO_YANG_ER: Int = 208
        public const val 小样儿: Int = XIAO_YANG_ER
        public const val BIAO_LEI: Int = 210
        public const val 飙泪: Int = BIAO_LEI
        public const val WO_BU_KAN: Int = 211
        public const val 我不看: Int = WO_BU_KAN
        public const val TUO_SAI: Int = 212
        public const val 托腮: Int = TUO_SAI
        public const val BO_BO: Int = 214
        public const val 啵啵: Int = BO_BO
        public const val HU_LIAN: Int = 215
        public const val 糊脸: Int = HU_LIAN
        public const val PAI_TOU: Int = 216
        public const val 拍头: Int = PAI_TOU
        public const val CHE_YI_CHE: Int = 217
        public const val 扯一扯: Int = CHE_YI_CHE
        public const val TIAN_YI_TIAN: Int = 218
        public const val 舔一舔: Int = TIAN_YI_TIAN
        public const val CENG_YI_CENG: Int = 219
        public const val 蹭一蹭: Int = CENG_YI_CENG
        public const val ZHUAI_ZHA_TIAN: Int = 220
        public const val 拽炸天: Int = ZHUAI_ZHA_TIAN
        public const val DING_GUA_GUA: Int = 221
        public const val 顶呱呱: Int = DING_GUA_GUA
        public const val BAO_BAO: Int = 222
        public const val 抱抱: Int = BAO_BAO
        public const val BAO_JI: Int = 223
        public const val 暴击: Int = BAO_JI
        public const val KAI_QIANG: Int = 224
        public const val 开枪: Int = KAI_QIANG
        public const val LIAO_YI_LIAO: Int = 225
        public const val 撩一撩: Int = LIAO_YI_LIAO
        public const val PAI_ZHUO: Int = 226
        public const val 拍桌: Int = PAI_ZHUO
        public const val PAI_SHOU: Int = 227
        public const val 拍手: Int = PAI_SHOU
        public const val GONG_XI: Int = 228
        public const val 恭喜: Int = GONG_XI
        public const val GAN_BEI: Int = 229
        public const val 干杯: Int = GAN_BEI
        public const val CHAO_FENG: Int = 230
        public const val 嘲讽: Int = CHAO_FENG
        public const val HENG: Int = 231
        public const val 哼: Int = HENG
        public const val FO_XI: Int = 232
        public const val 佛系: Int = FO_XI
        public const val QIA_YI_QIA: Int = 233
        public const val 掐一掐: Int = QIA_YI_QIA
        public const val JING_DAI: Int = 234
        public const val 惊呆: Int = JING_DAI
        public const val CHAN_DOU: Int = 235
        public const val 颤抖: Int = CHAN_DOU
        public const val KEN_TOU: Int = 236
        public const val 啃头: Int = KEN_TOU
        public const val TOU_KAN: Int = 237
        public const val 偷看: Int = TOU_KAN
        public const val SHAN_LIAN: Int = 238
        public const val 扇脸: Int = SHAN_LIAN
        public const val YUAN_LIANG: Int = 239
        public const val 原谅: Int = YUAN_LIANG
        public const val PEN_LIAN: Int = 240
        public const val 喷脸: Int = PEN_LIAN
        public const val SHENG_RI_KUAI_LE: Int = 241
        public const val 生日快乐: Int = SHENG_RI_KUAI_LE
        public const val TOU_ZHUANG_JI: Int = 242
        public const val 头撞击: Int = TOU_ZHUANG_JI
        public const val SHUAI_TOU: Int = 243
        public const val 甩头: Int = SHUAI_TOU
        public const val RENG_GOU: Int = 244
        public const val 扔狗: Int = RENG_GOU
        public const val JIA_YOU_BI_SHENG: Int = 245
        public const val 加油必胜: Int = JIA_YOU_BI_SHENG
        public const val JIA_YOU_BAO_BAO: Int = 246
        public const val 加油抱抱: Int = JIA_YOU_BAO_BAO
        public const val KOU_ZHAO_HU_TI: Int = 247
        public const val 口罩护体: Int = KOU_ZHAO_HU_TI
        public const val BAN_ZHUAN_ZHONG: Int = 260
        public const val 搬砖中: Int = BAN_ZHUAN_ZHONG
        public const val MANG_DAO_FEI_QI: Int = 261
        public const val 忙到飞起: Int = MANG_DAO_FEI_QI
        public const val NAO_KUO_TENG: Int = 262
        public const val 脑阔疼: Int = NAO_KUO_TENG
        public const val CANG_SANG: Int = 263
        public const val 沧桑: Int = CANG_SANG
        public const val WU_LIAN: Int = 264
        public const val 捂脸: Int = WU_LIAN
        public const val LA_YAN_JING: Int = 265
        public const val 辣眼睛: Int = LA_YAN_JING
        public const val O_YO: Int = 266
        public const val 哦哟: Int = O_YO
        public const val TOU_TU: Int = 267
        public const val 头秃: Int = TOU_TU
        public const val WEN_HAO_LIAN: Int = 268
        public const val 问号脸: Int = WEN_HAO_LIAN
        public const val AN_ZHONG_GUAN_CHA: Int = 269
        public const val 暗中观察: Int = AN_ZHONG_GUAN_CHA
        public const val emm: Int = 270
        public const val CHI_GUA: Int = 271
        public const val 吃瓜: Int = CHI_GUA
        public const val HE_HE_DA: Int = 272
        public const val 呵呵哒: Int = HE_HE_DA
        public const val WO_SUAN_LE: Int = 273
        public const val 我酸了: Int = WO_SUAN_LE
        public const val TAI_NAN_LE: Int = 274
        public const val 太南了: Int = TAI_NAN_LE
        public const val LA_JIAO_JIANG: Int = 276
        public const val 辣椒酱: Int = LA_JIAO_JIANG
        public const val WANG_WANG: Int = 277
        public const val 汪汪: Int = WANG_WANG
        public const val HAN: Int = 278
        public const val 汗: Int = HAN
        public const val DA_LIAN: Int = 279
        public const val 打脸: Int = DA_LIAN
        public const val JI_ZHANG: Int = 280
        public const val 击掌: Int = JI_ZHANG
        public const val WU_YAN_XIAO: Int = 281
        public const val 无眼笑: Int = WU_YAN_XIAO
        public const val JING_LI: Int = 282
        public const val 敬礼: Int = JING_LI
        public const val KUANG_XIAO: Int = 283
        public const val 狂笑: Int = KUANG_XIAO
        public const val MIAN_WU_BIAO_QING: Int = 284
        public const val 面无表情: Int = MIAN_WU_BIAO_QING
        public const val MO_YU: Int = 285
        public const val 摸鱼: Int = MO_YU
        public const val MO_GUI_XIAO: Int = 286
        public const val 魔鬼笑: Int = MO_GUI_XIAO
        public const val O: Int = 287
        public const val 哦: Int = O
        public const val QING: Int = 288
        public const val 请: Int = QING
        public const val ZHENG_YAN: Int = 289
        public const val 睁眼: Int = ZHENG_YAN
        internal val names: Array<String> = Array(290) { "[表情]" }

        init {
            names[JING_YA] = "[惊讶]"
            names[PIE_ZUI] = "[撇嘴]"
            names[SE] = "[色]"
            names[FA_DAI] = "[发呆]"
            names[DE_YI] = "[得意]"
            names[LIU_LEI] = "[流泪]"
            names[HAI_XIU] = "[害羞]"
            names[BI_ZUI] = "[闭嘴]"
            names[SHUI] = "[睡]"
            names[DA_KU] = "[大哭]"
            names[GAN_GA] = "[尴尬]"
            names[FA_NU] = "[发怒]"
            names[TIAO_PI] = "[调皮]"
            names[ZI_YA] = "[呲牙]"
            names[WEI_XIAO] = "[微笑]"
            names[NAN_GUO] = "[难过]"
            names[KU] = "[酷]"
            names[ZHUA_KUANG] = "[抓狂]"
            names[TU] = "[吐]"
            names[TOU_XIAO] = "[偷笑]"
            names[KE_AI] = "[可爱]"
            names[BAI_YAN] = "[白眼]"
            names[AO_MAN] = "[傲慢]"
            names[JI_E] = "[饥饿]"
            names[KUN] = "[困]"
            names[JING_KONG] = "[惊恐]"
            names[LIU_HAN] = "[流汗]"
            names[HAN_XIAO] = "[憨笑]"
            names[YOU_XIAN] = "[悠闲]"
            names[FEN_DOU] = "[奋斗]"
            names[ZHOU_MA] = "[咒骂]"
            names[YI_WEN] = "[疑问]"
            names[XU] = "[嘘]"
            names[YUN] = "[晕]"
            names[ZHE_MO] = "[折磨]"
            names[SHUAI] = "[衰]"
            names[KU_LOU] = "[骷髅]"
            names[QIAO_DA] = "[敲打]"
            names[ZAI_JIAN] = "[再见]"
            names[FA_DOU] = "[发抖]"
            names[AI_QING] = "[爱情]"
            names[TIAO_TIAO] = "[跳跳]"
            names[ZHU_TOU] = "[猪头]"
            names[YONG_BAO] = "[拥抱]"
            names[DAN_GAO] = "[蛋糕]"
            names[SHAN_DIAN] = "[闪电]"
            names[ZHA_DAN] = "[炸弹]"
            names[DAO] = "[刀]"
            names[ZU_QIU] = "[足球]"
            names[BIAN_BIAN] = "[便便]"
            names[KA_FEI] = "[咖啡]"
            names[FAN] = "[饭]"
            names[MEI_GUI] = "[玫瑰]"
            names[DIAO_XIE] = "[凋谢]"
            names[AI_XIN] = "[爱心]"
            names[XIN_SUI] = "[心碎]"
            names[LI_WU] = "[礼物]"
            names[TAI_YANG] = "[太阳]"
            names[YUE_LIANG] = "[月亮]"
            names[ZAN] = "[赞]"
            names[CAI] = "[踩]"
            names[WO_SHOU] = "[握手]"
            names[SHENG_LI] = "[胜利]"
            names[FEI_WEN] = "[飞吻]"
            names[OU_HUO] = "[怄火]"
            names[XI_GUA] = "[西瓜]"
            names[LENG_HAN] = "[冷汗]"
            names[CA_HAN] = "[擦汗]"
            names[KOU_BI] = "[抠鼻]"
            names[GU_ZHANG] = "[鼓掌]"
            names[QIU_DA_LE] = "[糗大了]"
            names[HUAI_XIAO] = "[坏笑]"
            names[ZUO_HENG_HENG] = "[左哼哼]"
            names[YOU_HENG_HENG] = "[右哼哼]"
            names[HA_QIAN] = "[哈欠]"
            names[BI_SHI] = "[鄙视]"
            names[WEI_QU] = "[委屈]"
            names[KUAI_KU_LE] = "[快哭了]"
            names[YIN_XIAN] = "[阴险]"
            names[QIN_QIN] = "[亲亲]"
            names[XIA] = "[吓]"
            names[KE_LIAN] = "[可怜]"
            names[CAI_DAO] = "[菜刀]"
            names[PI_JIU] = "[啤酒]"
            names[LAN_QIU] = "[篮球]"
            names[PING_PANG] = "[乒乓]"
            names[SHI_AI] = "[示爱]"
            names[PIAO_CHONG] = "[瓢虫]"
            names[BAO_QUAN] = "[抱拳]"
            names[GOU_YIN] = "[勾引]"
            names[QUAN_TOU] = "[拳头]"
            names[CHA_JIN] = "[差劲]"
            names[AI_NI] = "[爱你]"
            names[NO] = "[NO]"
            names[OK] = "[OK]"
            names[ZHUAN_QUAN] = "[转圈]"
            names[KE_TOU] = "[磕头]"
            names[HUI_TOU] = "[回头]"
            names[TIAO_SHENG] = "[跳绳]"
            names[HUI_SHOU] = "[挥手]"
            names[JI_DONG] = "[激动]"
            names[JIE_WU] = "[街舞]"
            names[XIAN_WEN] = "[献吻]"
            names[ZUO_TAI_JI] = "[左太极]"
            names[YOU_TAI_JI] = "[右太极]"
            names[SHUANG_XI] = "[双喜]"
            names[BIAN_PAO] = "[鞭炮]"
            names[DENG_LONG] = "[灯笼]"
            names[K_GE] = "[K歌]"
            names[HE_CAI] = "[喝彩]"
            names[QI_DAO] = "[祈祷]"
            names[BAO_JIN] = "[爆筋]"
            names[BANG_BANG_TANG] = "[棒棒糖]"
            names[HE_NAI] = "[喝奶]"
            names[FEI_JI] = "[飞机]"
            names[CHAO_PIAO] = "[钞票]"
            names[YAO] = "[药]"
            names[SHOU_QIANG] = "[手枪]"
            names[CHA] = "[茶]"
            names[ZHA_YAN_JING] = "[眨眼睛]"
            names[LEI_BEN] = "[泪奔]"
            names[WU_NAI] = "[无奈]"
            names[MAI_MENG] = "[卖萌]"
            names[XIAO_JIU_JIE] = "[小纠结]"
            names[PEN_XIE] = "[喷血]"
            names[XIE_YAN_XIAO] = "[斜眼笑]"
            names[doge] = "[doge]"
            names[JING_XI] = "[惊喜]"
            names[SAO_RAO] = "[骚扰]"
            names[XIAO_KU] = "[笑哭]"
            names[WO_ZUI_MEI] = "[我最美]"
            names[HE_XIE] = "[河蟹]"
            names[YANG_TUO] = "[羊驼]"
            names[YOU_LING] = "[幽灵]"
            names[DAN] = "[蛋]"
            names[JU_HUA] = "[菊花]"
            names[HONG_BAO] = "[红包]"
            names[DA_XIAO] = "[大笑]"
            names[BU_KAI_XIN] = "[不开心]"
            names[LENG_MO] = "[冷漠]"
            names[E] = "[呃]"
            names[HAO_BANG] = "[好棒]"
            names[BAI_TUO] = "[拜托]"
            names[DIAN_ZAN] = "[点赞]"
            names[WU_LIAO] = "[无聊]"
            names[TUO_LIAN] = "[托脸]"
            names[CHI] = "[吃]"
            names[SONG_HUA] = "[送花]"
            names[HAI_PA] = "[害怕]"
            names[HUA_CHI] = "[花痴]"
            names[XIAO_YANG_ER] = "[小样儿]"
            names[BIAO_LEI] = "[飙泪]"
            names[WO_BU_KAN] = "[我不看]"
            names[TUO_SAI] = "[托腮]"
            names[BO_BO] = "[啵啵]"
            names[HU_LIAN] = "[糊脸]"
            names[PAI_TOU] = "[拍头]"
            names[CHE_YI_CHE] = "[扯一扯]"
            names[TIAN_YI_TIAN] = "[舔一舔]"
            names[CENG_YI_CENG] = "[蹭一蹭]"
            names[ZHUAI_ZHA_TIAN] = "[拽炸天]"
            names[DING_GUA_GUA] = "[顶呱呱]"
            names[BAO_BAO] = "[抱抱]"
            names[BAO_JI] = "[暴击]"
            names[KAI_QIANG] = "[开枪]"
            names[LIAO_YI_LIAO] = "[撩一撩]"
            names[PAI_ZHUO] = "[拍桌]"
            names[PAI_SHOU] = "[拍手]"
            names[GONG_XI] = "[恭喜]"
            names[GAN_BEI] = "[干杯]"
            names[CHAO_FENG] = "[嘲讽]"
            names[HENG] = "[哼]"
            names[FO_XI] = "[佛系]"
            names[QIA_YI_QIA] = "[掐一掐]"
            names[JING_DAI] = "[惊呆]"
            names[CHAN_DOU] = "[颤抖]"
            names[KEN_TOU] = "[啃头]"
            names[TOU_KAN] = "[偷看]"
            names[SHAN_LIAN] = "[扇脸]"
            names[YUAN_LIANG] = "[原谅]"
            names[PEN_LIAN] = "[喷脸]"
            names[SHENG_RI_KUAI_LE] = "[生日快乐]"
            names[TOU_ZHUANG_JI] = "[头撞击]"
            names[SHUAI_TOU] = "[甩头]"
            names[RENG_GOU] = "[扔狗]"
            names[JIA_YOU_BI_SHENG] = "[加油必胜]"
            names[JIA_YOU_BAO_BAO] = "[加油抱抱]"
            names[KOU_ZHAO_HU_TI] = "[口罩护体]"
            names[BAN_ZHUAN_ZHONG] = "[搬砖中]"
            names[MANG_DAO_FEI_QI] = "[忙到飞起]"
            names[NAO_KUO_TENG] = "[脑阔疼]"
            names[CANG_SANG] = "[沧桑]"
            names[WU_LIAN] = "[捂脸]"
            names[LA_YAN_JING] = "[辣眼睛]"
            names[O_YO] = "[哦哟]"
            names[TOU_TU] = "[头秃]"
            names[WEN_HAO_LIAN] = "[问号脸]"
            names[AN_ZHONG_GUAN_CHA] = "[暗中观察]"
            names[emm] = "[emm]"
            names[CHI_GUA] = "[吃瓜]"
            names[HE_HE_DA] = "[呵呵哒]"
            names[WO_SUAN_LE] = "[我酸了]"
            names[TAI_NAN_LE] = "[太南了]"
            names[LA_JIAO_JIANG] = "[辣椒酱]"
            names[WANG_WANG] = "[汪汪]"
            names[HAN] = "[汗]"
            names[DA_LIAN] = "[打脸]"
            names[JI_ZHANG] = "[击掌]"
            names[WU_YAN_XIAO] = "[无眼笑]"
            names[JING_LI] = "[敬礼]"
            names[KUANG_XIAO] = "[狂笑]"
            names[MIAN_WU_BIAO_QING] = "[面无表情]"
            names[MO_YU] = "[摸鱼]"
            names[MO_GUI_XIAO] = "[魔鬼笑]"
            names[O] = "[哦]"
            names[QING] = "[请]"
            names[ZHENG_YAN] = "[睁眼]"
        }

    }
}