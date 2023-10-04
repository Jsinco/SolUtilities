package me.jsinco.solutilities.solcmd

import me.jsinco.solutilities.SolUtilities
import org.bukkit.command.CommandSender

interface SubCommand {
    fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>)
    fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): List<String?>?
    fun permission(): String?
    fun playerOnly(): Boolean
}