package me.jsinco.solutilities.utility

import org.bukkit.Bukkit
import org.bukkit.Sound

object Util {

    fun playServerSound(sound: Sound, volume: Float, pitch: Float) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.scoreboardTags.contains("solutilities.silent")) {
                player.playSound(player.location, sound, volume, pitch)
            }
        }
    }
}