package me.jsinco.solutilities.celestial.luna.commands

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


class WrapTokenCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player
        val item = ItemStack(Material.valueOf(args[1].uppercase()))
        val meta = item.itemMeta!!

        meta.persistentDataContainer.set(NamespacedKey(plugin, "WrapToken"), PersistentDataType.SHORT, 0.toShort())
        meta.addEnchant(Enchantment.DURABILITY, 10, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)

        meta.setDisplayName(IridiumColorAPI.process(
            Util.colorcode(
            args.joinToString(" ")
            .replace("${args[0]} ${args[1]}", "")
            .trim())
        ))
        meta.lore = Util.colorArrayList(mutableListOf(
            "§fUse this token to purchase a", "§fwrap from &#a8ff92/wraps§f!", "",
            "§fRight-click the &#a8ff92NPC §fwith the", "§fsame name to exchange!",
            "", "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ",
            "&#EEE1D5Tier • &#b9ddff&lC&#bedaff&le&#c4d8ff&ll&#c9d5ff&le&#ced3ff&ls&#d3d0ff&lt&#d9cdff&li&#decbff&la&#e3c8ff&ll",
            "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "
            )
        )!!

        item.itemMeta = meta
        player.inventory.addItem(item)
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        when (args.size) {
            2 -> {
                return Util.MATERIALS_STRING
            }
            3 -> {
                return mutableListOf("<name>")
            }
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.celestial.luna"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}