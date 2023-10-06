package me.jsinco.solutilities.celestial

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import me.jsinco.solutilities.utility.Util.getOnlinePlayers
import org.bukkit.command.CommandSender

class Celestial { // Can't be abstract because classes can only have one supertype
    companion object {
        private val plugin: SolUtilities = SolUtilities.getPlugin()
    }

    fun executeCelestialCommand(subCommands: MutableMap<String, SubCommand>, sender: CommandSender, args: Array<out String>?): Boolean {
        if (args == null || !subCommands.containsKey(args[0])) return false

        val subCommand = subCommands[args[0]]!!
        if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission()!!)) {
            sender.sendMessage("${Util.prefix}You do not have permission to execute this command")
            return true
        } else if (subCommand.playerOnly() && sender !is org.bukkit.entity.Player) {
            sender.sendMessage("${Util.prefix}This command can only be executed by players")
            return true
        }


        subCommand.execute(plugin, sender, args)
        return true
    }

    fun tabCompleteCelestialCommand(subCommands: MutableMap<String, SubCommand>, sender: CommandSender, args: Array<out String>?): MutableList<String> {
        if (args == null) return getOnlinePlayers()
        else if (args.size == 1) {
            return subCommands.keys.toMutableList()
        } else if (subCommands.containsKey(args[0])) {
            val subCommand = subCommands[args[0]]!!
            return subCommand.tabComplete(plugin, sender, args) ?: mutableListOf()
        }
        return getOnlinePlayers()
    }
}