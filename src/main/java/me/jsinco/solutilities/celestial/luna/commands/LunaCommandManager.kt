package me.jsinco.solutilities.celestial.luna.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.celestial.Celestial
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class LunaCommandManager(val plugin: SolUtilities) : BukkitCommand(
    "luna", "Luna command", "/luna <subcommand>", listOf("luna")
) {

    private val subCommands: MutableMap<String, SubCommand> = mutableMapOf()
    private val celestial: Celestial = Celestial()
    init {
        subCommands["wrap"] = WrapCommand()
        subCommands["wrapinv"] = WrapInventoryCommand()
        subCommands["open"] = OpenGUICommand()
        subCommands["wraptoken"] = WrapTokenCommand()
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        return celestial.executeCelestialCommand(subCommands, sender, args)
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return celestial.tabCompleteCelestialCommand(subCommands, sender, args)
    }
}