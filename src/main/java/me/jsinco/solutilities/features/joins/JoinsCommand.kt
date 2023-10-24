package me.jsinco.solutilities.features.joins

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class JoinsCommand(val plugin: SolUtilities) : BukkitCommand(
    "joins", "Joins command", "/joins <join|quit>", listOf()
) {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        if (!Util.checkPermission(sender, "solutilities.command.joins")) return false
        val arg = args?.getOrNull(0) ?: return false
        val player = sender as? Player ?: return false

        if (player.hasMetadata("silentvanish")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                if (!player.hasMetadata("vanished")) {
                    player.removeMetadata("silentvanish", plugin)
                }
            }, 1L)
            return true
        }

        when (arg.lowercase()) {
            "join" -> {
                Bukkit.broadcastMessage(Util.colorcode("${plugin.config.getString("joins.join-prefix")}${Joins.JOIN_MSGS.random()}".replace("%player%", player.name)))
                val sound = Sound.valueOf(plugin.config.getString("joins.join-sound")!!)
                Util.playServerSound(sound, 0.5f, 1f)
            }

            "quit" -> {
                Bukkit.broadcastMessage(Util.colorcode("${plugin.config.getString("joins.quit-prefix")}${Joins.QUIT_MSGS.random()}".replace("%player%", player.name)))
                val sound = Sound.valueOf(plugin.config.getString("joins.quit-sound")!!)
                Util.playServerSound(sound, 0.5f, 1f)
            }
        }
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>?): MutableList<String> {
        return mutableListOf("join", "quit")
    }
}