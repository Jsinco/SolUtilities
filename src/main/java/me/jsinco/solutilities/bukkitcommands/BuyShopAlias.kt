package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class BuyShopAlias : BukkitCommand(
    "ls", "Searches for an item in the shop", "/ls <item>", listOf()
) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            sender.sendMessage(Util.prefix + "Specify an item to search for.")
            return true
        }
        Bukkit.dispatchCommand(sender, "searchshop TO_BUY " + args[0].uppercase())
        return true
    }



    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return Util.MATERIALS_STRING
    }
}