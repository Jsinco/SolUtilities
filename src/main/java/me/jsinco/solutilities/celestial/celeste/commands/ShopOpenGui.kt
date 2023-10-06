package me.jsinco.solutilities.celestial.celeste.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.celestial.celeste.Shop
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class ShopOpenGui : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        if (args.size < 2) {
            sender.sendMessage("${plugin.config.getString("prefix")}Usage: /celeste shop <player>")
            return
        }
        val target = Bukkit.getPlayerExact(args[1]) ?: return
        Shop.openInventory(target, 0)
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.celestial.celeste"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}