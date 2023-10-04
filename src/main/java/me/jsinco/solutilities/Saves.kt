package me.jsinco.solutilities

import org.bukkit.configuration.file.YamlConfiguration

object Saves {
    private val filemanager = FileManager("Saves.yml")
    private val yamlSaves = filemanager.getFileYaml()

    @JvmStatic
    fun setup() {
        filemanager.setFolder("data")
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