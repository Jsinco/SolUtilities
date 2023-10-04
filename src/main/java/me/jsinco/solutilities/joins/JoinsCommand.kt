package me.jsinco.solutilities.joins

import me.jsinco.solutilities.SolUtilities
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinsCommand(val plugin: SolUtilities) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val arg = args?.getOrNull(0) ?: return false
        val player = sender as? Player ?: return false
        when (arg.lowercase()) {
            "join" -> {
                val event = PlayerJoinEvent(player, "${plugin.config.getString("joins.join-prefix")}${Joins.joinMessages.random()}")
                plugin.server.pluginManager.callEvent(event)
            }

            "quit" -> {
                val event = PlayerQuitEvent(player, "${plugin.config.getString("joins.quit-prefix")}${Joins.quitMessages.random()}")
                plugin.server.pluginManager.callEvent(event)
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        return mutableListOf("join", "quit")
    }
}