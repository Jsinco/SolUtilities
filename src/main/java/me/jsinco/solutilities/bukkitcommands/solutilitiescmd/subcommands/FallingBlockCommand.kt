package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.GUIActions
import me.jsinco.solutilities.utility.Util
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class FallingBlockCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val material = Material.valueOf(args[1].uppercase())
        val player = sender as Player

        //player.world.spawnFallingBlock(player.location, material.createBlockData())

        val armorstand: ArmorStand = player.world.spawnEntity(player.location, EntityType.ARMOR_STAND) as ArmorStand
        armorstand.isVisible = false
        armorstand.equipment.helmet = GUIActions.createGuiItem(false, ItemStack(material), "&f&l${material.name}")
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String> {
        return Util.MATERIALS_STRING
    }

    override fun permission(): String? {
        return "solutilities.command.fallingblock"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}