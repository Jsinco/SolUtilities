package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class PingCommand : BukkitCommand(
    "ping", "Shows your ping", "/ping <player?>", listOf("ping")
) {


    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        val target = if (args.size == 1) {
            Bukkit.getPlayerExact(args[0])
        } else {
            sender as? Player ?: return true
        }

        if (target == null) return true
        if (target == sender) {
            sender.sendMessage(Util.colorcode("${Util.prefix}Pong! Your ping is ${target.ping}ms"))
        } else {
            sender.sendMessage(Util.colorcode("${Util.prefix}Pong! ${target.name}'s ping is ${target.ping}ms"))
        }
        return true
    }


}