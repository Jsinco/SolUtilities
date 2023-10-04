package me.jsinco.solutilities.bukkitcommands.commands

import me.jsinco.solutilities.ColorUtils
import me.jsinco.solutilities.SolUtilities
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class SearchShopAlias(val plugin: SolUtilities) : CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) return false
        if (strings.isEmpty()) {
            commandSender.sendMessage(ColorUtils.colorcode(plugin.config.getString("prefix") + "Specify an item to search for."))
            return true
        }
        if (command.name.equals("ls", ignoreCase = true)) {
            Bukkit.dispatchCommand(commandSender, "searchshop TO_BUY " + strings[0].uppercase())
        } else if (command.name.equals("lb", ignoreCase = true)) {
            Bukkit.dispatchCommand(commandSender, "searchshop TO_SELL " + strings[0].uppercase())
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        val materials: MutableList<String> = ArrayList()
        for (material in Material.values()) {
            materials.add(material.name.lowercase())
        }
        return materials
    }
}