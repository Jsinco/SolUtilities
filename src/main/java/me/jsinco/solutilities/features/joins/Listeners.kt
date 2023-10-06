package me.jsinco.solutilities.features.joins

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
            event.joinMessage = plugin.config.getString("joins.join-firsttime.message")!!

            for (command in plugin.config.getStringList("joins.join-firsttime.commands")) {
                plugin.server.dispatchCommand(plugin.server.consoleSender, command.replace("%player%", event.player.name))
            }
        }

        if (event.joinMessage != null) {
            event.joinMessage = Util.colorcode(event.joinMessage!!
                .replace("%player%", event.player.name)
                .replace("%joins%", "${Saves.get().getInt("TotalJoins")}"))
        }
        Util.playServerSound(sound, 0.5f, 1f)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val sound: Sound = Sound.valueOf(plugin.config.getString("joins.quit-sound")!!)

        event.quitMessage = Util.colorcode("${plugin.config.getString("joins.quit-prefix")}${Joins.quitMessages.random()}".replace("%player%", event.player.name))
        Util.playServerSound(sound, 0.5f, 1f)
    }
}