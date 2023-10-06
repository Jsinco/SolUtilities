package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.util.ArrayList

class SellShopAlias : BukkitCommand(
    "lb", "Searchshop Alias", "/lb <item>", listOf()
) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (args == null) {
            sender.sendMessage(Util.prefix + "Specify an item to search for.")
            return true
        }
        Bukkit.dispatchCommand(sender, "searchshop TO_SELL " + args[0].uppercase())
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>?): MutableList<String> {
        return Util.MATERIALS_STRING
    }
}