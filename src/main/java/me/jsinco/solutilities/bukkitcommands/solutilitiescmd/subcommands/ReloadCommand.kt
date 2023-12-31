package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.utility.Util
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.command.CommandSender

class ReloadCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        plugin.reloadConfig()
        Util.loadUtils()
        sender.sendMessage(Util.colorcode(plugin.config.getString("prefix") + "Config reloaded!"))
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.command.reload"
    }

    override fun playerOnly(): Boolean {
        return false
    }

}