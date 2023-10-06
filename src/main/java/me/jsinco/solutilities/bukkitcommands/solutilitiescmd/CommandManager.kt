package me.jsinco.solutilities.bukkitcommands.solutilitiescmd

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands.*
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.util.Collections

class CommandManager(val plugin: SolUtilities) : BukkitCommand(
    "solutilities", "Main command for SolUtilities", "/solutilities <subcommand>", listOf()
) {

    private val subCommands: MutableMap<String, SubCommand> = mutableMapOf()

    init {
        subCommands["donationmsg"] = DonationMsgCommand()
        subCommands["reload"] = ReloadCommand()
        subCommands["silent"] = SilentCommand()
        subCommands["rankupbroadcast"] = RankupBroadcastCommand()
        subCommands["nbtsee"] = NBTSeeCommand()
        subCommands["nicksee"] = NickPreviewCommand()
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
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

        subCommand.execute(plugin, sender, args)
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>?): MutableList<String> {
        if (args == null) {
            return Collections.emptyList()
        } else if (args.size == 1) {
            val subCommandNames: MutableList<String> = mutableListOf()
            for (subcommand in subCommands) {
                if (subcommand.value.permission() == null || sender.hasPermission(subcommand.value.permission()!!)) {
                    subCommandNames.add(subcommand.key)
                }
            }
            return subCommandNames
        }

        if (!subCommands.containsKey(args[0])) return Collections.emptyList()
        val subCommand: SubCommand = subCommands[args[0]]!!

        return subCommand.tabComplete(plugin, sender, args) ?: Collections.emptyList()
    }
}