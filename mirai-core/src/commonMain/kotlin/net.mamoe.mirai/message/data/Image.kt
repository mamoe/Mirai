/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:JvmMultifileClass
@file:JvmName("MessageUtils")

@file:Suppress("EXPERIMENTAL_API_USAGE")

package net.mamoe.mirai.message.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.mamoe.mirai.utils.io.chunkedHexToBytes
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * 自定义表情 (收藏的表情), 图片
 */
sealed class Image : Message, MessageContent {
    companion object Key : Message.Key<Image> {
        @JvmStatic
        @JsName("fromId")
        @JvmName("fromId")
        operator fun invoke(imageId: String): Image = when (imageId.length) {
            37 -> NotOnlineImageFromFile(imageId) // /f8f1ab55-bf8e-4236-b55e-955848d7069f
            42 -> CustomFaceFromFile(imageId) // {01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.png
            else -> throw IllegalArgumentException("Bad imageId, expecting length=37 or 42, got ${imageId.length}")
        }
    }

    /**
     * 图片的 id. 只需要有这个 id 即可发送图片.
     *
     * 示例:
     * 好友图片的 id: `/f8f1ab55-bf8e-4236-b55e-955848d7069f`
     * 群图片的 id: `{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.png`
     */
    abstract val imageId: String

    final override fun toString(): String {
        return "[mirai:$imageId]"
    }
}

abstract class CustomFace : Image() {
    abstract val filepath: String
    abstract val fileId: Int
    abstract val serverIp: Int
    abstract val serverPort: Int
    abstract val fileType: Int
    abstract val signature: ByteArray
    abstract val useful: Int
    abstract val md5: ByteArray
    abstract val bizType: Int
    abstract val imageType: Int
    abstract val width: Int
    abstract val height: Int
    abstract val source: Int
    abstract val size: Int
    abstract val pbReserve: ByteArray
    abstract val original: Int
}

private val EMPTY_BYTE_ARRAY = ByteArray(0)

private fun calculateImageMd5ByImageId(imageId: String): ByteArray {
    return if (imageId.startsWith('/')) {
        imageId
            .drop(1)
            .replace("-", "")
            .take(16 * 2)
            .chunkedHexToBytes()
    } else {
        imageId
            .substringAfter('{')
            .substringBefore('}')
            .replace("-", "")
            .take(16 * 2)
            .chunkedHexToBytes()
    }
}

@Serializable
data class CustomFaceFromFile(
    override val filepath: String, // {01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.png
    override val md5: ByteArray
) : CustomFace() {
    constructor(imageId: String) : this(filepath = imageId, md5 = calculateImageMd5ByImageId(imageId))

    override val fileId: Int get() = 0
    override val serverIp: Int get() = 0
    override val serverPort: Int get() = 0
    override val fileType: Int get() = 0 // 0
    override val signature: ByteArray get() = EMPTY_BYTE_ARRAY
    override val useful: Int get() = 1
    override val bizType: Int get() = 0
    override val imageType: Int get() = 0
    override val width: Int get() = 0
    override val height: Int get() = 0
    override val source: Int get() = 200
    override val size: Int get() = 0
    override val original: Int get() = 1
    override val pbReserve: ByteArray get() = EMPTY_BYTE_ARRAY
    override val imageId: String get() = filepath

    override fun hashCode(): Int {
        return filepath.hashCode() + 31 * md5.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is CustomFaceFromFile && other.md5.contentEquals(this.md5) && other.filepath == this.filepath
    }
}

/**
 * 电脑可能看不到这个消息.
 */
abstract class NotOnlineImage : Image() {
    abstract val resourceId: String
    abstract val md5: ByteArray
    abstract val filepath: String
    abstract val fileLength: Int
    abstract val height: Int
    abstract val width: Int
    open val bizType: Int get() = 0
    open val imageType: Int get() = 1000
    abstract val fileId: Int
    open val downloadPath: String get() = resourceId
    open val original: Int get() = 1

    override val imageId: String get() = resourceId
}

data class NotOnlineImageFromFile(
    override val resourceId: String,
    override val md5: ByteArray,
    @Transient override val filepath: String = resourceId,
    @Transient override val fileLength: Int = 0,
    @Transient override val height: Int = 0,
    @Transient override val width: Int = 0,
    @Transient override val bizType: Int = 0,
    @Transient override val imageType: Int = 1000,
    @Transient override val downloadPath: String = resourceId,
    @Transient override val fileId: Int = 0
) : NotOnlineImage() {
    constructor(imageId: String) : this(resourceId = imageId, md5 = calculateImageMd5ByImageId(imageId))

    override fun hashCode(): Int {
        return resourceId.hashCode() + 31 * md5.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is NotOnlineImageFromFile && other.md5.contentEquals(this.md5) && other.resourceId == this.resourceId
    }
}