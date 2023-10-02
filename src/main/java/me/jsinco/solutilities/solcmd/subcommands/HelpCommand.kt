package me.jsinco.solutilities.solcmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.solcmd.SubCommand
import org.bukkit.command.CommandSender

class HelpCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<String?>) {
        TODO("Not yet implemented")
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<String?>?): List<String?>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.command.help"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}