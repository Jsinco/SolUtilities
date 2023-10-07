package me.jsinco.solutilities.celestial.luna.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.celestial.luna.SelectGUI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class OpenGUICommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        if (args.size < 2) {
            sender.sendMessage("${plugin.config.getString("prefix")}Usage: /luna open <player>")
            return
        }
        val target: Player = Bukkit.getPlayerExact(args[1]) ?: return
        SelectGUI.init()
        SelectGUI.openInventory(target)
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.celestial.luna"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}