package me.jsinco.solutilities.features.joins

import me.jsinco.solutilities.Saves
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue

class Listeners(val plugin: SolUtilities) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val sound: Sound = Sound.valueOf(plugin.config.getString("joins.join-sound")!!)
        if (Util.checkIfVanished(event.player)) return

        event.joinMessage = ("${plugin.config.getString("joins.join-prefix")}${Joins.JOIN_MSGS.random()}")
        if (!event.player.hasPlayedBefore()) {
            event.joinMessage = plugin.config.getString("joins.join-firsttime.message")!!
            Saves.get().set("TotalJoins", Saves.get().getInt("TotalJoins") + 1)
            Saves.save()
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                for (command in plugin.config.getStringList("joins.join-firsttime.commands")) {
                    plugin.server.dispatchCommand(plugin.server.consoleSender, command.replace("%player%", event.player.name))
                }
            }, 20L)
        }

        if (event.joinMessage != null) {
            event.joinMessage = Util.colorcode(event.joinMessage!!
                .replace("%player%", event.player.name)
                .replace("%joins%", "${Saves.get().getInt("TotalJoins")}"))
        }
        Util.playServerSound(sound, 0.5f, 1f)
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val sound: Sound = Sound.valueOf(plugin.config.getString("joins.quit-sound")!!)
        if (Util.checkIfVanished(event.player)) return
        event.quitMessage = Util.colorcode("${plugin.config.getString("joins.quit-prefix")}${Joins.QUIT_MSGS.random()}".replace("%player%", event.player.name))
        Util.playServerSound(sound, 0.5f, 1f)
    }

    @EventHandler
    fun onCommandPreProcess(event: PlayerCommandPreprocessEvent) {
        val command = event.message.substring(1).split(" ")[0]
        if (Joins.VANISH_CMDS.contains(command) && event.message.contains("-s")) {
            event.player.setMetadata("silentvanish", FixedMetadataValue(plugin, true))
            event.player.sendMessage(Util.prefix + "Silently vanishing...")
        }
    }
}