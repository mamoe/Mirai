/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("EXPERIMENTAL_API_USAGE", "unused")

package net.mamoe.mirai.utils

import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.io.InputStream
import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.Input
import kotlinx.serialization.InternalSerializationApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.OfflineImage
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.message.data.toLongUnsigned
import kotlin.jvm.JvmSynthetic

/**
 * 外部图片. 图片数据还没有读取到内存.
 *
 * 在 JVM, 请查看 'ExternalImageJvm.kt'
 *
 * @see ExternalImage.sendTo 上传图片并以纯图片消息发送给联系人
 * @See ExternalImage.upload 上传图片并得到 [Image] 消息
 */
class ExternalImage private constructor(
    val md5: ByteArray,
    val input: Any, // Input from kotlinx.io, InputStream from kotlinx.io MPP, ByteReadChannel from ktor
    val inputSize: Long // dont be greater than Int.MAX
) {
    @Deprecated("changing soon in 1.0.0", level = DeprecationLevel.ERROR)
    constructor(
        md5: ByteArray,
        input: ByteReadChannel,
        inputSize: Long // dont be greater than Int.MAX
    ) : this(md5, input as Any, inputSize)

    @Deprecated("changing soon in 1.0.0", level = DeprecationLevel.ERROR)
    constructor(
        md5: ByteArray,
        input: Input,
        inputSize: Long // dont be greater than Int.MAX
    ) : this(md5, input as Any, inputSize)

    @Deprecated("changing soon in 1.0.0", level = DeprecationLevel.ERROR)
    constructor(
        md5: ByteArray,
        input: ByteReadPacket
    ) : this(md5, input as Any, input.remaining)

    @Deprecated("changing soon in 1.0.0", level = DeprecationLevel.ERROR)
    @OptIn(InternalSerializationApi::class)
    constructor(
        md5: ByteArray,
        input: InputStream
    ) : this(md5, input as Any, input.available().toLongUnsigned())

    init {
        require(inputSize < 30L * 1024 * 1024) { "file is too big. Maximum is about 20MB" }
    }

    companion object {
        const val defaultFormatName = "mirai"


        fun generateUUID(md5: ByteArray): String {
            return "${md5[0, 3]}-${md5[4, 5]}-${md5[6, 7]}-${md5[8, 9]}-${md5[10, 15]}"
        }

        fun generateImageId(md5: ByteArray): String {
            return """{${generateUUID(md5)}}.$defaultFormatName"""
        }
    }

    /*
     * ImgType:
     *  JPG:    1000
     *  PNG:    1001
     *  WEBP:   1002
     *  BMP:    1005
     *  GIG:    2000 // gig? gif?
     *  APNG:   2001
     *  SHARPP: 1004
     */


    override fun toString(): String = "[ExternalImage(${generateUUID(md5)})]"

    fun calculateImageResourceId(): String = generateImageId(md5)
}

/**
 * 将图片作为单独的消息发送给指定联系人
 */
@JvmSynthetic
suspend fun <C : Contact> ExternalImage.sendTo(contact: C): MessageReceipt<C> = when (contact) {
    is Group -> contact.uploadImage(this).sendTo(contact)
    is User -> contact.uploadImage(this).sendTo(contact)
    else -> error("unreachable")
}

/**
 * 上传图片并通过图片 ID 构造 [Image]
 * 这个函数可能需消耗一段时间
 *
 * @see contact 图片上传对象. 由于好友图片与群图片不通用, 上传时必须提供目标联系人
 */
@JvmSynthetic
suspend fun ExternalImage.upload(contact: Contact): OfflineImage = when (contact) {
    is Group -> contact.uploadImage(this)
    is User -> contact.uploadImage(this)
    else -> error("unreachable")
}

/**
 * 将图片作为单独的消息发送给 [this]
 */
@JvmSynthetic
suspend inline fun <C : Contact> C.sendImage(image: ExternalImage): MessageReceipt<C> = image.sendTo(this)

internal operator fun ByteArray.get(rangeStart: Int, rangeEnd: Int): String = buildString {
    for (it in rangeStart..rangeEnd) {
        append(this@get[it].fixToString())
    }
}

private fun Byte.fixToString(): String {
    return when (val b = this.toInt() and 0xff) {
        in 0..15 -> "0${this.toString(16).toUpperCase()}"
        else -> b.toString(16).toUpperCase()
    }
}