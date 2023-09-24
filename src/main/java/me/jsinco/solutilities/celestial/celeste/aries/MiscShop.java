package me.jsinco.solutilities.celestial.celeste.aries;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.utility.GUIActions;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
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
import java.util.List;
import java.util.Set;

public class MiscShop implements Listener {

    private static final ArrayList<Inventory> pages = new ArrayList<>();
    private final PlayerPointsAPI playerPoints;

    public MiscShop(PlayerPointsAPI playerPoints) {
        this.playerPoints = playerPoints;
    }

    public static void adminInitializeShop() {
        try {
            pages.forEach(page -> page.getViewers().forEach(humanEntity -> {
                humanEntity.closeInventory();
                humanEntity.sendMessage(ColorUtils.colorcode("&#ffb89cA&#ffb8a0n &#ffb8a4a&#ffb8a9d&#ffb8adm&#ffb8b1i&#ffb8b5n &#ffb8b9r&#ffb8bde&#ffb8c2l&#ffb8c6o&#ffb9caa&#ffb9ced&#ffb9d2e&#ffb9d7d &#ffb9dbt&#ffb9dfh&#ffb9e3e &#ffb9e7s&#ffb9ebh&#ffb9f0o&#ffb9f4p&#ffb9f8."));
            }));
            pages.clear();
            int leftOffItem = 0;

            Set<String> itemNames = MiscFile.get().getConfigurationSection("Items").getKeys(false);
            List<ItemStack> itemStacks = new ArrayList<>();
            itemNames.forEach(name -> itemStacks.add(MiscFile.get().getItemStack("Items." + name)));


            int putItem = 10;
            while (leftOffItem < itemNames.size()) {
                if (pages.isEmpty() || pages.get(pages.size() - 1).getItem(16) != null) {
                    pages.add(createPage());
                    putItem = 10;
                } else {
                    ItemStack itemStack = itemStacks.get(leftOffItem).clone();
                    ItemMeta meta = itemStack.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.add("");
                    lore.add("§7Left click: §6$" + String.format("%,d", MiscFile.get().getInt("Prices." + ChatColor.stripColor(meta.getDisplayName()).replace(" ","_"))));
                    lore.add("§7Right click: §f\uE54C§e" + MiscFile.get().getInt("SolCoins." + ChatColor.stripColor(meta.getDisplayName().replace(" ", "_"))));
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);


                    pages.get(pages.size() - 1).setItem(putItem, itemStack);
                    putItem++;
                    leftOffItem++;
                }
            }

        } catch (Exception e) {
            //
        }
    }


    public static Inventory createPage() {
        Inventory shop = Bukkit.createInventory(null,27, ColorUtils.colorcode("&#ffb89c&lA&#ffb8a8&lr&#ffb8b3&li&#ffb8bf&le&#ffb9ca&ls&#ffb9d6&l' &#ffb9e1&lB&#ffb9ed&la&#ffb9f8&lg"));

        int[] roseBush = {0,1,7,8,18,19,25,26};
        int[] torchFlower = {2,4,6,20,22,24};
        int[] seaGrass = {3,5,21,23};
        for (int bush : roseBush) {
            shop.setItem(bush, GUIActions.createGuiItem(false,new ItemStack(Material.ROSE_BUSH)," "));
        }
        for (int torch : torchFlower) {
            shop.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER)," "));
        }
        for (int grass : seaGrass) {
            shop.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS)," "));
        }

        shop.setItem(9,GUIActions.createGuiItem(true,new ItemStack(Material.LARGE_AMETHYST_BUD),"&#ffb89c&lB&#ffb8a9&la&#ffb8b6&lc&#ffb8c3&lk &#ffb9d1&lP&#ffb9de&la&#ffb9eb&lg&#ffb9f8&le"));
        shop.setItem(17,GUIActions.createGuiItem(true,new ItemStack(Material.LARGE_AMETHYST_BUD),"&#ffb89c&lN&#ffb8a9&le&#ffb8b6&lx&#ffb8c3&lt &#ffb9d1&lP&#ffb9de&la&#ffb9eb&lg&#ffb9f8&le"));
        return shop;
    }

    @EventHandler
    public void shopClick(InventoryClickEvent event) {
        if (!pages.contains(event.getInventory())) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.ROSE_BUSH || clickedItem.getType() == Material.TORCHFLOWER || clickedItem.getType() == Material.SEAGRASS) return;

        if (clickedItem.getType() == Material.LARGE_AMETHYST_BUD){
            try {
                switch (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName())) {
                    case "Next Page" -> openInventory(player, pages.indexOf(event.getInventory()) + 1);
                    case "Back Page" -> openInventory(player, pages.indexOf(event.getInventory()) - 1);
                }
            } catch (IndexOutOfBoundsException e) {
                player.sendMessage(ColorUtils.colorcode("&#ffb89cT&#ffb8a0h&#ffb8a3e&#ffb8a7r&#ffb8abe &#ffb8aea&#ffb8b2r&#ffb8b6e&#ffb8b9n&#ffb8bd'&#ffb8c1t &#ffb8c4a&#ffb8c8n&#ffb9ccy&#ffb9d0m&#ffb9d3o&#ffb9d7r&#ffb9dbe &#ffb9dep&#ffb9e2a&#ffb9e6g&#ffb9e9e&#ffb9eds&#ffb9f1.&#ffb9f4.&#ffb9f8."));
            }
            return;
        }

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).replace(" ","_");
        if (event.isLeftClick()) {
            purchaseItemMoney(player, itemName);
        } else {
            purchaseItemTropicoins(player, itemName);
        }

    }

    private void purchaseItemMoney(Player player, String itemName) {
        Economy economy = SolUtilities.getEconomy();
        int price = MiscFile.get().getInt("Prices." + itemName);
        ItemStack item = MiscFile.get().getItemStack("Items." + itemName);
        if (item == null) return;

        if (economy.getBalance(player) >= price) {
            item.setAmount(1);
            if (item.getAmount() > 1) {
                throw new IllegalArgumentException("Item amount cannot be greater than 1");
            }

            economy.withdrawPlayer(player, price);
            player.sendMessage(ColorUtils.colorcode("&#ffb89cN&#ffb8a1i&#ffb8a5c&#ffb8aae&#ffb8ae! &#ffb8b3T&#ffb8b8h&#ffb8bca&#ffb8c1n&#ffb8c5k&#ffb9cas &#ffb9cff&#ffb9d3o&#ffb9d8r &#ffb9dcb&#ffb9e1u&#ffb9e6y&#ffb9eai&#ffb9efn&#ffb9f3g&#ffb9f8."));
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(ColorUtils.colorcode("&#ffb89cP&#ffb8a0f&#ffb8a4f&#ffb8a8t&#ffb8ab, &#ffb8afI &#ffb8b3d&#ffb8b7o&#ffb8bbn&#ffb8bf'&#ffb8c2t &#ffb8c6u&#ffb9can&#ffb9ced&#ffb9d2e&#ffb9d6r&#ffb9d9s&#ffb9dde&#ffb9e1l&#ffb9e5l &#ffb9e9h&#ffb9ede&#ffb9f0r&#ffb9f4e&#ffb9f8."));
        }
    }

    private void purchaseItemTropicoins(Player player, String itemName) {
        int price = MiscFile.get().getInt("SolCoins." + itemName);
        ItemStack item = MiscFile.get().getItemStack("Items." + itemName);
        if (item == null) return;

        if (playerPoints.take(player.getUniqueId(), price)) {
            item.setAmount(1);
            if (item.getAmount() > 1) {
                throw new IllegalArgumentException("Item amount cannot be greater than 1");
            }


            player.sendMessage(ColorUtils.colorcode("&#ffb89cN&#ffb8a1i&#ffb8a5c&#ffb8aae&#ffb8ae! &#ffb8b3T&#ffb8b8h&#ffb8bca&#ffb8c1n&#ffb8c5k&#ffb9cas &#ffb9cff&#ffb9d3o&#ffb9d8r &#ffb9dcb&#ffb9e1u&#ffb9e6y&#ffb9eai&#ffb9efn&#ffb9f3g&#ffb9f8."));
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(ColorUtils.colorcode("&#ffb89cP&#ffb8a0f&#ffb8a4f&#ffb8a8t&#ffb8ab, &#ffb8afI &#ffb8b3d&#ffb8b7o&#ffb8bbn&#ffb8bf'&#ffb8c2t &#ffb8c6u&#ffb9can&#ffb9ced&#ffb9d2e&#ffb9d6r&#ffb9d9s&#ffb9dde&#ffb9e1l&#ffb9e5l &#ffb9e9h&#ffb9ede&#ffb9f0r&#ffb9f4e&#ffb9f8."));
        }
    }

    public static void openInventory(final HumanEntity ent, int page) {
        ent.openInventory(pages.get(page));
    }
}
