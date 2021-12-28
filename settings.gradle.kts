/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

pluginManagement {
    repositories {
        if (System.getProperty("use.maven.local") == "true") {
            mavenLocal()
        }
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

rootProject.name = "mirai"

fun includeProject(projectPath: String, dir: String? = null) {
    include(projectPath)
    if (dir != null) project(projectPath).projectDir = file(dir)
}

include(":mirai-core-utils")
include(":mirai-core-api")
include(":mirai-core")
include(":mirai-core-all")
include(":mirai-bom")
include(":mirai-dokka")

include(":binary-compatibility-validator")
include(":binary-compatibility-validator-android")
project(":binary-compatibility-validator-android").projectDir = file("binary-compatibility-validator/android")

includeProject(":mirai-logging-log4j2", "logging/mirai-logging-log4j2")
includeProject(":mirai-logging-slf4j", "logging/mirai-logging-slf4j")
includeProject(":mirai-logging-slf4j-simple", "logging/mirai-logging-slf4j-simple")
includeProject(":mirai-logging-slf4j-logback", "logging/mirai-logging-slf4j-logback")


val disableOldFrontEnds = true

fun includeConsoleProject(projectPath: String, dir: String? = null) =
    includeProject(projectPath, "mirai-console/$dir")

includeConsoleProject(":mirai-console-compiler-annotations", "tools/compiler-annotations")
includeConsoleProject(":mirai-console", "backend/mirai-console")
includeConsoleProject(":mirai-console.codegen", "backend/codegen")
includeConsoleProject(":mirai-console-terminal", "frontend/mirai-console-terminal")

// region mirai-console.integration-test
includeConsoleProject(":mirai-console.integration-test", "backend/integration-test")

val consoleIntegrationTestSubPluginBuildGradleKtsTemplate by lazy {
    rootProject.projectDir
        .resolve("mirai-console/backend/integration-test/testers")
        .resolve("tester.template.gradle.kts")
        .readText()
}

@Suppress("SimpleRedundantLet")
fun includeConsoleITPlugin(path: File) {
    path.resolve("build.gradle.kts").takeIf { !it.isFile }?.let { initScript ->
        initScript.writeText(consoleIntegrationTestSubPluginBuildGradleKtsTemplate)
    }

    val projectPath = ":mirai-console.integration-test.tp.${path.name}"
    include(projectPath)
    project(projectPath).projectDir = path
}
rootProject.projectDir
    .resolve("mirai-console/backend/integration-test/testers")
    .listFiles()?.asSequence().orEmpty()
    .filter { it.isDirectory }
    .forEach { includeConsoleITPlugin(it) }
// endregion

includeConsoleProject(":mirai-console-compiler-common", "tools/compiler-common")
includeConsoleProject(":mirai-console-intellij", "tools/intellij-plugin")
includeConsoleProject(":mirai-console-gradle", "tools/gradle-plugin")

@Suppress("ConstantConditionIf")
if (!disableOldFrontEnds) {
    includeConsoleProject(":mirai-console-terminal", "frontend/mirai-console-terminal")

    println("JDK version: ${JavaVersion.current()}")

    if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
        includeConsoleProject(":mirai-console-graphical", "frontend/mirai-console-graphical")
    } else {
        println("当前使用的 JDK 版本为 ${System.getProperty("java.version")},  请使用 JDK 9 以上版本引入模块 `:mirai-console-graphical`\n")
    }
}

include(":ci-release-helper")