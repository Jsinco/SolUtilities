package me.jsinco.solutilities

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.CommandSender

object Util {

    lateinit var prefix: String
    lateinit var MATERIALS_STRING: MutableList<String>
    fun loadUtils() {
        prefix = colorcode(SolUtilities.getPlugin().config.getString("prefix")!!)
        for (material in Material.entries) {
            MATERIALS_STRING.add(material.name.lowercase())
        }
    }

    const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    @JvmStatic
    fun colorcode(text: String): String {
        val texts = text.split(String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equals("&", ignoreCase = true)) {
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7))
                } else {
                    finalText.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }


    @JvmStatic
    fun colorArrayList(list: List<String>): List<String>? {
        val coloredList: MutableList<String> = ArrayList()
        for (string in list) {
            coloredList.add(colorcode(string))
        }
        return coloredList
    }

    @JvmStatic
    fun colorArray(array: Array<String>): Array<String>? {
        for (i in array.indices) {
            array[i] = colorcode(array[i])
        }
        return array
    }


    fun playServerSound(sound: Sound, volume: Float, pitch: Float) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.scoreboardTags.contains("solutilities.silent")) {
                player.playSound(player.location, sound, volume, pitch)
            }
        }
    }

    fun checkPermission(sender: CommandSender, permission: String): Boolean {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(colorcode(prefix + "You do not have permission to use this command"))
            return false
        }
        return true
    }
}