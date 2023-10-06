package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SilentCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player

        if (player.scoreboardTags.contains("solutilities.silent")) {
            player.scoreboardTags.remove("solutilities.silent")
        } else {
            player.scoreboardTags.add("solutilities.silent")
        }
        player.sendMessage("${plugin.config.getString("prefix")}Sounds toggled!")
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String? {
        return null
    }

    override fun playerOnly(): Boolean {
        return true
    }
}