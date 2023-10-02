package me.jsinco.solutilities.blackmarket;

import me.jsinco.oneannouncer.DiscordSRVUtil;
import me.jsinco.oneannouncer.discord.JDAMethods;
import me.jsinco.solutilities.BulkSaves;
import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class Market {
    public static void refreshMarketItems() {
        List<String> itemNames = List.copyOf(BulkSaves.get().getConfigurationSection("Blackmarket.Items").getKeys(false));

        // select items, determine weight, etc.
        // annoying to write
        List<ItemStack> selectedItems = new ArrayList<>();
        MAIN_LOOP: for (int i = 0; i < 3; i++) {
            int randomItem = new Random().nextInt(itemNames.size());
            ItemStack item = BulkSaves.get().getItemStack("Blackmarket.Items." + itemNames.get(randomItem)).clone();
            int weight = BulkSaves.get().getInt("Blackmarket.Weight." + itemNames.get(randomItem));
            if (item.getType().isAir() || !item.hasItemMeta()) {
                i--;
                continue;
            }

            for (ItemStack selectedItem : selectedItems) {
                if (selectedItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    i--;
                    continue MAIN_LOOP;
                }
            }

            if (new Random().nextInt(100) <= weight) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add("");
                if (BulkSaves.get().getDouble("Blackmarket.dollar." + itemNames.get(randomItem)) != 0) {
                    lore.add(ColorUtils.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4$" + String.format("%,.2f", BulkSaves.get().getDouble("Blackmarket.dollar." + itemNames.get(randomItem)))));
                } else if (BulkSaves.get().getInt("Blackmarket.solcoin." + itemNames.get(randomItem)) != 0) {
                    lore.add(ColorUtils.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4\uE54C" + String.format("%,d", BulkSaves.get().getInt("Blackmarket.solcoin." + itemNames.get(randomItem)))));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);

                selectedItems.add(item);
            } else {
                i--;
            }
        }

        BulkSaves.get().set("Blackmarket.ActiveItems", null);
        BulkSaves.get().set("Blackmarket.ActiveItemsStock", null);
        for (ItemStack selectedItem : selectedItems) {
            String name = ChatColor.stripColor(selectedItem.getItemMeta().getDisplayName().replace(" ", "_"))  + "_" + selectedItem.getAmount();
            BulkSaves.get().set("Blackmarket.ActiveItems." + name, selectedItem);
            BulkSaves.get().set("Blackmarket.ActiveItemsStock." + name, BulkSaves.get().getInt("Blackmarket.DeadStocks." + name));
        }

        BulkSaves.save();
        BulkSaves.reload();
    }



    public static boolean marketPurchase(Player player, ItemStack purchasedItem) {
        if (BulkSaves.get().get("Blackmarket.Cooldown." + player.getUniqueId()) != null) {
            if (BulkSaves.get().getLong("Blackmarket.Cooldown." + player.getUniqueId()) > System.currentTimeMillis()) {
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "You can only purchase items every 30 minutes! &7(Cooldown: " + ((BulkSaves.get().getLong("Blackmarket.Cooldown." + player.getUniqueId()) - System.currentTimeMillis()) / 60000) + " minutes)"));
                return false;
            } else {
                BulkSaves.get().set("Blackmarket.Cooldown." + player.getUniqueId(), null);
                BulkSaves.save();
            }
        }


        Set<String> blackmarketItemNames = BulkSaves.get().getConfigurationSection("Blackmarket.Items").getKeys(false);
        List<ItemStack> blackmarketItems = new ArrayList<>();

        List<Double> blackmarketPrice = new ArrayList<>();
        for (String itemName : blackmarketItemNames) {
            blackmarketItems.add(BulkSaves.get().getItemStack("Blackmarket.Items." + itemName));
            if (BulkSaves.get().get("Blackmarket.dollar." + itemName) != null) {
                blackmarketPrice.add(BulkSaves.get().getDouble("Blackmarket.dollar." + itemName));
            } else {
                blackmarketPrice.add(BulkSaves.get().getDouble("Blackmarket.solcoin." + itemName));
            }
        }

        String purchasedItemName = ChatColor.stripColor(purchasedItem.getItemMeta().getDisplayName()).replace(" ","_") + "_" + purchasedItem.getAmount();

        for (int i = 0; i < blackmarketItems.size(); i++) {
            String bmItemName = ChatColor.stripColor(blackmarketItems.get(i).getItemMeta().getDisplayName()).replace(" ","_") + "_" + blackmarketItems.get(i).getAmount();
            if (bmItemName.equals(purchasedItemName)) {
                if (BulkSaves.get().getInt("Blackmarket.ActiveItemsStock." + purchasedItemName) <= 0) {
                    player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "This item is out of stock!"));
                    return false;
                }

                if (BulkSaves.get().get("Blackmarket.dollar." + purchasedItemName) != null) {
                    Economy economy = SolUtilities.getEconomy();
                    if (economy.getBalance(player) < blackmarketPrice.get(i)) {
                        player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "You don't have enough money!"));
                        return false;
                    }
                    economy.withdrawPlayer(player, blackmarketPrice.get(i));
                } else {
                    PlayerPointsAPI ppAPI = PlayerPoints.getInstance().getAPI();
                    int solcoinPrice = (int) Math.round(blackmarketPrice.get(i));
                    if (!ppAPI.take(player.getUniqueId(),  solcoinPrice)) {
                        player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "You don't have enough Solcoins!"));
                        return false;
                    }
                }
                BulkSaves.get().set("Blackmarket.ActiveItemsStock." + purchasedItemName, BulkSaves.get().getInt("Blackmarket.ActiveItemsStock." + purchasedItemName) - 1);
                BulkSaves.save();
                player.getInventory().addItem(blackmarketItems.get(i));
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "You purchased a " + blackmarketItems.get(i).getItemMeta().getDisplayName() + "&#E2E2E2!"));

                BulkSaves.get().set("Blackmarket.Cooldown." + player.getUniqueId(), System.currentTimeMillis() + 1800000); // 30 minutes
                BulkSaves.save();
                return true;
            }
        }
        return false;
    }


    public void marketResetter(SolUtilities plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (System.currentTimeMillis() >= BulkSaves.get().getLong("Blackmarket.ResetTime")) {
                MarketItemPreview.initMarketItemPreviewGui();
                refreshMarketItems();
                reselectMarket();
                Bukkit.broadcastMessage(ColorUtils.colorcode(plugin.getConfig().getString("Blackmarket.Message").replace("%prefix%",pl.getConfig().getString("Blackmarket.Prefix"))));

                long randomHour = new Random().nextLong(plugin.getConfig().getLong("Blackmarket.ResetMin"),plugin.getConfig().getLong("Blackmarket.ResetMax"));

                BulkSaves.get().set("Blackmarket.ResetTime", System.currentTimeMillis()+randomHour*3600000);
                BulkSaves.save();

                Set<String> notifyDiscordPlayers = BulkSaves.get().getConfigurationSection("Blackmarket.NotifyDiscord").getKeys(false);
                for (String uuid: notifyDiscordPlayers) {
                    String discordID = DiscordSRVUtil.INSTANCE.getDiscordIDFromUUID(UUID.fromString(uuid));

                    if (discordID == null) continue;
                    JDAMethods.sendMessageDiscordUser(discordID, "**Market** » The Blackmarket has restocked!");
                }
            }
            if (BulkSaves.get().getConfigurationSection("Blackmarket.Cooldown") == null || BulkSaves.get().getConfigurationSection("Blackmarket.Cooldown").getKeys(false).isEmpty()) return;
            List<String> cooldownPlayers = new ArrayList<>(BulkSaves.get().getConfigurationSection("Blackmarket.Cooldown").getKeys(false));
            for (String cooldownPlayer : cooldownPlayers) {
                if (BulkSaves.get().getLong("Blackmarket.Cooldown." + cooldownPlayer) <= System.currentTimeMillis()) {
                    BulkSaves.get().set("Blackmarket.Cooldown." + cooldownPlayer, null);
                    BulkSaves.save();
                    Player player = Bukkit.getPlayer(UUID.fromString(cooldownPlayer));
                    if (player != null) {
                        player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Your Blackmarket cooldown has expired!"));
                    }
                }
            }
        },0L, 1200L);
    }


    public static boolean marketSelector(int market) {
        return market == BulkSaves.get().getInt("Blackmarket.ActiveMarket");
    }

    public static int seeActiveMarket() {
        return BulkSaves.get().getInt("Blackmarket.ActiveMarket");
    }

    public static String reselectMarket() {
        int random = new Random().nextInt(1,4); // between 1 and 3
        BulkSaves.get().set("Blackmarket.ActiveMarket", random);
        BulkSaves.save();
        return "Market " + random + " selected!";
    }

    public static long getResetTime() {
        return (BulkSaves.get().getLong("Blackmarket.ResetTime") - System.currentTimeMillis()) / 60000;
    }

    public static String activeMarketNPCName(int market) {
        String npcName = pl.getConfig().getString("Blackmarket.Return-NPC-Name?.NPC"+market);
        if (npcName == null) {
            return "NPC" + market;
        }
        return npcName;
    }
}
