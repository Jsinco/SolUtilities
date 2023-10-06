package me.jsinco.solutilities.misc.furniture;

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

public class FurnitureAdminGUI implements Listener {

    private static final List<Inventory> pages = new ArrayList<>();

    public static void initFurnitureAdminGui() {
        try {
            pages.forEach(page -> page.getViewers().forEach(humanEntity -> {
                humanEntity.closeInventory();
                humanEntity.sendMessage(Util.colorcode("This gui can only be viewed one player at a time."));
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
        Inventory page = Bukkit.createInventory(null,54, Util.colorcode("&#f76a3b&lF&#f7733b&lu&#f77d3b&lr&#f7863b&ln&#f7903b&li&#f7993b&lt&#f8a33b&lu&#f8ac3a&lr&#f8b53a&le &#f8bf3a&lE&#f8c83a&ld&#f8d23a&li&#f8db3a&lt"));

        for (int i = 45; i < 54; i++) {
            page.setItem(i, GUIActions.createGuiItem(false, new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&l"));
        }

        page.setItem(48, GUIActions.createGuiItem(true, new ItemStack(Material.ARROW), "&#f76a3bP&#f7743br&#f77f3be&#f7893bv&#f7933bi&#f79d3bo&#f8a83au&#f8b23as &#f8bc3aP&#f8c63aa&#f8d13ag&#f8db3ae"));
        page.setItem(50, GUIActions.createGuiItem(true, new ItemStack(Material.ARROW), "&l&#f76a3bN&#f77a3be&#f78a3bx&#f79a3bt &#f8ab3aP&#f8bb3aa&#f8cb3ag&#f8db3ae"));
        page.setItem(49, GUIActions.createGuiItem(true, new ItemStack(Material.CHEST), "&#f76a3bS&#f7903ba&#f8b53av&#f8db3ae", "§7Click to save edits"));

        return page;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!pages.contains(event.getInventory())) return;

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        switch (clickedItem.getType()) {
            case CHEST -> {
                List<ItemStack> furnitures = new ArrayList<>();
                for (Inventory page : pages) { // for every page in pages
                    for (int i = 0; i < page.getSize(); i++) { // for every item in page
                        ItemStack item = page.getItem(i);
                        if (item == null || item.getType().equals(Material.CHEST) || item.getType().equals(Material.GRAY_STAINED_GLASS_PANE) || item.getType().equals(Material.ARROW)) {
                            continue;
                        }
                        furnitures.add(item);
                    }
                }
                Saves.get().set("Furniture", furnitures);
                Saves.save();
                event.setCancelled(true);

                FurnitureGUI.initFurnitureGui();
                FurnitureGUI.openInventory(player, 0);
            }
            case ARROW -> { // overcomplicated code?

                switch (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName())) {
                    case "Next Page" ->  {
                        try {
                            openInventory(player, pages.indexOf(event.getInventory()) + 1);
                        } catch (IndexOutOfBoundsException ex) {
                            pages.add(createPage());
                            openInventory(player, pages.indexOf(event.getInventory()) + 1);
                        }
                    }
                    case "Previous Page" -> {
                        try {
                            openInventory(player, pages.indexOf(event.getInventory()) - 1);
                        } catch (IndexOutOfBoundsException ex) {
                            player.sendMessage(Util.colorcode(Util.prefix + "You are on the last page."));
                        }
                    }
                }
                event.setCancelled(true);
            }
            case GRAY_STAINED_GLASS_PANE -> event.setCancelled(true);
        }
    }



    public static void openInventory(final HumanEntity ent, int page) {
        ent.openInventory(pages.get(page));
    }
}
