package me.jsinco.solutilities.celestial.luna.commands

import me.jsinco.solutilities.SolUtilities
import org.bukkit.command.CommandSender

interface LunaCommand {
    fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>)
    fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>?
    fun permission(): String?
    fun playerOnly(): Boolean
}