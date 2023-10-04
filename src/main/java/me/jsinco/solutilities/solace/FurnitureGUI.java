package me.jsinco.solutilities.solace;

import me.jsinco.solutilities.Saves;
import me.jsinco.solutilities.Util;
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

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

@SuppressWarnings("DuplicatedCode")
public class FurnitureGUI implements Listener {

    private static final List<Inventory> pages = new ArrayList<>();

    public static void initFurnitureGui() {
        try {
            pages.forEach(page -> page.getViewers().forEach(humanEntity -> {
                humanEntity.closeInventory();
                humanEntity.sendMessage(Util.colorcode("Saved!"));
            }));
            pages.clear();
            int leftOffItem = 0;

            List<ItemStack> furnitures = new ArrayList<>();
            Saves.get().getList("Furniture").forEach(furniture -> furnitures.add((ItemStack) furniture));

            int putItem = 0;
            while (leftOffItem < furnitures.size()) {
                if (pages.isEmpty() || pages.get(pages.size() - 1).getItem(44) != null) {
                    pages.add(createPage());
                    putItem = 0;
                } else {
                    ItemStack itemStack = furnitures.get(leftOffItem);
                    pages.get(pages.size() - 1).setItem(putItem, itemStack);
                    putItem++;
                    leftOffItem++;
                }
            }

        } catch (Exception ignored){
        }
    }


    public static Inventory createPage() {
        Inventory page = Bukkit.createInventory(null,54, Util.colorcode("&#f76a3b&lF&#f7783b&lu&#f7863b&lr&#f7943b&ln&#f8a33b&li&#f8b13a&lt&#f8bf3a&lu&#f8cd3a&lr&#f8db3a&le"));

        for (int i = 45; i < 54; i++) {
            page.setItem(i, GUIActions.createGuiItem(false, new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&l"));
        }

        page.setItem(48, GUIActions.createGuiItem(true, new ItemStack(Material.ARROW), "&#f76a3bP&#f7743br&#f77f3be&#f7893bv&#f7933bi&#f79d3bo&#f8a83au&#f8b23as &#f8bc3aP&#f8c63aa&#f8d13ag&#f8db3ae"));
        page.setItem(50, GUIActions.createGuiItem(true, new ItemStack(Material.ARROW), "&l&#f76a3bN&#f77a3be&#f78a3bx&#f79a3bt &#f8ab3aP&#f8bb3aa&#f8cb3ag&#f8db3ae"));
        return page;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!pages.contains(event.getInventory())) return;
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        if (clickedItem.getType().equals(Material.ARROW)) {
            try {
                switch (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName())) {
                    case "Next Page" -> openInventory(player, pages.indexOf(event.getInventory()) + 1);
                    case "Previous Page" -> openInventory(player, pages.indexOf(event.getInventory()) - 1);
                }
            } catch (IndexOutOfBoundsException e) {
                player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "You are on the last page."));
            }
        }
    }



    public static void openInventory(final HumanEntity ent, int page) {
        ent.openInventory(pages.get(page));
    }
}
