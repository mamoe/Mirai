package net.mamoe.mirai.console.graphical.model

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import net.mamoe.mirai.console.plugins.PluginDescription
import tornadofx.getValue
import tornadofx.setValue

class PluginModel(
    val name: String,
    val version: String,
    val author: String,
    val description: String
) : RecursiveTreeObject<PluginModel>() {
    constructor(plugin: PluginDescription):this(plugin.name, plugin.version, plugin.author, plugin.info)

    val enabledProperty = SimpleBooleanProperty(this, "enabledProperty")
    var enabled by enabledProperty
}