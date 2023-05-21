/*
 * Copyright 2019-2023 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.utils.crypto

import kotlinx.cinterop.*
import net.mamoe.mirai.internal.utils.free
import net.mamoe.mirai.internal.utils.getOpenSSLError
import openssl.*

private val aes256CBC by lazy { EVP_aes_256_cbc() }

internal actual fun aesEncrypt(input: ByteArray, iv: ByteArray, key: ByteArray): ByteArray {
    return doAES(input, iv, key, true)
}

internal actual fun aesDecrypt(input: ByteArray, iv: ByteArray, key: ByteArray): ByteArray {
    return doAES(input, iv, key, false)
}

/**
 * reference:
 *  - https://wiki.openssl.org/index.php/EVP_Symmetric_Encryption_and_Decryption
 */
private fun doAES(input: ByteArray, iv: ByteArray, key: ByteArray, doEncrypt: Boolean): ByteArray {
    memScoped {
        val evpCipherCtx = EVP_CIPHER_CTX_new() ?: error("Failed to create evp cipher context: ${getOpenSSLError()}")

        val pinnedKey = key.pin()
        val pinnedIv = iv.pin()
        val pinnedInput = input.pin()

        if (1 != EVP_CipherInit(
                ctx = evpCipherCtx,
                cipher = aes256CBC,
                key = pinnedKey.addressOf(0).reinterpret(),
                iv = pinnedIv.addressOf(0).reinterpret(),
                enc = if (doEncrypt) 1 else 0
            )
        ) {
            pinnedKey.unpin()
            pinnedIv.unpin()
            pinnedInput.unpin()
            EVP_CIPHER_CTX_free(evpCipherCtx)
            error("Failed to init aes-256-cbc cipher: ${getOpenSSLError()}")
        }

        pinnedKey.unpin()
        pinnedIv.unpin()

        val blockSize = _evpCipherCtxGetBlockSize(evpCipherCtx)
        val cipherBufferSize = pinnedInput.get().size + blockSize - (pinnedInput.get().size % blockSize)
        val pinnedCipherBuffer = ByteArray(cipherBufferSize.convert()).pin()


        val tempLen = alloc<IntVar>()
        val cipherSize = alloc<IntVar>()

        if (1 != EVP_CipherUpdate(
                ctx = evpCipherCtx,
                out = pinnedCipherBuffer.addressOf(0).reinterpret(),
                outl = tempLen.ptr,
                `in` = pinnedInput.addressOf(0).reinterpret(),
                inl = pinnedInput.get().size.convert()
            )
        ) {
            pinnedInput.unpin()
            pinnedCipherBuffer.unpin()
            free(tempLen.ptr, cipherSize.ptr)
            EVP_CIPHER_CTX_free(evpCipherCtx)
            error("Failed do aes-256-cbc cipher update: ${getOpenSSLError()}")
        }
        cipherSize.value = tempLen.value

        if (1 != EVP_CipherFinal(
                ctx = evpCipherCtx,
                outm = pinnedCipherBuffer.addressOf(tempLen.value).reinterpret(),
                outl = tempLen.ptr
            )
        ) {
            pinnedInput.unpin()
            pinnedCipherBuffer.unpin()
            free(tempLen.ptr, cipherSize.ptr)
            EVP_CIPHER_CTX_free(evpCipherCtx)
            error("Failed do aes-256-cbc cipher final: ${getOpenSSLError()}")
        }
        cipherSize.value += tempLen.value

        return pinnedCipherBuffer.get().copyOf(cipherSize.value).also {
            pinnedInput.unpin()
            pinnedCipherBuffer.unpin()
            EVP_CIPHER_CTX_free(evpCipherCtx)
        }
    }
}
