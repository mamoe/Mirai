/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("UnusedImport")

import BinaryCompatibilityConfigurator.configureBinaryValidator
import BinaryCompatibilityConfigurator.configureBinaryValidators
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.Instant

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("java")
    `maven-publish`
    id("me.him188.kotlin-jvm-blocking-bridge")
    id("me.him188.kotlin-dynamic-delegation")
}

version = Versions.console
description = "Mirai Console Backend"

kotlin {
    explicitApiWarning()
}

dependencies {
    compileAndTestRuntime(project(":mirai-core-api"))
    compileAndTestRuntime(project(":mirai-core-utils"))
    compileAndTestRuntime(`kotlin-stdlib-jdk8`)

    compileAndTestRuntime(`kotlinx-atomicfu-jvm`)
    compileAndTestRuntime(`kotlinx-coroutines-core-jvm`)
    compileAndTestRuntime(`kotlinx-serialization-core-jvm`)
    compileAndTestRuntime(`kotlinx-serialization-json-jvm`)
    compileAndTestRuntime(`kotlin-reflect`)

    implementation(project(":mirai-console-compiler-annotations"))

    smartImplementation(`yamlkt-jvm`)
    smartImplementation(`jetbrains-annotations`)
    smartImplementation(`caller-finder`)
    smartApi(`kotlinx-coroutines-jdk8`)

    testApi(project(":mirai-core"))
    testApi(`kotlin-stdlib-jdk8`)
}

tasks {
    val compileKotlin by getting {}

    register("fillBuildConstants") {
        group = "mirai"
        doLast {
            (compileKotlin as KotlinCompile).source.filter { it.name == "MiraiConsoleBuildConstants.kt" }.single()
                .let { file ->
                    file.writeText(
                        file.readText()
                            .replace(
                                Regex("""val buildDate: Instant = Instant.ofEpochSecond\(.*\)""")
                            ) {
                                """val buildDate: Instant = Instant.ofEpochSecond(${
                                    Instant.now().epochSecond
                                })"""
                            }
                            .replace(
                                Regex("""const val versionConst:\s+String\s+=\s+".*"""")
                            ) { """const val versionConst: String = "${project.version}"""" }
                    )
                }
        }
    }
}

configurePublishing("mirai-console")
configureBinaryValidator(null)