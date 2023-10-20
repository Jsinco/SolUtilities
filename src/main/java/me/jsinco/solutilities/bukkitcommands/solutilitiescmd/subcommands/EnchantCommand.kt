package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class EnchantCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player
        val item = player.inventory.itemInMainHand

        val meta = item.itemMeta ?: return

        val enchant = args[1]
        val level = if (args[2] == "max") {
            255
        } else {
            args[2].toInt()
        }

        if (enchant == "all") {
            for (enchantment in Enchantment.values()) {
                meta.addEnchant(enchantment, level, true)
            }
        } else {
            Enchantment.getByKey(NamespacedKey.minecraft(enchant))?.let { meta.addEnchant(it, level, true) }
        }

        item.itemMeta = meta
        player.inventory.setItemInMainHand(item)
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        val minecraftEnchants: MutableList<String> = mutableListOf()
        minecraftEnchants.add("all")
        Enchantment.values().forEach {
            minecraftEnchants.add(it.key.key)
        }

        if (args.size == 2) {
            return minecraftEnchants
        } else if (args.size == 3) {
            return mutableListOf("1", "2", "3", "4", "5", "max")
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.command.enchant"
    }

    override fun playerOnly(): Boolean {
        return true
    }

    fun getEnchant(enchantment: Enchantment): String {
        return when (Enchantment.getByKey(enchantment.key)) {
            Enchantment.ARROW_DAMAGE -> "Power"
            Enchantment.ARROW_FIRE -> "Flame"
            Enchantment.ARROW_INFINITE -> "Infinity"
            Enchantment.ARROW_KNOCKBACK -> "Punch"
            Enchantment.BINDING_CURSE -> "Curse of Binding"
            Enchantment.DAMAGE_ALL -> "Sharpness"
            Enchantment.DAMAGE_ARTHROPODS -> "Bane of Arthropods"
            Enchantment.DAMAGE_UNDEAD -> "Smite"
            Enchantment.DEPTH_STRIDER -> "Depth Strider"
            Enchantment.DIG_SPEED -> "Efficiency"
            Enchantment.DURABILITY -> "Unbreaking"
            Enchantment.FIRE_ASPECT -> "Fire Aspect"
            Enchantment.FROST_WALKER -> "Frost Walker"
            Enchantment.KNOCKBACK -> "Knockback"
            Enchantment.LOOT_BONUS_BLOCKS -> "Fortune"
            Enchantment.LOOT_BONUS_MOBS -> "Looting"
            Enchantment.LUCK -> "Luck of the Sea"
            Enchantment.LURE -> "Lure"
            Enchantment.MENDING -> "Mending"
            Enchantment.OXYGEN -> "Respiration"
            Enchantment.PROTECTION_ENVIRONMENTAL -> "Protection"
            Enchantment.PROTECTION_EXPLOSIONS -> "Blast Protection"
            Enchantment.PROTECTION_FALL -> "Feather Falling"
            Enchantment.PROTECTION_FIRE -> "Fire Protection"
            Enchantment.PROTECTION_PROJECTILE -> "Projectile Protection"
            Enchantment.SILK_TOUCH -> "Silk Touch"
            Enchantment.SWEEPING_EDGE -> "Sweeping Edge"
            Enchantment.THORNS -> "Thorns"
            Enchantment.VANISHING_CURSE -> "Cure of Vanishing"
            Enchantment.WATER_WORKER -> "Aqua Affinity"
            else -> "Unknown"
        }
    }

}