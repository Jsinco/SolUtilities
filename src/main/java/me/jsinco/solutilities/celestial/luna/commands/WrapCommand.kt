package me.jsinco.solutilities.celestial.luna.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.celestial.luna.LunaUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class WrapCommand : SubCommand {

    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player

        if (args.size < 2) {
            player.sendMessage("${plugin.config.getString("prefix")}Usage: /luna wrap <name>")
            return
        }

        val name = args.joinToString(" ")
            .replace(args[0], "")
            .trim()
        val item = player.inventory.itemInMainHand
        player.inventory.addItem(LunaUtil.createItemAsWrap(item, name))
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return Collections.singletonList("<name>")
    }

    override fun permission(): String {
        return "solutilities.celestial.luna"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}