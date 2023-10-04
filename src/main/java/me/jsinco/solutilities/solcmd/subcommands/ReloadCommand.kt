package me.jsinco.solutilities.solcmd.subcommands

import me.jsinco.solutilities.Util
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.solcmd.SubCommand
import org.bukkit.command.CommandSender

class ReloadCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        plugin.reloadConfig()
        sender.sendMessage(Util.colorcode(plugin.config.getString("prefix") + "Config reloaded!"))
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): List<String?>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.command.reload"
    }

    override fun playerOnly(): Boolean {
        return false
    }

}