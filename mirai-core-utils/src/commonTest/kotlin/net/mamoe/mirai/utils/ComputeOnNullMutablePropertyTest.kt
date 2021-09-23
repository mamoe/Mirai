/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.utils

import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ComputeOnNullMutablePropertyTest {
    @Test
    fun `can initialize`() {
        val prop = computeOnNullMutableProperty { "ok" }
        assertEquals("ok", prop.get())
    }

    @Test
    fun `can override`() {
        val called = AtomicBoolean(false)
        val prop = computeOnNullMutableProperty { "not ok".also { called.set(true) } }
        prop.set("ok")
        assertEquals("ok", prop.get())
        assertFalse { called.get() }
    }

    @Test
    fun `can reinitialize 1`() {
        val called = AtomicBoolean(false)
        val prop = computeOnNullMutableProperty { "ok".also { called.set(true) } }
        prop.set("not ok 2")
        prop.set(null)
        assertEquals("ok", prop.get())
        assertTrue { called.get() }
    }

    @Test
    fun `can reinitialize 2`() {
        val prop = computeOnNullMutableProperty { "ok" }
        prop.get()
        prop.set(null)
        assertEquals("ok", prop.get())
    }
}