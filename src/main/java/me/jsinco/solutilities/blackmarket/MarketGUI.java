package me.jsinco.solutilities.blackmarket;

import me.jsinco.solutilities.BulkSaves;
import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class MarketGUI implements Listener {

    private static final Inventory marketGUI = Bukkit.createInventory(null, 45, ColorUtils.colorcode("&#df4a4a&lM&#ca464b&la&#b5414b&lr&#9f3d4c&lk&#8a384c&le&#75344d&lt"));
    // in 27 slot gui 11, 13, 15
    // in 54 slot gui 29, 31, 33
    @SuppressWarnings("DuplicatedCode")
    public static void loadMarketGUI() {
        List<String> blackmarketactiveItemNames = List.copyOf(BulkSaves.get().getConfigurationSection("Blackmarket.ActiveItems").getKeys(false));
        List<ItemStack> activeItems = new ArrayList<>();

        for (String blackmarketactiveItemName : blackmarketactiveItemNames) {
            activeItems.add(BulkSaves.get().getItemStack("Blackmarket.ActiveItems." + blackmarketactiveItemName));
        }

        for (ItemStack activeItem : activeItems) {
            ItemMeta meta = activeItem.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(pl, "BlackmarketItem"), PersistentDataType.BOOLEAN, true);
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            boolean editedStock = false;
            for (int i = 0; i < lore.size(); i++) {
                if (ChatColor.stripColor(lore.get(i)).contains("▪ Stock:")) {
                    lore.set(i, ColorUtils.colorcode("&#accaf4▪ &#ddeceeStock: &#accaf4" + BulkSaves.get().getInt("Blackmarket.ActiveItemsStock." + ChatColor.stripColor(activeItem.getItemMeta().getDisplayName()).replace(" ", "_") + "_" + activeItem.getAmount())));
                    editedStock = true;
                }
            }
            if (!editedStock) {
                lore.add(ColorUtils.colorcode("&#accaf4▪ &#ddeceeStock: &#accaf4" + BulkSaves.get().getInt("Blackmarket.ActiveItemsStock." + ChatColor.stripColor(activeItem.getItemMeta().getDisplayName()).replace(" ", "_") + "_" + activeItem.getAmount())));
            }

            meta.setLore(lore);
            activeItem.setItemMeta(meta);
        }

        marketGUI.setItem(20, activeItems.get(0));
        marketGUI.setItem(22, activeItems.get(1));
        marketGUI.setItem(24, activeItems.get(2));

        int[] whiteTulip = {0,8,36,44};
        int[] orangeTulip = {3,5,39,41};
        int[] torchFlower = {4,40};
        int[] seaGrass = {1,2,6,7,37,38,42,43};

        for (int tulip : whiteTulip) {
            marketGUI.setItem(tulip, GUIActions.createGuiItem(false,new ItemStack(Material.WHITE_TULIP),"§0"));
        }
        for (int tulip : orangeTulip) {
            marketGUI.setItem(tulip,GUIActions.createGuiItem(false,new ItemStack(Material.ORANGE_TULIP),"§0"));
        }
        for (int torch : torchFlower) {
            marketGUI.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER),"§0"));
        }
        for (int grass : seaGrass) {
            marketGUI.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS),"§0"));
        }
    }

    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(marketGUI);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(marketGUI)) return;
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (clickedItem == null || clickedItem.getType().isAir() || !clickedItem.hasItemMeta()) return;

        if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"BlackmarketItem"), PersistentDataType.BOOLEAN)) {
            if (Market.marketPurchase(player, clickedItem)) {
                loadMarketGUI();
            }
        }
    }

}
