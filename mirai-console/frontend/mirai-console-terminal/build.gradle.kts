/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("java")
    `maven-publish`
}

dependencies {
    implementation("org.jline:jline:3.15.0")
    implementation("org.fusesource.jansi:jansi:1.18")

    compileAndTestRuntime(project(":mirai-console"))
    compileAndTestRuntime(`mirai-core-api`)
    compileAndTestRuntime(kotlin("stdlib-jdk8", Versions.kotlinStdlib)) // must specify `compileOnly` explicitly

    testApi(`mirai-core`)
    testApi(project(":mirai-console"))
}

version = Versions.consoleTerminal

description = "Console Terminal CLI frontend for mirai"

configurePublishing("mirai-console-terminal")

// endregion