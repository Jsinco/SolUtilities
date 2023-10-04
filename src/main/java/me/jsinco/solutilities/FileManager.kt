package me.jsinco.solutilities

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.nio.file.Files

class FileManager(val fileName: String) {
    val plugin: SolUtilities = SolUtilities.getPlugin()

    var file: File = File(plugin.dataFolder, fileName)
    private val yamlConfiguration: YamlConfiguration = YamlConfiguration.loadConfiguration(file)

    fun setFolder(folder: String) {
        file = File(plugin.dataFolder, "$folder${File.pathSeparator}$fileName")
    }


    fun generateFile(): Boolean {
        var returnValue = false
        try {
            if (!file.exists()) {
                file.createNewFile()
                returnValue = true
            }
            if (plugin.getResource(file.name) != null) {
                val inputStream = plugin.getResource(file.name)
                val outputStream = Files.newOutputStream(file.toPath())
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                inputStream.close()
                outputStream.flush()
                outputStream.close()
                returnValue = true
            }
        } catch (ex: IOException) {
            plugin.logger.warning("Couldnt save file: ${file.name} \n $ex")
            returnValue = false
        }
        return returnValue
    }

    fun getFileYaml(): YamlConfiguration {
        return yamlConfiguration
    }

    fun saveFileYaml() {
        yamlConfiguration.save(file)
    }

    fun reloadFileYaml() {
        yamlConfiguration.load(file)
    }

}