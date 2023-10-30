package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class ItemFlagCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        sender as Player
        val item = sender.inventory.itemInMainHand

        if (item.type.isAir) return

        val itemMeta = item.itemMeta

        val flag = args[1]
        if (flag == "all") {
            for (itemFlag in ItemFlag.entries) {
                itemMeta.addItemFlags(itemFlag)
            }
        } else {
            itemMeta.addItemFlags(ItemFlag.valueOf(args[1].uppercase()))
        }
        item.itemMeta = itemMeta
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()
        for (flag in ItemFlag.entries) {
            list.add(flag.name.lowercase())
        }
        list.add("all")
        return list
    }

    override fun permission(): String? {
        return "solutilities.command.itemflag"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}