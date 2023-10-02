package me.jsinco.solutilities.solcmd

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.solcmd.subcommands.HelpCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class CommandManager(val plugin: SolUtilities) : CommandExecutor, TabCompleter {

    private val subCommands: MutableMap<String, SubCommand> = mutableMapOf()

    init {
        subCommands += "help" to HelpCommand()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val prefix = plugin.config.getString("prefix")
        if (args == null) {
            sender.sendMessage("${prefix}An error occurred while executing this command.")
            return true
        }

        if (!subCommands.containsKey(args[0])) {
            sender.sendMessage("${prefix}Unknown subcommand.")
            return true
        }

        val subCommand: SubCommand = subCommands[args[0]]!!
        if (subCommand.playerOnly() && sender !is Player) {
            sender.sendMessage("${prefix}This command can only be executed by players.")
            return true
        } else if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission()!!)) {
            sender.sendMessage("${prefix}You do not have permission to execute this command.")
            return true
        }

        subCommand.execute(plugin, sender, args!!)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        if (args == null) {
            return null
        } else if (args.size == 1) {
            return subCommands.keys.toMutableList()
        }

        if (!subCommands.containsKey(args[0])) return null
        val subCommand: SubCommand = subCommands[args[0]]!!

        return subCommand.tabComplete(plugin, sender, args)
    }
}