package me.jsinco.solutilities

import org.bukkit.command.CommandSender

interface SubCommand {
    fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>)
    fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>?
    fun permission(): String?
    fun playerOnly(): Boolean
}