package me.jsinco.solutilities.celestial.luna;

import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class WrapGUI implements Listener {

    static List<Inventory> wrapGUIs = new ArrayList<>();

    public static Inventory wrapGUI() {
        Inventory wraps = Bukkit.createInventory(null,27, Util.colorcode("&#b9ddff&lL&#bedaff&lu&#c4d8ff&ln&#c9d5ff&la &#ced3ff&lW&#d3d0ff&lr&#d9cdff&la&#decbff&lp&#e3c8ff&ls"));
        int[] roseBush = {0,1,7,8,18,19,25,26};
        int[] torchFlower = {2,4,6,20,22,24};
        int[] seaGrass = {3,5,21,23};
        int[] mediumAmethyst = {9,10,11,15,16,17};
        for (int bush : roseBush) {
            wraps.setItem(bush, GUIActions.createGuiItem(false,new ItemStack(Material.ROSE_BUSH)," "));
        }
        for (int torch : torchFlower) {
            wraps.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER)," "));
        }
        for (int grass : seaGrass) {
            wraps.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS)," "));
        }
        for (int ameth : mediumAmethyst) {
            wraps.setItem(ameth,GUIActions.createGuiItem(false,new ItemStack(Material.MEDIUM_AMETHYST_BUD)," "));
        }

        wraps.setItem(13, GUIActions.createGuiItem(true, new ItemStack(Material.AMETHYST_CLUSTER), "&#b9ddff&lC&#c0daff&lo&#c7d6ff&ln&#ced3ff&lf&#d5cfff&li&#dcccff&lr&#e3c8ff&lm"));

        wrapGUIs.add(wraps);
        return wraps;
    }
    
    @EventHandler
    public void wrapGUIClick (InventoryClickEvent event) {
        if (!wrapGUIs.contains(event.getInventory())) return;
        Inventory wrapGUI = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;
        if (clickedItem.getType() == Material.ROSE_BUSH || clickedItem.getType() == Material.TORCHFLOWER ||
                clickedItem.getType() == Material.SEAGRASS || clickedItem.getType() == Material.MEDIUM_AMETHYST_BUD) {
            event.setCancelled(true);
        }

        else if (clickedItem.getType() == Material.AMETHYST_CLUSTER) {
            event.setCancelled(true);
            ItemStack item = wrapGUI.getItem(12);
            ItemStack wrap = wrapGUI.getItem(14);

            if (item == null || wrap == null || !wrap.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(ModelAdmin.pl,"type"), PersistentDataType.STRING)) {
                player.sendMessage(Util.colorcode("&#b9ddffH&#badcffm&#bcdcffm&#bddbffm&#bfdaff, &#c0d9fft&#c2d9ffh&#c3d8ffi&#c5d7ffs &#c6d6ffd&#c7d6ffo&#c9d5ffe&#cad4ffs&#ccd4ffn&#cdd3ff'&#cfd2fft &#d0d1ffl&#d2d1ffo&#d3d0ffo&#d5cfffk &#d6cfffl&#d7ceffi&#d9cdffk&#daccffe &#dcccffa &#ddcbffw&#dfcaffr&#e0c9ffa&#e2c9ffp&#e3c8ff!"));
                return;
            }

            String m = item.getType().toString().toLowerCase();
            if (m.contains("netherite")||m.contains("shield")||m.contains("bow")||m.contains("rod")) {
                if (Wrapping.isArmor(item) && !wrap.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(ModelAdmin.pl, "PaperHelmet"), PersistentDataType.SHORT)) {
                    Wrapping.wrapItem(item, Wrapping.convertArmorToLeather(item), wrap, player, wrapGUI);
                } else {
                    Wrapping.wrapItem(item, item, wrap, player, wrapGUI);
                }
            } else {
                player.sendMessage(Util.colorcode("&#b9ddffY&#badcffo&#bbdcffu &#bddbffc&#bedbffa&#bfdaffn &#c0daffo&#c1d9ffn&#c2d8ffl&#c4d8ffy &#c5d7ffw&#c6d7ffr&#c7d6ffa&#c8d5ffp &#c9d5ffn&#cbd4ffe&#ccd4fft&#cdd3ffh&#ced3ffe&#cfd2ffr&#d0d1ffi&#d2d1fft&#d3d0ffe &#d4d0fft&#d5cfffo&#d6ceffo&#d7ceffl&#d9cdffs &#dacdffa&#dbccffn&#dcccffd &#ddcbffa&#decaffr&#e0caffm&#e1c9ffo&#e2c9ffr&#e3c8ff!"));
            }
        }
    }


    @EventHandler
    public void wrapGUIClose (InventoryCloseEvent event) {
        if (!wrapGUIs.contains(event.getInventory())) return;
        Inventory wrapGUI = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (wrapGUI.getItem(12) != null) player.getInventory().addItem(wrapGUI.getItem(12));
        if (wrapGUI.getItem(14) != null) player.getInventory().addItem(wrapGUI.getItem(14));
        wrapGUIs.remove(event.getInventory());
    }

    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(wrapGUI());
    }

}
