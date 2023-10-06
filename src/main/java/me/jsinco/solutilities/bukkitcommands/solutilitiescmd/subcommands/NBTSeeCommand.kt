package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NBTSeeCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player
        val item: ItemStack = player.inventory.itemInMainHand
        val meta = item.itemMeta
        if (meta != null) {
            player.sendMessage("NameSpacedKeys: ")
            player.sendMessage(meta.persistentDataContainer.keys.toString())
        }
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.command.nbtsee"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}