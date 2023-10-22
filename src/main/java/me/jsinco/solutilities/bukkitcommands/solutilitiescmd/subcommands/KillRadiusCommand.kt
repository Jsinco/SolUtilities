package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class KillRadiusCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        sender as Player

        sender.getNearbyEntities(10.0,10.0,10.0).forEach { entity ->
            if (entity is LivingEntity) {
                entity.health = 0.0
            } else {
                entity.remove()
            }
        }
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.command.killradius"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}