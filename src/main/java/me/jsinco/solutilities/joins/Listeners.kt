package me.jsinco.solutilities.joins

import me.jsinco.solutilities.Saves
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Listeners(val plugin: SolUtilities) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val sound: Sound = Sound.valueOf(plugin.config.getString("joins.join-sound")!!)

        event.joinMessage = ("${plugin.config.getString("joins.join-prefix")}${Joins.joinMessages.random()}")
        if (!event.player.hasPlayedBefore()) {
            event.joinMessage = plugin.config.getString("joins.join-firsttime")!!
        }

        if (event.joinMessage != null) {
            event.joinMessage = event.joinMessage!!
                .replace("%player%", event.player.name)
                .replace("%joins%", "${Saves.get().getInt("TotalJoins")}")
        }
        Util.playServerSound(sound, 0.5f, 1f)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val sound: Sound = Sound.valueOf(plugin.config.getString("joins.quit-sound")!!)

        event.quitMessage = ("${plugin.config.getString("joins.quit-prefix")}${Joins.quitMessages.random()}")
        Util.playServerSound(sound, 0.5f, 1f)
    }
}