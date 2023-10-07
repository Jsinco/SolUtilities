package me.jsinco.solutilities

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object Saves {
    private val filemanager = FileManager("data/saves.yml")
    private val yamlSaves = filemanager.getFileYaml()

    @JvmStatic
    fun setup() {
        filemanager.generateFolder("data")
        filemanager.generateFile()
    }

    @JvmStatic
    fun get(): YamlConfiguration {
        return yamlSaves
    }

    @JvmStatic
    fun save() {
        filemanager.saveFileYaml()
    }

    @JvmStatic
    fun reload() {
        filemanager.reloadFileYaml()
    }
}