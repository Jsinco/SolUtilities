package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class PingCommand : BukkitCommand(
    "ping", "Shows your ping", "/ping <player?>", listOf()
) {


    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        val player = if (args != null && args.size > 2) {
            Bukkit.getPlayerExact(args[1])
        } else {
            sender as? Player ?: return true
        }

        if (player == null) return true
        if (player == sender) {
            sender.sendMessage(Util.colorcode("${Util.prefix}Pong! Your ping is ${player.ping}ms"))
        } else {
            sender.sendMessage(Util.colorcode("${Util.prefix}Pong! ${player.name}'s ping is ${player.ping}ms"))
        }
        return true
    }


}