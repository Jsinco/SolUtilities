package me.jsinco.solutilities.bukkitcommands.commands

import me.jsinco.solutilities.Util
import me.jsinco.solutilities.SolUtilities
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PingCommand(val plugin: SolUtilities) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val player = if (args != null && args.size > 2) {
            Bukkit.getPlayerExact(args[1])
        } else {
            sender as? Player ?: return true
        }

        if (player == null) return true
        if (player == sender) {
            sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}Pong! Your ping is ${player.ping}ms"))
        } else {
            sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}Pong! ${player.name}'s ping is ${player.ping}ms"))
        }
        return true
    }


}