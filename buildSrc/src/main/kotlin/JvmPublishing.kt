/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import keys.SecretKeys
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.register

fun Project.configureRemoteRepos() {
    tasks.register("ensureMavenCentralAvailable") {
        doLast {
            if (GpgSigner.signer == GpgSigner.NoopSigner) {
                error("GPG Signer isn't available.")
            }
            val keys = SecretKeys.getCache(project)
            if (!keys.loadKey("sonatype").isValid) {
                error("Maven Central isn't available.")
            }
        }
    }

    publishing {
        // sonatype
        val keys = SecretKeys.getCache(project)
        repositories {
            if (System.getenv("MIRAI_IS_SNAPSHOTS_PUBLISHING")?.toBoolean() == true) {
                maven {
                    name = "MiraiRepo"
                    setUrl(System.getenv("SNAPSHOTS_PUBLISHING_URL"))

                    credentials {
                        username = System.getenv("SNAPSHOTS_PUBLISHING_USER")
                        password = System.getenv("SNAPSHOTS_PUBLISHING_KEY")
                    }
                }
            }

            val sonatype = keys.loadKey("sonatype")
            if (sonatype.isValid) {
                maven {
                    name = "MavenCentral"
                    // Maven Central
                    setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")

                    credentials {
                        username = sonatype.user
                        password = sonatype.password
                    }
                }
            } else {
                println("SonaType is not available")
            }
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Project.configurePublishing(
    artifactId: String,
    vcs: String = "https://github.com/mamoe/mirai",
    addProjectComponents: Boolean = true,
    setupGpg: Boolean = true,
) {
    configureRemoteRepos()

    val shadowJar = if (!addProjectComponents) null else tasks.register<ShadowJar>("shadowJar") {
        archiveClassifier.set("all")
        manifest.inheritFrom(tasks.getByName<Jar>("jar").manifest)
        from(project.sourceSets["main"].output)
        configurations = mutableListOf(
            project.configurations.findByName("runtimeClasspath") ?: project.configurations["runtime"]
        )
        exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
    }

    val sourcesJar = if (!addProjectComponents) null else tasks.register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    val stubJavadoc = if (!addProjectComponents) null else tasks.register<Jar>("javadocJar") {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                if (addProjectComponents) from(components["java"])

                groupId = rootProject.group.toString()
                setArtifactId(artifactId)
                version = project.version.toString()

                setupPom(
                    project = project,
                    vcs = vcs
                )

                sourcesJar?.let { artifact(it) }
                stubJavadoc?.get()?.let { artifact(it) }
                shadowJar?.get()?.let { artifact(it) }
            }
        }
        if (setupGpg) {
            configGpgSign(this@configurePublishing)
        }
    }
}