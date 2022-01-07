/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.console.framework.test

import net.mamoe.mirai.console.testFramework.AbstractConsoleTest
import net.mamoe.mirai.console.plugin.PluginManager
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FrameworkTest : AbstractConsoleTest() {

    @Test
    fun testConsole1() {
        assertEquals(0, PluginManager.plugins.size)
    }

    @Test
    fun testConsole2() {
        assertEquals(0, PluginManager.plugins.size)
    }
}