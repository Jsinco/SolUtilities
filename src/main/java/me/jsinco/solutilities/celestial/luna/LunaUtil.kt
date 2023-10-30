package me.jsinco.solutilities.celestial.luna

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.persistence.PersistentDataType


object LunaUtil {
    private val plugin: SolUtilities = SolUtilities.getPlugin()

    val WRAPPABLE_MATERIALS: List<String> = listOf("LEATHER", "NETHERITE", "ROD", "BOW", "SHIELD", "PAPER")
    val ARMORS: List<String> = listOf("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS")
    val LORE: List<String> = Util.colorArrayList(listOf("§fUse this item to wrap your", "§ftools or armor!", "", "§fSpeak to Luna §fat &#a8ff92/celestial", "§fto apply!",
        "", "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ",
        "&#EEE1D5Tier • &#b9ddff&lC&#bedaff&le&#c4d8ff&ll&#c9d5ff&le&#ced3ff&ls&#d3d0ff&lt&#d9cdff&li&#decbff&la&#e3c8ff&ll",
        "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "))!!

    fun getItemType(item: ItemStack): String? {
        var precondition = false

        for (string in WRAPPABLE_MATERIALS) {
            if (item.type.name.contains(string)) {
                precondition = true
                break
            }
        }
        if (!precondition) return null

        return when (item.type) {
            Material.PAPER -> {
                "helmet"
            }
            Material.BLAZE_ROD -> {
                "wand"
            }

            else -> {
                item.type.toString().substring(item.type.toString().lastIndexOf("_") + 1).lowercase()
            }
        }
    }

    fun createItemAsWrap(item: ItemStack, name: String): ItemStack {
        val meta: ItemMeta = item.itemMeta!!

        val data = meta.persistentDataContainer
        val type = getItemType(item)?.uppercase()?.replace("wand", "blaze_rod") ?: return item


        data.set(NamespacedKey(plugin, "type"), PersistentDataType.STRING, type)
        data.set(NamespacedKey(plugin, "model"), PersistentDataType.INTEGER, meta.customModelData)

        if (ARMORS.contains(type)) {
            val leatherArmorMeta = meta as LeatherArmorMeta
            data.set(NamespacedKey(plugin, "red"), PersistentDataType.INTEGER, leatherArmorMeta.color.red)
            data.set(NamespacedKey(plugin, "green"), PersistentDataType.INTEGER, leatherArmorMeta.color.green)
            data.set(NamespacedKey(plugin, "blue"), PersistentDataType.INTEGER, leatherArmorMeta.color.blue)
        } else if (item.type == Material.PAPER) {
            data.set(NamespacedKey(plugin, "paperhelmet"), PersistentDataType.SHORT, 0.toShort())
        }

        meta.setDisplayName(IridiumColorAPI.process(Util.colorcode(name)))
        meta.lore = LORE
        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE)
        item.itemMeta = meta
        return item
    }
}