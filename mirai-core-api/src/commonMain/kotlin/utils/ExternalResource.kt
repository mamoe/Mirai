/*
 * Copyright 2019-2020 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("EXPERIMENTAL_API_USAGE", "unused")

package net.mamoe.mirai.utils

import net.mamoe.kjbb.JvmBlockingBridge
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.ExternalResource.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.*


/**
 * 一个*不可变的*外部资源.
 *
 * [ExternalResource] 在创建之后就应该保持其属性的不变, 即任何时候获取其属性都应该得到相同结果, 任何时候打开流都得到的一样的数据.
 *
 * ## 创建
 * - [File.toExternalResource]
 * - [RandomAccessFile.toExternalResource]
 * - [ByteArray.toExternalResource]
 * - [InputStream.toExternalResource]
 *
 * ## 释放
 *
 * 当 [ExternalResource] 创建时就可能会打开个文件 (如使用 [File.toExternalResource]).
 * 类似于 [InputStream], [ExternalResource] 需要被 [关闭][close].
 *
 * @see ExternalResource.uploadAsImage 将资源作为图片上传, 得到 [Image]
 * @see ExternalResource.sendAsImageTo 将资源作为图片发送
 * @see Contact.uploadImage 上传一个资源作为图片, 得到 [Image]
 * @see Contact.sendImage 发送一个资源作为图片
 *
 * @see FileCacheStrategy
 */
public interface ExternalResource : Closeable {

    /**
     * 文件内容 MD5. 16 bytes
     */
    public val md5: ByteArray

    /**
     * 文件格式，如 "png", "amr". 当无法自动识别格式时为 "mirai"
     */
    public val formatName: String

    /**
     * 文件大小 bytes
     */
    public val size: Long

    /**
     * 打开 [InputStream]. 在返回的 [InputStream] 被 [关闭][InputStream.close] 前无法再次打开流.
     *
     * 关闭此流不会关闭 [ExternalResource].
     */
    public fun inputStream(): InputStream

    @MiraiInternalApi
    public fun calculateResourceId(): String {
        return generateImageId(md5, formatName.ifEmpty { "mirai" })
    }

    public companion object {
        /**
         * 在无法识别文件格式时使用的默认格式名.
         *
         * @see ExternalResource.formatName
         */
        public const val DEFAULT_FORMAT_NAME: String = "mirai"

        /**
         * **打开文件**并创建 [ExternalResource].
         *
         * 将以只读模式打开这个文件 (因此文件会处于被占用状态), 直到 [ExternalResource.close].
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        public fun File.toExternalResource(formatName: String? = null): ExternalResource =
            RandomAccessFile(this, "r").toExternalResource(formatName)

        /**
         * 创建 [ExternalResource].
         *
         * @see closeOriginalFileOnClose 若为 `true`, 在 [ExternalResource.close] 时将会同步关闭 [RandomAccessFile]. 否则不会.
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        public fun RandomAccessFile.toExternalResource(
            formatName: String? = null,
            closeOriginalFileOnClose: Boolean = true
        ): ExternalResource =
            ExternalResourceImplByFile(this, formatName, closeOriginalFileOnClose)

        /**
         * 创建 [ExternalResource]
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        public fun ByteArray.toExternalResource(formatName: String? = null): ExternalResource =
            ExternalResourceImplByByteArray(this, formatName)


        /**
         * 立即使用 [FileCacheStrategy] 缓存 [InputStream] 并创建 [ExternalResource].
         *
         * 注意：本函数不会关闭流
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        @Throws(IOException::class)
        public fun InputStream.toExternalResource(formatName: String? = null): ExternalResource =
            Mirai.FileCacheStrategy.newCache(this, formatName)


        /**
         * 将图片作为单独的消息发送给指定联系人.
         *
         * 注意：本函数不会关闭 [ExternalResource]
         *
         *
         * @see Contact.uploadImage 上传图片
         * @see Contact.sendMessage 最终调用, 发送消息.
         */
        @JvmBlockingBridge
        @JvmStatic
        @JvmName("sendAsImage")
        public suspend fun <C : Contact> ExternalResource.sendAsImageTo(contact: C): MessageReceipt<C> =
            when (contact) {
                is Group -> contact.uploadImage(this).sendTo(contact)
                is User -> contact.uploadImage(this).sendTo(contact)
                else -> error("unreachable")
            }

        /**
         * 上传图片并构造 [Image].
         * 这个函数可能需消耗一段时间.
         *
         * 注意：本函数不会关闭 [ExternalResource]
         *
         * @param contact 图片上传对象. 由于好友图片与群图片不通用, 上传时必须提供目标联系人
         *
         * @see Contact.uploadImage 最终调用, 上传图片.
         */
        @JvmBlockingBridge
        @JvmStatic
        public suspend fun ExternalResource.uploadAsImage(contact: Contact): Image = when (contact) {
            is Group -> contact.uploadImage(this)
            is User -> contact.uploadImage(this)
            else -> error("unreachable")
        }

        /**
         * 将图片作为单独的消息发送给 [this]
         *
         * @see Contact.sendMessage 最终调用, 发送消息.
         */
        @JvmSynthetic
        public suspend inline fun <C : Contact> C.sendImage(image: ExternalResource): MessageReceipt<C> =
            image.sendAsImageTo(this)
    }
}


private fun InputStream.detectFileTypeAndClose(): String? {
    val buffer = ByteArray(8)
    return use {
        kotlin.runCatching { it.read(buffer) }.onFailure { return null }
        getFileType(buffer)
    }
}

internal class ExternalResourceImplByFileWithMd5(
    private val file: RandomAccessFile,
    override val md5: ByteArray,
    formatName: String?
) : ExternalResource {
    override val size: Long = file.length()
    override val formatName: String by lazy {
        formatName ?: inputStream().detectFileTypeAndClose().orEmpty()
    }

    override fun inputStream(): InputStream {
        check(file.filePointer == 0L) { "RandomAccessFile.inputStream cannot be opened simultaneously." }
        return file.inputStream()
    }

    override fun close() {
        file.close()
    }
}

internal class ExternalResourceImplByFile(
    private val file: RandomAccessFile,
    formatName: String?,
    private val closeOriginalFileOnClose: Boolean = true
) : ExternalResource {
    override val size: Long = file.length()
    override val md5: ByteArray by lazy { inputStream().md5() }
    override val formatName: String by lazy {
        formatName ?: inputStream().detectFileTypeAndClose().orEmpty()
    }

    override fun inputStream(): InputStream {
        check(file.filePointer == 0L) { "RandomAccessFile.inputStream cannot be opened simultaneously." }
        return file.inputStream()
    }

    override fun close() {
        if (closeOriginalFileOnClose) file.close()
    }
}

internal class ExternalResourceImplByByteArray(
    private val data: ByteArray,
    formatName: String?
) : ExternalResource {
    override val size: Long = data.size.toLong()
    override val md5: ByteArray by lazy { data.md5() }
    override val formatName: String by lazy {
        formatName ?: getFileType(data.copyOf(8)).orEmpty()
    }

    override fun inputStream(): InputStream = data.inputStream()
    override fun close() {}
}

private fun RandomAccessFile.inputStream(): InputStream {
    val file = this
    return object : InputStream() {
        override fun read(): Int = file.read()
        override fun read(b: ByteArray, off: Int, len: Int): Int = file.read(b, off, len)
        override fun close() {
            file.seek(0)
        }
        // don't close file on stream.close. stream may be obtained at multiple times.
    }.buffered()
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
