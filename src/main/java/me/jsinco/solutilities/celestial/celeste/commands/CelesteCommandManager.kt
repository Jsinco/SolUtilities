package me.jsinco.solutilities.celestial.celeste.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.celestial.Celestial
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class CelesteCommandManager(val plugin: SolUtilities) : BukkitCommand(
    "celeste", "Celeste command", "/celeste <subcommand>", listOf()
) {
    private val subCommands: MutableMap<String, SubCommand> = mutableMapOf()
    private val celestial: Celestial = Celestial()
    init {
        subCommands["open"] = ShopOpenGui()
        subCommands["add"] = AddItemCommand()
        subCommands["remove"] = RemoveItemCommand()
        subCommands["reload"] = ReloadShopCommand()
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        return celestial.executeCelestialCommand(subCommands, sender, args)
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return celestial.tabCompleteCelestialCommand(subCommands, sender, args)
    }
}
