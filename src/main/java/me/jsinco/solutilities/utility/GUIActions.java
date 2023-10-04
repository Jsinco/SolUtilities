package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class GUIActions {

    public static final ItemStack border = createGuiItem(false,new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");


    public static ItemStack createGuiItem(boolean enchanted, ItemStack itemStack, String name, String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Util.colorcode(name));
        meta.setLore(Arrays.asList(lore));
        if (enchanted) meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }



    public static ItemStack createNBTItem(boolean glint, String key, int cMdlData, Material m, String name, String... lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Util.colorcode(name));
        meta.setLore(Arrays.asList(Util.colorArray(lore)));

        if (glint) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        }

        if (key != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey(pl, key), PersistentDataType.SHORT, (short) 1);
        }

        if (cMdlData != 0) {
            meta.setCustomModelData(cMdlData);
        }

        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack getSkull(String UUID){ //method for creating a player head from a UUID
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)));
        playerHead.setItemMeta(meta);
        return playerHead;
    }




}

