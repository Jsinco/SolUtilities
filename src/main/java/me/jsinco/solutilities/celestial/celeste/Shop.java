package me.jsinco.solutilities.celestial.celeste;

import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.celestial.CelestialFile;
import me.jsinco.solutilities.hooks.VaultHook;
import me.jsinco.solutilities.utility.GUIActions;
import net.milkbowl.vault.economy.Economy;
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

public class Shop implements Listener {

    private static final ArrayList<Inventory> pages = new ArrayList<>();
    public Shop() {
        adminInitializeShop();
    }

    public static void adminInitializeShop() {
        try {
            pages.forEach(page -> page.getViewers().forEach(humanEntity -> {
                humanEntity.closeInventory();
                humanEntity.sendMessage(Util.colorcode("&#ffc8c8A&#ffcac8n &#ffccc8a&#ffcec8d&#ffd0c8m&#ffd2c7i&#ffd4c7n &#ffd6c7f&#ffd8c7o&#ffdac7r&#ffdcc7c&#ffdec7e &#ffe0c7r&#ffe3c7e&#ffe5c6l&#ffe7c6o&#ffe9c6a&#ffebc6d&#ffedc6e&#ffefc6d &#fff1c6t&#fff3c6h&#fff5c5e &#fff7c5s&#fff9c5h&#fffbc5o&#fffdc5p"));
            }));
            pages.clear();
            int leftOffItem = 0;

            List<String> itemNames = new ArrayList<>(List.copyOf(CelestialFile.get().getConfigurationSection("Items").getKeys(false)));
            Collections.sort(itemNames);
            List<ItemStack> itemStacks = new ArrayList<>();
            itemNames.forEach(name -> itemStacks.add(CelestialFile.get().getItemStack("Items." + name)));

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
                    lore.add(Util.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4$") + String.format("%,d", CelestialFile.get().getInt("Prices." + ChatColor.stripColor(meta.getDisplayName()).replace(" ","_"))));
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
        Inventory shop = Bukkit.createInventory(null,27, Util.colorcode("&#ffc8c8&lC&#ffd1c8&le&#ffdac7&ll&#ffe3c7&le&#ffebc6&ls&#fff4c6&lt&#fffdc5&le"));

        int[] roseBush = {0,1,7,8,18,19,25,26};
        int[] torchFlower = {2,4,6,20,22,24};
        int[] seaGrass = {3,5,21,23};
        for (int bush : roseBush) {
            shop.setItem(bush,GUIActions.createGuiItem(false,new ItemStack(Material.ROSE_BUSH)," "));
        }
        for (int torch : torchFlower) {
            shop.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER)," "));
        }
        for (int grass : seaGrass) {
            shop.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS)," "));
        }

        shop.setItem(9,GUIActions.createGuiItem(true,new ItemStack(Material.LARGE_AMETHYST_BUD),"&#ffc8c8&lB&#ffd0c8&la&#ffd7c7&lc&#ffdfc7&lk &#ffe6c6&lP&#ffeec6&la&#fff5c5&lg&#fffdc5&le"));
        shop.setItem(17,GUIActions.createGuiItem(true,new ItemStack(Material.LARGE_AMETHYST_BUD),"&#ffc8c8&lN&#ffd0c8&le&#ffd7c7&lx&#ffdfc7&lt &#ffe6c6&lP&#ffeec6&la&#fff5c5&lg&#fffdc5&le"));
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
                player.sendMessage(Util.colorcode("&#ffc8c8Y&#ffcbc8o&#ffcdc8u&#ffd0c8'&#ffd3c7r&#ffd5c7e &#ffd8c7o&#ffdbc7n &#ffddc7t&#ffe0c7h&#ffe3c7e &#ffe5c6f&#ffe8c6i&#ffeac6n&#ffedc6a&#fff0c6l &#fff2c6p&#fff5c5a&#fff8c5g&#fffac5e&#fffdc5!"));
            }
            return;
        }

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).replace(" ","_");
        purchaseItem(player, itemName);
    }


    private void purchaseItem(Player player, String itemName) {
        Economy economy = VaultHook.getEconomy();
        int price = CelestialFile.get().getInt("Prices." + itemName);
        ItemStack item = CelestialFile.get().getItemStack("Items." + itemName);
        if (item == null) return;

        if (economy.getBalance(player) >= price) {
            item.setAmount(1);
            if (item.getAmount() > 1) {
                throw new IllegalArgumentException("Item amount cannot be greater than 1");
            }

            economy.withdrawPlayer(player, price);
            player.sendMessage(Util.colorcode("&#ffc8c8T&#ffc9c8h&#ffcbc8a&#ffccc8n&#ffcec8k &#ffcfc8y&#ffd0c8o&#ffd2c7u &#ffd3c7s&#ffd5c7o &#ffd6c7m&#ffd7c7u&#ffd9c7c&#ffdac7h &#ffdcc7f&#ffddc7o&#ffdec7r &#ffe0c7b&#ffe1c7u&#ffe3c7y&#ffe4c6i&#ffe5c6n&#ffe7c6g&#ffe8c6! &#ffe9c6I &#ffebc6h&#ffecc6o&#ffeec6p&#ffefc6e &#fff0c6y&#fff2c6o&#fff3c6u &#fff5c5l&#fff6c5i&#fff7c5k&#fff9c5e &#fffac5i&#fffcc5t&#fffdc5!"));
            player.getInventory().addItem(item);
        } else {
            player.sendMessage(Util.colorcode("&#ffc8c8U&#ffc9c8h&#ffcbc8m&#ffccc8, &#ffcec8I &#ffcfc8d&#ffd1c8o&#ffd2c7n&#ffd3c7'&#ffd5c7t &#ffd6c7t&#ffd8c7h&#ffd9c7i&#ffdbc7n&#ffdcc7k &#ffddc7y&#ffdfc7o&#ffe0c7u &#ffe2c7h&#ffe3c6a&#ffe5c6v&#ffe6c6e &#ffe8c6e&#ffe9c6n&#ffeac6o&#ffecc6u&#ffedc6g&#ffefc6h &#fff0c6f&#fff2c6o&#fff3c6r &#fff4c5t&#fff6c5h&#fff7c5a&#fff9c5t&#fffac5.&#fffcc5.&#fffdc5."));
        }
    }



    public static void openInventory(final HumanEntity ent, int page) {
        ent.openInventory(pages.get(page));
    }

    /*
    private static List<ItemStack> sortByPrice(List<ItemStack> items) {
        List<String> itemsNamesSorted = new ArrayList<>();
        List<ItemStack> itemStacksSorted = new ArrayList<>();

        Set<String> pricesRaw = File.get().getConfigurationSection("Prices").getKeys(false);
        List<String> pricesRawList = new ArrayList<>(pricesRaw);
        List<Integer> prices = new ArrayList<>();
        for (String priceOfItem : pricesRawList) {
            prices.add(File.get().getInt("Prices." + priceOfItem));
        }

        for (int i = 0; i < pricesRaw.size(); i++){
            itemsNamesSorted.add(pricesRawList.get(prices.indexOf(Collections.min(prices))));
            pricesRawList.remove(prices.indexOf(Collections.min(prices)));
            prices.remove(Collections.min(prices));
        }

        for (String name : itemsNamesSorted) {
            itemStacksSorted.add(items.get(itemsNamesSorted.indexOf(name)));
        }

        return itemStacksSorted;
    }
     */
}
