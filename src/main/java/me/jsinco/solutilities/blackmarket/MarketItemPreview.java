package me.jsinco.solutilities.blackmarket;

import me.jsinco.solutilities.Saves;
import me.jsinco.solutilities.utility.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketItemPreview implements Listener {

    private static final List<Inventory> pages = new ArrayList<>();

    public static void initMarketItemPreviewGui() {
        try {
            pages.forEach(page -> page.getViewers().forEach(humanEntity -> {
                humanEntity.closeInventory();
                humanEntity.sendMessage(Util.colorcode("GUI was reloaded."));
            }));
            pages.clear();

            List<String> itemNames = new ArrayList<>(List.copyOf(Saves.get().getConfigurationSection("Blackmarket.Items").getKeys(false)));
            Collections.sort(itemNames);
            int putItem = 0;
            for (int i = 0; i < itemNames.size(); i++) {
                String itemName = itemNames.get(i);
                ItemStack item = Saves.get().getItemStack("Blackmarket.Items." + itemName).clone();

                if (pages.isEmpty() || pages.get(pages.size() - 1).getItem(44) != null) {
                    pages.add(createPage());
                    i--;
                    putItem = 0;
                } else {
                    if (item.getType().isAir() || !item.hasItemMeta()) {
                        Bukkit.getLogger().warning("Item " + itemName + " is invalid. Skipping...");
                        continue;
                    }

                    ItemMeta meta = item.getItemMeta();

                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                    lore.add("");
                    lore.add(Util.colorcode("&#accaf4▪ &#ddeceeWeight/Chance: &#accaf4" + Saves.get().getInt("Blackmarket.Weight." + itemName) + "%"));
                    lore.add(Util.colorcode("&#accaf4▪ &#ddeceeBase Stock: &#accaf4" + Saves.get().getInt("Blackmarket.DeadStocks." + itemName)));
                    if (Saves.get().getDouble("Blackmarket.dollar." + itemName) != 0) {
                        lore.add(Util.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4$" + String.format("%,.2f", Saves.get().getDouble("Blackmarket.dollar." + itemName))));
                    } else if (Saves.get().getInt("Blackmarket.solcoin." + itemName) != 0) {
                        lore.add(Util.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4\uE54C" + String.format("%,d", Saves.get().getInt("Blackmarket.solcoin." + itemName))));
                    }

                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    // need to do some stuff before putting in gui
                    pages.get(pages.size() - 1).setItem(putItem, item);
                    putItem++;
                }
            }

        } catch (Exception e){ // FIXME: Exception when opening gui with no items present
            e.printStackTrace();
        }
        Saves.reload();
    }
/*
items.put(itemName, BulkSaves.get().getItemStack("Blackmarket.Items." + itemName));
                weights.put(itemName, BulkSaves.get().getInt("Blackmarket.Weight." + itemName));
                stocks.put(itemName, BulkSaves.get().getInt("Blackmarket.DeadStocks." + itemName));

                if (BulkSaves.get().getDouble("Blackmarket.dollar." + itemName) != 0) {
                    prices.put(itemName, "&#accaf4▪ &#ddeceePrice: $&#accaf4" + BulkSaves.get().getDouble("Blackmarket.dollar." + itemName));
                } else if (BulkSaves.get().getInt("Blackmarket.solcoin." + itemName) != 0) {
                    prices.put(itemName, "&#accaf4▪ &#ddeceePrice: \uE54C&#accaf4" + BulkSaves.get().getInt("Blackmarket.solcoin." + itemName));
                }
 */

    private static Inventory createPage() {
        Inventory page = Bukkit.createInventory(null,54, Util.colorcode("&#df4a4a&lM&#ca464b&la&#b5414b&lr&#9f3d4c&lk&#8a384c&le&#75344d&lt"));

        for (int i = 45; i < 54; i++) {
            page.setItem(i, GUIActions.createNBTItem(false, "bmborder", 0, Material.BLACK_STAINED_GLASS_PANE, "&f"));
        }

        page.setItem(48, GUIActions.createNBTItem(true, "bmbackpage", 10041, Material.PAPER, "&#df4a4a&lB&#bc434b&la&#983b4c&lc&#75344d&lk"));
        page.setItem(49, GUIActions.createNBTItem(true, "bmresettime",0, Material.CLOCK, "&fTime until next market refresh: " + Market.getResetTime() + "mins"
        , "&fActive market: /blackmarket market " + Market.seeActiveMarket() + " <player>"));
        page.setItem(50, GUIActions.createNBTItem(true, "bmnextpage", 10043, Material.PAPER, "&#df4a4a&lN&#bc434b&le&#983b4c&lx&#75344d&lt"));
        return page;
    }

    private static void updateTime() {
        // "Time until next market refresh: " + Market.getResetTime() + " mins"
        pages.forEach(page -> {
            ItemStack item = page.getItem(49);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Util.colorcode("&fTime until next market refresh: " + Market.getResetTime() + "mins"));
            item.setItemMeta(meta);
            page.setItem(49, item);
        });
    }

    public static void openInventory(final HumanEntity ent, int page) {
        ent.openInventory(pages.get(page));
        updateTime();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!pages.contains(event.getInventory())) return;
        event.setCancelled(true);
        updateTime();

        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (item == null) return;

        if (!item.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {

            switch (ChatColor.stripColor(item.getItemMeta().getDisplayName())) {
                case "Next" -> {
                    if (pages.indexOf(event.getInventory()) + 1 == pages.size()) return;
                    openInventory(player, pages.indexOf(event.getInventory()) + 1);
                    return;
                }
                case "Back" -> {
                    if (pages.indexOf(event.getInventory()) == 0) return;
                    openInventory(player, pages.indexOf(event.getInventory()) - 1);
                    return;
                }
            }

            if (event.isLeftClick()) {
                String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_") + "_" + item.getAmount();
                ItemStack itemStack = Saves.get().getItemStack("Blackmarket.Items." + itemName);
                if (itemStack == null) return;
                player.getInventory().addItem(itemStack);
            } else {
                player.getInventory().addItem(item.clone());
            }

        }
    }
}
