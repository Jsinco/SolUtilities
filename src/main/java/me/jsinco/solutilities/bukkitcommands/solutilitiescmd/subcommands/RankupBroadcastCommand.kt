package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.Util
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender

class RankupBroadcastCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        Bukkit.broadcastMessage(Util.colorcode(args.joinToString(" ").replace("rankupbroadcast ", "").trim()))
        Util.playServerSound(Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 1f)
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.command.rankupbroadcast"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}