package net.mamoe.mirai.console.command

import net.mamoe.mirai.console.command.descriptor.CommandSignatureFromKFunction
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.compiler.common.ResolveContext
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

public interface SubCommandGroup {

    /**
     * 被聚合时提供的子指令
     */
    @ExperimentalCommandDescriptors
    public val overloads: List<@JvmWildcard CommandSignatureFromKFunction>

    /**
     * 标记一个属性为子指令集合，且使用flat策略
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.PROPERTY)
    public annotation class FlattenSubCommands(
    )

}