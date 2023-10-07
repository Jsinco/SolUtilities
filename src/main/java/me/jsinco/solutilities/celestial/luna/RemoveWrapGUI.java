package me.jsinco.solutilities.celestial.luna;

import me.jsinco.solutilities.utility.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RemoveWrapGUI implements Listener {

    static List<Inventory> removeWrapGUIs = new ArrayList<>();

    @SuppressWarnings("DuplicatedCode")
    public static Inventory removewrapGUI() {
        Inventory removewrap = Bukkit.createInventory(null,27, Util.colorcode("&#b9ddff&lL&#bedaff&lu&#c4d8ff&ln&#c9d5ff&la &#ced3ff&lW&#d3d0ff&lr&#d9cdff&la&#decbff&lp&#e3c8ff&ls"));
        int[] roseBush = {0,1,7,8,18,19,25,26};
        int[] torchFlower = {2,4,6,20,22,24};
        int[] seaGrass = {3,5,21,23};
        int[] mediumAmethyst = {10,11,13,15,16};
        int[] largeAmethyst = {9,17};
        for (int bush : roseBush) {
            removewrap.setItem(bush, GUIActions.createGuiItem(false,new ItemStack(Material.ROSE_BUSH)," "));
        }
        for (int torch : torchFlower) {
            removewrap.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER)," "));
        }
        for (int grass : seaGrass) {
            removewrap.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS)," "));
        }
        for (int ameth : mediumAmethyst) {
            removewrap.setItem(ameth,GUIActions.createGuiItem(false,new ItemStack(Material.MEDIUM_AMETHYST_BUD)," "));
        }
        for (int ameth : largeAmethyst) {
            removewrap.setItem(ameth,GUIActions.createGuiItem(false,new ItemStack(Material.AMETHYST_CLUSTER)," "));
        }

        removewrap.setItem(14, GUIActions.createGuiItem(true,new ItemStack(Material.BARRIER), "&#b9ddff&lR&#bedbff&le&#c2d8ff&lm&#c7d6ff&lo&#ccd4ff&lv&#d0d1ff&le &#d5cfff&lW&#dacdff&lr&#decaff&la&#e3c8ff&lp",
                "§7This action cannot be undone"));

        removeWrapGUIs.add(removewrap);
        return removewrap;
    }

    @EventHandler
    public void invClick (InventoryClickEvent event) {
        if (!removeWrapGUIs.contains(event.getInventory())) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;
        if (clickedItem.getType() == Material.ROSE_BUSH || clickedItem.getType() == Material.TORCHFLOWER ||
                clickedItem.getType() == Material.SEAGRASS || clickedItem.getType() == Material.MEDIUM_AMETHYST_BUD || clickedItem.getType() == Material.AMETHYST_CLUSTER) {
            event.setCancelled(true);
        }

        else if (clickedItem.getType() == Material.BARRIER) {
            Wrapping.removeWrap(event.getInventory().getItem(12), player, event.getInventory());
            event.setCancelled(true);
        }
    }



    @EventHandler
    public void wrapGUIClose (InventoryCloseEvent event) {
        if (!removeWrapGUIs.contains(event.getInventory())) return;
        Inventory removeWrapGUI = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (removeWrapGUI.getItem(12) != null) player.getInventory().addItem(removeWrapGUI.getItem(12));
        removeWrapGUIs.remove(event.getInventory());
    }

    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(removewrapGUI());
    }
}
