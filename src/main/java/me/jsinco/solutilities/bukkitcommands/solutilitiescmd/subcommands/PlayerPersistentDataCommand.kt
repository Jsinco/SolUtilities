package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerPersistentDataCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player

        when (args[1]) {
            "clear" -> {
                player.persistentDataContainer.keys.forEach {
                    player.persistentDataContainer.remove(it)
                }
            }
            "see" -> {
                player.sendMessage("NameSpacedKeys: ")
                player.sendMessage(player.persistentDataContainer.keys.toString())
            }
        }
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String> {
        return mutableListOf("clear", "see")
    }

    override fun permission(): String {
        return "solutilities.command.playerpersistentdata"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}