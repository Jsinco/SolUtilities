package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.clip.placeholderapi.PlaceholderAPI
import me.jsinco.solutilities.utility.Util
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NickPreviewCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = if (args.size > 2) {
            Bukkit.getPlayerExact(args[1])
        } else {
            sender as? Player ?: return
        }
        sender.sendMessage(Util.colorcode(PlaceholderAPI.setPlaceholders(player, "%solutilities_prefix%%eternaltags_tag% &7•&#E2E2E2 %player_displayname%&7:&#E2E2E2 This is how my name looks in chat!")))
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String? {
        return null
    }

    override fun playerOnly(): Boolean {
        return false
    }
}