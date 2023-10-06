package me.jsinco.solutilities.celestial.aries;

import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class CandleApplyGUI implements Listener {
    private static final SolUtilities plugin = SolUtilities.getPlugin();

    static List<Inventory> candleGUIs = new ArrayList<>();

    public static Inventory candleGUI() {
        Inventory candlegui = Bukkit.createInventory(null,27, Util.colorcode("&#ffb89c&lC&#ffb8a4&la&#ffb8ad&ln&#ffb8b5&ld&#ffb8bd&ll&#ffb8c6&le &#ffb9ce&lB&#ffb9d7&lu&#ffb9df&lr&#ffb9e7&ln&#ffb9f0&le&#ffb9f8&lr"));
        int[] roseBush = {0,1,7,8,18,19,25,26};
        int[] torchFlower = {2,4,6,20,22,24};
        int[] seaGrass = {3,5,21,23};
        int[] mediumAmethyst = {9,10,11,15,16,17};
        for (int bush : roseBush) {
            candlegui.setItem(bush, GUIActions.createGuiItem(false,new ItemStack(Material.ROSE_BUSH)," "));
        }
        for (int torch : torchFlower) {
            candlegui.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER)," "));
        }
        for (int grass : seaGrass) {
            candlegui.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS)," "));
        }
        for (int ameth : mediumAmethyst) {
            candlegui.setItem(ameth,GUIActions.createGuiItem(false,new ItemStack(Material.MEDIUM_AMETHYST_BUD)," "));
        }

        candlegui.setItem(13, GUIActions.createGuiItem(true, new ItemStack(Material.AMETHYST_CLUSTER), "&#ffb89c&lC&#ffb8ab&lo&#ffb8bb&ln&#ffb9ca&lf&#ffb9d9&li&#ffb9e9&lr&#ffb9f8&lm"));

        candleGUIs.add(candlegui);
        return candlegui;
    }




    private static void addCandle(ItemStack item, ItemStack candle) { // TODO: REDO GLOW CANDLES
        if (item.getType().isAir() || !candle.hasItemMeta()) return;
        ItemMeta candleMeta = candle.getItemMeta();
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer candleData = candleMeta.getPersistentDataContainer();
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

        boolean glowCandle = false;
        boolean alreadyGlowItem = false;
        if (candleData.has(new NamespacedKey(plugin.getSolItems(),"Glow"), PersistentDataType.STRING)) {
            glowCandle = true;
        }
        if (itemData.has(new NamespacedKey(plugin.getSolItems(),"Glow"), PersistentDataType.STRING)) {
            alreadyGlowItem = true;
        }
        if (!item.getType().equals(Material.ELYTRA) && glowCandle) {
            return;
        }


        List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        boolean solitem = false;
        for (int i = 0; i < itemLore.size(); i++) {
            if (itemLore.get(i).equals("§")) {
                if (glowCandle && !alreadyGlowItem){
                    itemLore.add(i, "§7Glow I§§");
                } else if (!glowCandle) {
                    itemLore.add(i, Util.colorcode(candleMeta.getPersistentDataContainer().get(new NamespacedKey(plugin.getSolItems(),"EnchantName"), PersistentDataType.STRING)) + "§§");
                }
                solitem = true;
                break;
            }
        }



        if (!solitem) { // this is incase the item is not already a custom item btw
            if (glowCandle && !alreadyGlowItem){
                itemLore.add(0, "§7Glow I§§");
            } else if (!glowCandle) {
                itemLore.add(0, Util.colorcode(candleMeta.getPersistentDataContainer().get(new NamespacedKey(plugin.getSolItems(),"EnchantName"), PersistentDataType.STRING)) + "§§");
            }
            itemLore.add(1,"§");
        }

        itemData.set(new NamespacedKey(plugin.getSolItems(),"Candled"), PersistentDataType.SHORT, (short) 2);

        if (!candleData.has(new NamespacedKey(plugin.getSolItems(),"Candle"), PersistentDataType.SHORT)) return;
        Set<NamespacedKey> candleKeys = candleData.getKeys();


        candleKeys.forEach(key -> {
            if (key.getKey().equalsIgnoreCase("Candle") || key.getKey().equalsIgnoreCase("EnchantName")) return;
            if (key.getKey().equalsIgnoreCase("Glow")) {
                itemData.set(new NamespacedKey(plugin.getSolItems(), "Glow"), PersistentDataType.STRING, candleData.get(new NamespacedKey(plugin.getSolItems(), "Glow"), PersistentDataType.STRING));
                return;
            }
            itemData.set(new NamespacedKey(plugin.getSolItems(), key.getKey()), PersistentDataType.SHORT, (short) 2);
        });

        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
    }

    @EventHandler
    public void candleGUIClick (InventoryClickEvent event) {
        if (!candleGUIs.contains(event.getInventory())) return;
        Inventory candleGUI = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;
        if (clickedItem.getType() == Material.ROSE_BUSH || clickedItem.getType() == Material.TORCHFLOWER ||
                clickedItem.getType() == Material.SEAGRASS || clickedItem.getType() == Material.MEDIUM_AMETHYST_BUD) {
            event.setCancelled(true);
        }

        else if (clickedItem.getType() == Material.AMETHYST_CLUSTER) {
            event.setCancelled(true);
            ItemStack item = candleGUI.getItem(12);
            ItemStack candle = candleGUI.getItem(14);

            if (item == null) return;
            addCandle(item, candle);
            candleGUI.setItem(14, null);
            player.sendMessage(Util.colorcode("&#ffb89cI &#ffb89eb&#ffb8a0u&#ffb8a3r&#ffb8a5n&#ffb8a7e&#ffb8a9d &#ffb8acy&#ffb8aeo&#ffb8b0u&#ffb8b2r &#ffb8b5c&#ffb8b7a&#ffb8b9n&#ffb8bbd&#ffb8bel&#ffb8c0e &#ffb8c2f&#ffb8c4o&#ffb8c7r &#ffb8c9y&#ffb9cbo&#ffb9cdu&#ffb9d0. &#ffb9d2W&#ffb9d4o&#ffb9d6n&#ffb9d9d&#ffb9dbe&#ffb9ddr &#ffb9dfw&#ffb9e2h&#ffb9e4a&#ffb9e6t &#ffb9e8i&#ffb9ebt &#ffb9edd&#ffb9efi&#ffb9f1d&#ffb9f4.&#ffb9f6.&#ffb9f8."));
        }
    }


    @EventHandler
    public void candleGUIClose (InventoryCloseEvent event) {
        if (!candleGUIs.contains(event.getInventory())) return;
        Inventory candleGUI = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (candleGUI.getItem(12) != null) player.getInventory().addItem(candleGUI.getItem(12));
        if (candleGUI.getItem(14) != null) player.getInventory().addItem(candleGUI.getItem(14));
        candleGUIs.remove(event.getInventory());
    }

    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(candleGUI());
    }
}
