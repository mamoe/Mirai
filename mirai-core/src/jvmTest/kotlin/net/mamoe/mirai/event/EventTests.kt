/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.event

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.GlobalScope
import net.mamoe.mirai.utils.internal.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class TestEvent : Event {
    var triggered = false
}

class EventTests {
    @Test
    fun testSubscribeInplace() {
        runBlocking {
            val subscriber = subscribeAlways<TestEvent> {
                triggered = true
            }

            assertTrue(TestEvent().broadcast().triggered)
            subscriber.complete()
        }
    }

    @Test
    fun testSubscribeGlobalScope() {
        runBlocking {
            GlobalScope.subscribeAlways<TestEvent> {
                triggered = true
            }

            assertTrue(TestEvent().broadcast().triggered)
        }
    }


    open class ParentEvent : Event {
        var triggered = false
    }

    open class ChildEvent : ParentEvent()

    open class ChildChildEvent : ChildEvent()

    @Test
    fun `broadcast Child to Parent`() {
        runBlocking {
            val job: CompletableJob
            job = subscribeAlways<ParentEvent> {
                triggered = true
            }

            assertTrue(ChildEvent().broadcast().triggered)
            job.complete()
        }
    }

    @Test
    fun `broadcast ChildChild to Parent`() {
        runBlocking {
            val job: CompletableJob
            job = subscribeAlways<ParentEvent> {
                triggered = true
            }
            assertTrue(ChildChildEvent().broadcast().triggered)
            job.complete()
        }
    }
}