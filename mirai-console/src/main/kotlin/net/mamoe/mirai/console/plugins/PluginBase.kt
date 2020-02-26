/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.console.plugins

import kotlinx.coroutines.*
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.utils.DefaultLogger
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.SimpleLogger
import net.mamoe.mirai.utils.io.encodeToString
import java.io.File
import java.io.InputStream
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class PluginBase(coroutineContext: CoroutineContext) : CoroutineScope {
    constructor() : this(EmptyCoroutineContext)

    private val supervisorJob = SupervisorJob()
    final override val coroutineContext: CoroutineContext = coroutineContext + supervisorJob

    /**
     * 插件被分配的data folder， 如果插件改名了 data folder 也会变 请注意
     */
    val dataFolder: File by lazy {
        File(PluginManager.pluginsPath + pluginDescription.name).also { it.mkdir() }
    }

    /**
     * 当一个插件被加载时调用
     */
    open fun onLoad() {

    }

    /**
     * 当所有插件全部被加载后被调用
     */
    open fun onEnable() {

    }

    /**
     * 当插件关闭前被调用
     */
    open fun onDisable() {

    }

    /**
     * 当任意指令被使用
     */
    open fun onCommand(command: Command, args: List<String>) {

    }


    internal fun enable() {
        this.onEnable()
    }

    /**
     * 加载一个data folder中的Config
     * 这个config是read-write的
     */
    fun loadConfig(fileName: String): Config {
        return Config.load(dataFolder.absolutePath + fileName)
    }

    @JvmOverloads
    internal fun disable(throwable: CancellationException? = null) {
        this.coroutineContext[Job]!!.cancelChildren(throwable)
        this.onDisable()
    }

    private lateinit var pluginDescription: PluginDescription

    internal fun init(pluginDescription: PluginDescription) {
        this.pluginDescription = pluginDescription
        this.onLoad()
    }

    val pluginManager = PluginManager

    val logger: MiraiLogger by lazy {
        SimpleLogger("Plugin ${pluginDescription.name}") { _, message, e ->
            MiraiConsole.logger("[${pluginDescription.name}]", 0, message)
            if (e != null) {
                MiraiConsole.logger("[${pluginDescription.name}]", 0, e.toString())
                e.printStackTrace()
            }
        }
    }

    /**
     * 加载一个插件jar, resources中的东西
     */
    fun getResources(fileName: String): InputStream? {
        return try {
            this.javaClass.classLoader.getResourceAsStream(fileName)
        } catch (e: Exception) {
            PluginManager.getFileInJarByName(
                this.pluginDescription.name,
                fileName
            )
        }
    }

    /**
     * 加载一个插件jar, resources中的Config
     * 这个Config是read-only的
     */
    fun getResourcesConfig(fileName: String): Config {
        if (fileName.contains(".")) {
            error("Unknown Config Type")
        }
        return Config.load(getResources(fileName) ?: error("Config Not Found"), fileName.split(".")[1])
    }

}

class PluginDescription(
    val name: String,
    val author: String,
    val basePath: String,
    val version: String,
    val info: String,
    val depends: List<String>,//插件的依赖
    internal var loaded: Boolean = false,
    internal var noCircularDepend: Boolean = true
) {

    override fun toString(): String {
        return "name: $name\nauthor: $author\npath: $basePath\nver: $version\ninfo: $info\ndepends: $depends"
    }

    companion object {
        fun readFromContent(content_: String): PluginDescription {
            val content = content_.split("\n")

            var name = "Plugin"
            var author = "Unknown"
            var basePath = "net.mamoe.mirai.PluginMain"
            var info = "Unknown"
            var version = "1.0.0"
            val depends = mutableListOf<String>();

            content.forEach {
                val line = it.trim()
                val lowercaseLine = line.toLowerCase()
                if (it.contains(":")) {
                    when {
                        lowercaseLine.startsWith("name") -> {
                            name = line.substringAfter(":").trim()
                        }
                        lowercaseLine.startsWith("author") -> {
                            author = line.substringAfter(":").trim()
                        }
                        lowercaseLine.startsWith("info") || lowercaseLine.startsWith("information") -> {
                            info = line.substringAfter(":").trim()
                        }
                        lowercaseLine.startsWith("main") || lowercaseLine.startsWith("path") || lowercaseLine.startsWith(
                            "basepath"
                        ) -> {
                            basePath = line.substringAfter(":").trim()
                        }
                        lowercaseLine.startsWith("version") || lowercaseLine.startsWith("ver") -> {
                            version = line.substringAfter(":").trim()
                        }
                    }
                } else if (line.startsWith("-")) {
                    depends.add(line.substringAfter("-").trim())
                }
            }
            return PluginDescription(
                name,
                author,
                basePath,
                version,
                info,
                depends
            )
        }
    }
}

internal class PluginClassLoader(file: File, parent: ClassLoader) : URLClassLoader(arrayOf(file.toURI().toURL()), parent)

object PluginManager {
    internal val pluginsPath = System.getProperty("user.dir") + "/plugins/".replace("//", "/").also {
        File(it).mkdirs()
    }

    val logger = SimpleLogger("Plugin Manager") { _, message, e ->
        MiraiConsole.logger("[Plugin Manager]", 0, message)
    }

    //已完成加载的
    private val nameToPluginBaseMap: MutableMap<String, PluginBase> = mutableMapOf()
    private val pluginDescriptions: MutableMap<String, PluginDescription> = mutableMapOf()

    fun onCommand(command: Command, args: List<String>) {
        nameToPluginBaseMap.values.forEach {
            it.onCommand(command, args)
        }
    }

    fun getAllPluginDescriptions(): Collection<PluginDescription> {
        return pluginDescriptions.values
    }

    /**
     * 尝试加载全部插件
     */
    fun loadPlugins() {
        val pluginsFound: MutableMap<String, PluginDescription> = mutableMapOf()
        val pluginsLocation: MutableMap<String, File> = mutableMapOf()

        logger.info("""开始加载${pluginsPath}下的插件""")

        File(pluginsPath).listFiles()?.forEach { file ->
            if (file != null && file.extension == "jar") {
                val jar = JarFile(file)
                val pluginYml =
                    jar.entries().asSequence().filter { it.name.toLowerCase().contains("plugin.yml") }.firstOrNull()
                if (pluginYml == null) {
                    logger.info("plugin.yml not found in jar " + jar.name + ", it will not be consider as a Plugin")
                } else {
                    val description =
                        PluginDescription.readFromContent(
                            URL("jar:file:" + file.absoluteFile + "!/" + pluginYml.name).openConnection().inputStream.use {
                                it.readBytes().encodeToString()
                            })
                    pluginsFound[description.name] = description
                    pluginsLocation[description.name] = file
                }
            }
        }

        fun checkNoCircularDepends(
            target: PluginDescription,
            needDepends: List<String>,
            existDepends: MutableList<String>
        ) {

            if (!target.noCircularDepend) {
                return
            }

            existDepends.add(target.name)

            if (needDepends.any { existDepends.contains(it) }) {
                target.noCircularDepend = false
            }

            existDepends.addAll(needDepends)

            needDepends.forEach {
                if (pluginsFound.containsKey(it)) {
                    checkNoCircularDepends(pluginsFound[it]!!, pluginsFound[it]!!.depends, existDepends)
                }
            }
        }


        pluginsFound.values.forEach {
            checkNoCircularDepends(it, it.depends, mutableListOf())
        }

        //load


        fun loadPlugin(description: PluginDescription): Boolean {
            if (!description.noCircularDepend) {
                logger.error("Failed to load plugin " + description.name + " because it has circular dependency")
                return false
            }

            //load depends first
            description.depends.forEach { dependent ->
                if (!pluginsFound.containsKey(dependent)) {
                    logger.error("Failed to load plugin " + description.name + " because it need " + dependent + " as dependency")
                    return false
                }
                val depend = pluginsFound[dependent]!!
                //还没有加载
                if (!depend.loaded && !loadPlugin(pluginsFound[dependent]!!)) {
                    logger.error("Failed to load plugin " + description.name + " because " + dependent + " as dependency failed to load")
                    return false
                }
            }
            //在这里所有的depends都已经加载了


            //real load
            logger.info("loading plugin " + description.name)

            try {
                val pluginClass = try {
                    PluginClassLoader(
                        (pluginsLocation[description.name]!!),
                        this.javaClass.classLoader
                    )
                        .loadClass(description.basePath)
                } catch (e: ClassNotFoundException) {
                    logger.info("failed to find Main: " + description.basePath + " checking if it's kotlin's path")
                    PluginClassLoader(
                        (pluginsLocation[description.name]!!),
                        this.javaClass.classLoader
                    )
                        .loadClass("${description.basePath}Kt")
                }
                return try {
                    val subClass = pluginClass.asSubclass(PluginBase::class.java)
                    val plugin: PluginBase = subClass.kotlin.objectInstance ?: subClass.getDeclaredConstructor().newInstance()
                    description.loaded = true
                    logger.info("successfully loaded plugin " + description.name + " version " + description.version + " by " + description.author)
                    logger.info(description.info)

                    nameToPluginBaseMap[description.name] = plugin
                    pluginDescriptions[description.name] = description
                    plugin.init(description)
                    true
                } catch (e: ClassCastException) {
                    logger.error("failed to load plugin " + description.name + " , Main class does not extends PluginBase ")
                    false
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                logger.error("failed to load plugin " + description.name + " , Main class not found under " + description.basePath)
                return false
            }
        }

        pluginsFound.values.forEach {
            loadPlugin(it)
        }

        nameToPluginBaseMap.values.forEach {
            it.enable()
        }

        logger.info("""加载了${nameToPluginBaseMap.size}个插件""")

    }


    @JvmOverloads
    fun disableAllPlugins(throwable: CancellationException? = null) {
        nameToPluginBaseMap.values.forEach {
            it.disable(throwable)
        }
    }

    /**
     * 根据插件名字找Jar的文件
     * null => 没找到
     */
    fun getJarPath(pluginName: String): File? {
        File(pluginsPath).listFiles()?.forEach { file ->
            if (file != null && file.extension == "jar") {
                val jar = JarFile(file)
                val pluginYml =
                    jar.entries().asSequence().filter { it.name.toLowerCase().contains("plugin.yml") }.firstOrNull()
                if (pluginYml != null) {
                    val description =
                        PluginDescription.readFromContent(
                            URL("jar:file:" + file.absoluteFile + "!/" + pluginYml.name).openConnection().inputStream.use {
                                it.readBytes().encodeToString()
                            })
                    if (description.name.toLowerCase() == pluginName.toLowerCase()) {
                        return file
                    }
                }
            }
        }
        return null
    }


    /**
     * 根据插件名字找Jar中的文件
     * null => 没找到
     */
    fun getFileInJarByName(pluginName: String, toFind: String): InputStream? {
        val jarFile = getJarPath(pluginName)
        if (jarFile == null) {
            return null
        }
        val jar = JarFile(jarFile)
        val toFindFile =
            jar.entries().asSequence().filter { it.name == toFind }.firstOrNull() ?: return null
        return URL("jar:file:" + jarFile.absoluteFile + "!/" + toFindFile.name).openConnection().inputStream
    }
}



