package me.jsinco.solutilities.celestial.aries;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.celestial.aries.itemprofler.ItemProfiler;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChooseGUI implements Listener {
    public static Inventory gui = Bukkit.createInventory(null, 27, ColorUtils.colorcode("&#ffb89c&lA&#ffb8b3&lr&#ffb9ca&li&#ffb9e1&le&#ffb9f8&ls"));

    @SuppressWarnings("DuplicatedCode")
    public static void init() {
        int[] roseBush = {0,1,7,8,18,19,25,26};
        int[] torchFlower = {2,4,6,20,22,24};
        int[] seaGrass = {3,5,21,23};
        int[] mediumAmethyst = {10,11,13,15,16};
        int[] largeAmethyst = {9,17};
        for (int bush : roseBush) {
            gui.setItem(bush, GUIActions.createGuiItem(false,new ItemStack(Material.ROSE_BUSH)," "));
        }
        for (int torch : torchFlower) {
            gui.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER)," "));
        }
        for (int grass : seaGrass) {
            gui.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS)," "));
        }
        for (int ameth : mediumAmethyst) {
            gui.setItem(ameth,GUIActions.createGuiItem(false,new ItemStack(Material.MEDIUM_AMETHYST_BUD)," "));
        }
        for (int ameth : largeAmethyst) {
            gui.setItem(ameth,GUIActions.createGuiItem(false,new ItemStack(Material.AMETHYST_CLUSTER)," "));
        }

        gui.setItem(12,GUIActions.createGuiItem(true, new ItemStack(Material.FLINT_AND_STEEL), ColorUtils.colorcode("&#ffb89c&lC&#ffb8a4&la&#ffb8ad&ln&#ffb8b5&ld&#ffb8bd&ll&#ffb8c6&le &#ffb9ce&lB&#ffb9d7&lu&#ffb9df&lr&#ffb9e7&ln&#ffb9f0&le&#ffb9f8&lr")));
//Vaulted gui.setItem(13,GUIActions.createGuiItem(true,new ItemStack(Material.BUNDLE), ColorUtils.colorcode("&#ffb89c&lA&#ffb8a8&lr&#ffb8b3&li&#ffb8bf&le&#ffb9ca&ls&#ffb9d6&l' &#ffb9e1&lB&#ffb9ed&la&#ffb9f8&lg")));
        gui.setItem(14,GUIActions.createNBTItem(true,"ariesmiscitemprofiler", 10071, Material.PAPER, "&#ffb89c&lI&#ffb8a4&lt&#ffb8ad&le&#ffb8b5&lm &#ffb8bd&lP&#ffb8c6&lr&#ffb9ce&lo&#ffb9d7&lf&#ffb9df&li&#ffb9e7&ll&#ffb9f0&le&#ffb9f8&lr"));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(gui)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        if (clickedItem.getType().equals(Material.FLINT_AND_STEEL)) {
            CandleApplyGUI.openInventory(player);
        }/* else if (clickedItem.getType().equals(Material.BUNDLE)) {
            MiscShop.openInventory(player, 0);
        }*/ else if (clickedItem.getType().equals(Material.PAPER)) {
            ItemProfiler.openItemProfiler(player);
        }
    }

    public static void openInventory(Player player) {
        player.openInventory(gui);
    }
}
