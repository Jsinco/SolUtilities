package me.jsinco.solutilities.celestial.aries.commands

import me.jsinco.solutilities.celestial.aries.AriesMainGUI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class OpenAriesGUI : BukkitCommand(
    "aries", "Opens the Aries GUI", "/aries <player>", listOf()
){
    /*
     * Aries doesn't have much going on with him, so we don't need a command manager for him
     */
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        if (args == null) return false
        val target = Bukkit.getPlayerExact(args[0]) ?: return false
        AriesMainGUI.init()
        AriesMainGUI.openInventory(target)
        return true
    }
}