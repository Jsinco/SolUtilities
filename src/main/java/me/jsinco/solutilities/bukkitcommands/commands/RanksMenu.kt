package me.jsinco.solutilities.bukkitcommands.commands

import me.jsinco.solutilities.Util
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.ranks.GUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RanksMenu(val plugin: SolUtilities) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        sender.sendMessage(Util.colorcode(plugin.config.getString("prefix") + "Opening ranks menu!"))
        GUI.openPage(sender)
        return true
    }
}
