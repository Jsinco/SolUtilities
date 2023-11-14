package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.CommandSender

class PlayerHealthsCommand : SubCommand {


    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {

        if (args.size < 2) return

        when (args[1].lowercase()) {
            "view" -> {
                if (args.size < 3) return
                val target = Bukkit.getPlayerExact(args[2]) ?: return
                val maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue
                sender.sendMessage("${Util.prefix}${target.health}/${maxHealth} \uE114")
            }
            "setmax" -> {
                if (args.size < 3) return
                val target = Bukkit.getPlayerExact(args[2]) ?: return
                target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = args[3].toDoubleOrNull() ?: return

                val maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue
                sender.sendMessage("${Util.prefix}${target.name}'s health is now: $maxHealth \uE114")
            }
            "viewall" -> {
                for (player in Bukkit.getOnlinePlayers()){
                    val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue
                    sender.sendMessage("${Util.prefix}${player.name}: ${player.health}/${maxHealth} \uE114")
                }
            }
        }

    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        when (args.size) {
            2 -> {
                return mutableListOf("view", "setmax", "viewall")
            }
            4 -> {
                if (args[1] == "setmax") {
                    return mutableListOf("<double>")
                }
            }
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.command.playerhealths"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}