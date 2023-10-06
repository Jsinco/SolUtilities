package me.jsinco.solutilities.celestial

import me.jsinco.solutilities.FileManager
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object CelestialFile {
    private val filemanager = FileManager("data/celestial.yml")
    private val celesteFile = filemanager.getFileYaml()
    @JvmStatic
    fun setup() {
        filemanager.generateFolder("data")
        filemanager.generateFile()
    }

    @JvmStatic
    fun get(): YamlConfiguration {
        return celesteFile
    }

    @JvmStatic
    fun save() {
        filemanager.saveFileYaml()
    }

    fun reload() {
        filemanager.reloadFileYaml()
    }
}
