package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SilentCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player

        val string = if (player.scoreboardTags.contains("solutilities.silent")) {
            player.scoreboardTags.remove("solutilities.silent")
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ex flag p@${player.name} solutilities.silent:!")
            "&aenabled"
        } else {
            player.scoreboardTags.add("solutilities.silent")
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ex flag p@${player.name} solutilities.silent")
            "&cdisabled"
        }
        player.sendMessage(Util.colorcode("${Util.prefix}Your sounds are now $string&#E2E2E2!"))
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