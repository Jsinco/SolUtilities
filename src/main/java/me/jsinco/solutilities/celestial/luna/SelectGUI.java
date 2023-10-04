package me.jsinco.solutilities.celestial.luna;

import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SelectGUI implements Listener {

    private static final Inventory gui = Bukkit.createInventory(null, 27, Util.colorcode("&#b9ddff&lL&#bedaff&lu&#c4d8ff&ln&#c9d5ff&la &#ced3ff&lW&#d3d0ff&lr&#d9cdff&la&#decbff&lp&#e3c8ff&ls"));


    @SuppressWarnings("DuplicatedCode") // I KNOW, I HAVE TO FIX LATER
    public static void init() {
        int[] roseBush = {0,1,7,8,18,19,25};
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
        ItemStack paper = GUIActions.createGuiItem(true,new ItemStack(Material.PAPER),"&#b9ddff&lI&#bddbff&lt&#c1d9ff&le&#c6d7ff&lm &#cad5ff&lW&#ced3ff&lr&#d2d0ff&la&#d6ceff&lp&#dbccff&lp&#dfcaff&le&#e3c8ff&lr");
        ItemMeta meta = paper.getItemMeta();
        meta.setCustomModelData(86);
        paper.setItemMeta(meta);
        gui.setItem(12, paper);
        gui.setItem(14, GUIActions.createGuiItem(true,new ItemStack(Material.BARRIER),"&#b9ddff&lW&#bddbff&lr&#c1d9ff&la&#c6d7ff&lp &#cad5ff&lR&#ced3ff&le&#d2d0ff&lm&#d6ceff&lo&#dbccff&lv&#dfcaff&le&#e3c8ff&lr"));
        gui.setItem(26, GUIActions.createGuiItem(true,new ItemStack(Material.BOOK), "&#b9ddff&lL&#bbdcff&lu&#bedbff&ln&#c0d9ff&la&#c3d8ff&l'&#c5d7ff&ls &#c8d6ff&lW&#cad4ff&lr&#cdd3ff&la&#cfd2ff&lp&#d2d1ff&lp&#d4cfff&li&#d7ceff&ln&#d9cdff&lg &#dcccff&lI&#decaff&ln&#e1c9ff&lf&#e3c8ff&lo"));
    }

    @EventHandler
    public void invClick (InventoryClickEvent event) {
        if (!event.getInventory().equals(gui)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        if (clickedItem.getType() == Material.PAPER) {
            WrapGUI.openInventory(player);
            player.sendMessage(Util.colorcode("&#b9ddffL&#bbdcffe&#bedbfft&#c0daff'&#c2d8ffs &#c5d7ffg&#c7d6ffe&#c9d5fft &#ccd4fft&#ced3ffo &#d0d1ffw&#d3d0ffr&#d5cfffa&#d7ceffp&#dacdffp&#dcccffi&#decaffn&#e1c9ffg&#e3c8ff!"));
        } else if (clickedItem.getType() == Material.BARRIER) {
            RemoveWrapGUI.openInventory(player);
            player.sendMessage(Util.colorcode("&#b9ddffH&#bbdcffe&#bddbffr&#bfdaffe&#c1d9ff'&#c3d8ffs &#c4d7fft&#c6d6ffh&#c8d5ffe &#cad4ffi&#ccd3fft&#ced3ffe&#d0d2ffm &#d2d1ffu&#d4d0ffn&#d6cfffw&#d8ceffr&#d9cdffa&#dbccffp&#ddcbffp&#dfcaffe&#e1c9ffr&#e3c8ff!"));
        } else if (clickedItem.getType() ==  Material.BOOK) {
            Bukkit.getServer().dispatchCommand(player, "lunahelp");
            player.closeInventory();
        }
    }

    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(gui);
    }
}
