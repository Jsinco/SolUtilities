package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class ArmorstandCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player

        if (args.size < 2) {
            player.sendMessage("${plugin.config.getString("prefix")}Usage: /solutilities armorstand <sethand/sethelmet>")
            return
        }

        val item = player.inventory.itemInMainHand
        val armorStand = if (player.getTargetEntity(5) is ArmorStand) {
            player.getTargetEntity(5) as ArmorStand
        } else {
            player.sendMessage("${plugin.config.getString("prefix")}You must be looking at an armorstand!")
            return
        }

        when (args[1].lowercase()) {
            "sethand" -> {
                player.inventory.setItemInMainHand(armorStand.equipment.itemInMainHand)
                armorStand.equipment.setItemInMainHand(item)
            }
            "sethelmet" -> {
                player.inventory.setItemInMainHand(armorStand.equipment.helmet)
                armorStand.equipment.helmet = item
            }
        }
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        when (args.size) {
            2 -> {
                return mutableListOf("sethand", "sethelmet")
            }
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.armorstand" // I have to use legacy permission node
    }

    override fun playerOnly(): Boolean {
        return true
    }


}