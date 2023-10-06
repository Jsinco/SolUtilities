package me.jsinco.solutilities.celestial.aries.itemprofler

import me.jsinco.solutilities.FileManager
import org.bukkit.configuration.file.YamlConfiguration

object ItemProfilerFile {
    private val fileManager: FileManager = FileManager("ItemProfiles.yml")

    @JvmStatic
    fun setup() {
        fileManager.generateFile()
    }

    @JvmStatic
    fun get(): YamlConfiguration {
        return fileManager.getFileYaml()
    }
}