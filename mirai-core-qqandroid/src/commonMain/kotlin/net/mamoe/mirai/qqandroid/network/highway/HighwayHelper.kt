/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.qqandroid.network.highway

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.content.OutgoingContent
import io.ktor.http.userAgent
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.InputStream
import kotlinx.io.core.Input
import kotlinx.io.core.discardExact
import kotlinx.io.core.readAvailable
import kotlinx.io.core.use
import kotlinx.serialization.InternalSerializationApi
import net.mamoe.mirai.qqandroid.QQAndroidBot
import net.mamoe.mirai.qqandroid.network.QQAndroidClient
import net.mamoe.mirai.qqandroid.network.protocol.data.proto.CSDataHighwayHead
import net.mamoe.mirai.qqandroid.utils.*
import net.mamoe.mirai.qqandroid.utils.io.serialization.readProtoBuf
import net.mamoe.mirai.qqandroid.utils.io.withUse
import net.mamoe.mirai.utils.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(MiraiInternalAPI::class, InternalSerializationApi::class)
@Suppress("SpellCheckingInspection")
internal suspend fun HttpClient.postImage(
    htcmd: String,
    uin: Long,
    groupcode: Long?,
    imageInput: Any, // Input from kotlinx.io, InputStream from kotlinx.io MPP, ByteReadChannel from ktor
    inputSize: Long,
    uKeyHex: String
): Boolean = post<HttpStatusCode> {
    url {
        protocol = URLProtocol.HTTP
        host = "htdata2.qq.com"
        path("cgi-bin/httpconn")

        parameters["htcmd"] = htcmd
        parameters["uin"] = uin.toString()

        if (groupcode != null) parameters["groupcode"] = groupcode.toString()

        parameters["term"] = "pc"
        parameters["ver"] = "5603"
        parameters["filesize"] = inputSize.toString()
        parameters["range"] = 0.toString()
        parameters["ukey"] = uKeyHex

        userAgent("QQClient")
    }

    body = object : OutgoingContent.WriteChannelContent() {
        override val contentType: ContentType = ContentType.Image.Any
        override val contentLength: Long = inputSize

        @OptIn(MiraiExperimentalAPI::class)
        override suspend fun writeTo(channel: ByteWriteChannel) {
            ByteArrayPool.useInstance { buffer: ByteArray ->
                when (imageInput) {
                    is Input -> {
                        var size: Int
                        while (imageInput.readAvailable(buffer).also { size = it } > 0) {
                            channel.writeFully(buffer, 0, size)
                            channel.flush()
                        }
                    }
                    is ByteReadChannel -> imageInput.copyAndClose(channel)
                    is InputStream -> {
                        var size: Int
                        while (imageInput.read(buffer).also { size = it } > 0) {
                            channel.writeFully(buffer, 0, size)
                            channel.flush()
                        }
                    }
                    else -> error("unsupported imageInput: ${imageInput::class.simpleName}")
                }
            }
        }
    }
} == HttpStatusCode.OK

@OptIn(MiraiInternalAPI::class, InternalSerializationApi::class)
internal object HighwayHelper {
    suspend fun uploadImageToServers(
        bot: QQAndroidBot,
        servers: List<Pair<Int, Int>>,
        uKey: ByteArray,
        image: ExternalImage,
        kind: String,
        commandId: Int
    ) = uploadImageToServers(bot, servers, uKey, image.md5, image.input, image.inputSize, kind, commandId)

    @OptIn(ExperimentalTime::class)
    suspend fun uploadImageToServers(
        bot: QQAndroidBot,
        servers: List<Pair<Int, Int>>,
        uKey: ByteArray,
        md5: ByteArray,
        input: Any,
        inputSize: Long,
        kind: String,
        commandId: Int
    ) = servers.retryWithServers(
        (inputSize * 1000 / 1024 / 10).coerceAtLeast(5000),
        onFail = {
            throw IllegalStateException("cannot upload $kind, failed on all servers.", it)
        }
    ) { ip, port ->
        bot.network.logger.verbose {
            "[Highway] Uploading $kind to ${ip}:$port, size=${inputSize / 1024} KiB"
        }

        val time = measureTime {
            uploadImage(
                client = bot.client,
                serverIp = ip,
                serverPort = port,
                imageInput = input,
                inputSize = inputSize.toInt(),
                fileMd5 = md5,
                ticket = uKey,
                commandId = commandId
            )
        }

        bot.network.logger.verbose {
            "[Highway] Uploading $kind: succeed at ${(inputSize.toDouble() / 1024 / time.inSeconds).roundToInt()} KiB/s"
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    internal suspend fun uploadImage(
        client: QQAndroidClient,
        serverIp: String,
        serverPort: Int,
        ticket: ByteArray,
        imageInput: Any,
        inputSize: Int,
        fileMd5: ByteArray,
        commandId: Int  // group=2, friend=1
    ) {
        require(imageInput is Input || imageInput is InputStream || imageInput is ByteReadChannel) { "unsupported imageInput: ${imageInput::class.simpleName}" }
        require(fileMd5.size == 16) { "bad md5. Required size=16, got ${fileMd5.size}" }
        //  require(ticket.size == 128) { "bad uKey. Required size=128, got ${ticket.size}" }
        // require(commandId == 2 || commandId == 1) { "bad commandId. Must be 1 or 2" }

        val socket = PlatformSocket()
        while (client.bot.network.isActive) {
            try {
                socket.connect(EmptyCoroutineContext, serverIp, serverPort)
                break
            } catch (e: SocketException) {
                delay(3000)
            }
        }
        socket.use {
            createImageDataPacketSequence(
                client = client,
                command = "PicUp.DataUp",
                commandId = commandId,
                ticket = ticket,
                data = imageInput,
                dataSize = inputSize,
                fileMd5 = fileMd5
            ).collect {
                socket.send(it)
                //0A 3C 08 01 12 0A 31 39 39 34 37 30 31 30 32 31 1A 0C 50 69 63 55 70 2E 44 61 74 61 55 70 20 E9 A7 05 28 00 30 BD DB 8B 80 02 38 80 20 40 02 4A 0A 38 2E 32 2E 30 2E 31 32 39 36 50 84 10 12 3D 08 00 10 FD 08 18 00 20 FD 08 28 C6 01 38 00 42 10 D4 1D 8C D9 8F 00 B2 04 E9 80 09 98 EC F8 42 7E 4A 10 D4 1D 8C D9 8F 00 B2 04 E9 80 09 98 EC F8 42 7E 50 89 92 A2 FB 06 58 00 60 00 18 53 20 01 28 00 30 04 3A 00 40 E6 B7 F7 D9 80 2E 48 00 50 00

                socket.read().withUse {
                    discardExact(1)
                    val headLength = readInt()
                    discardExact(4)
                    val proto = readProtoBuf(CSDataHighwayHead.RspDataHighwayHead.serializer(), length = headLength)
                    check(proto.errorCode == 0) { "highway transfer failed, error ${proto.errorCode}" }
                }
            }
        }
    }
}


internal suspend inline fun List<Pair<Int, Int>>.retryWithServers(
    timeoutMillis: Long,
    onFail: (exception: Throwable?) -> Unit,
    crossinline block: suspend (ip: String, port: Int) -> Unit
) {
    require(this.isNotEmpty()) { "receiver of retryWithServers must not be empty" }

    var exception: Throwable? = null
    for (pair in this) {
        return kotlin.runCatching {
            withTimeoutOrNull(timeoutMillis) {
                block(pair.first.toIpV4AddressString(), pair.second)
            }
        }.recover {
            if (exception != null) {
                exception!!.addSuppressedMirai(it)
            }
            exception = it
            null
        }.getOrNull() ?: continue
    }

    onFail(exception)
}