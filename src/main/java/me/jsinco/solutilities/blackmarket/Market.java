package me.jsinco.solutilities.blackmarket;

import me.jsinco.oneannouncer.DiscordSRVUtil;
import me.jsinco.oneannouncer.discord.JDAMethods;
import me.jsinco.solutilities.Saves;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.hooks.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Market {

    private static final SolUtilities plugin = SolUtilities.getPlugin();

    public static void refreshMarketItems() {
        List<String> itemNames = List.copyOf(Saves.get().getConfigurationSection("Blackmarket.Items").getKeys(false));

        // select items, determine weight, etc.
        // annoying to write
        List<ItemStack> selectedItems = new ArrayList<>();
        MAIN_LOOP: for (int i = 0; i < 3; i++) {
            int randomItem = new Random().nextInt(itemNames.size());
            ItemStack item = Saves.get().getItemStack("Blackmarket.Items." + itemNames.get(randomItem)).clone();
            int weight = Saves.get().getInt("Blackmarket.Weight." + itemNames.get(randomItem));
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
                if (Saves.get().getDouble("Blackmarket.dollar." + itemNames.get(randomItem)) != 0) {
                    lore.add(Util.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4$" + String.format("%,.2f", Saves.get().getDouble("Blackmarket.dollar." + itemNames.get(randomItem)))));
                } else if (Saves.get().getInt("Blackmarket.solcoin." + itemNames.get(randomItem)) != 0) {
                    lore.add(Util.colorcode("&#accaf4▪ &#ddeceePrice: &#accaf4\uE54C" + String.format("%,d", Saves.get().getInt("Blackmarket.solcoin." + itemNames.get(randomItem)))));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);

                selectedItems.add(item);
            } else {
                i--;
            }
        }

        Saves.get().set("Blackmarket.ActiveItems", null);
        Saves.get().set("Blackmarket.ActiveItemsStock", null);
        for (ItemStack selectedItem : selectedItems) {
            String name = ChatColor.stripColor(selectedItem.getItemMeta().getDisplayName().replace(" ", "_"))  + "_" + selectedItem.getAmount();
            Saves.get().set("Blackmarket.ActiveItems." + name, selectedItem);
            Saves.get().set("Blackmarket.ActiveItemsStock." + name, Saves.get().getInt("Blackmarket.DeadStocks." + name));
        }

        Saves.save();
        Saves.reload();
    }



    public static boolean marketPurchase(Player player, ItemStack purchasedItem) {
        if (Saves.get().get("Blackmarket.Cooldown." + player.getUniqueId()) != null) {
            if (Saves.get().getLong("Blackmarket.Cooldown." + player.getUniqueId()) > System.currentTimeMillis()) {
                player.sendMessage(Util.colorcode(plugin.getConfig().getString("Blackmarket.Prefix") + "You can only purchase items every 30 minutes! &7(Cooldown: " + ((Saves.get().getLong("Blackmarket.Cooldown." + player.getUniqueId()) - System.currentTimeMillis()) / 60000) + " minutes)"));
                return false;
            } else {
                Saves.get().set("Blackmarket.Cooldown." + player.getUniqueId(), null);
                Saves.save();
            }
        }


        Set<String> blackmarketItemNames = Saves.get().getConfigurationSection("Blackmarket.Items").getKeys(false);
        List<ItemStack> blackmarketItems = new ArrayList<>();

        List<Double> blackmarketPrice = new ArrayList<>();
        for (String itemName : blackmarketItemNames) {
            blackmarketItems.add(Saves.get().getItemStack("Blackmarket.Items." + itemName));
            if (Saves.get().get("Blackmarket.dollar." + itemName) != null) {
                blackmarketPrice.add(Saves.get().getDouble("Blackmarket.dollar." + itemName));
            } else {
                blackmarketPrice.add(Saves.get().getDouble("Blackmarket.solcoin." + itemName));
            }
        }

        String purchasedItemName = ChatColor.stripColor(purchasedItem.getItemMeta().getDisplayName()).replace(" ","_") + "_" + purchasedItem.getAmount();

        for (int i = 0; i < blackmarketItems.size(); i++) {
            String bmItemName = ChatColor.stripColor(blackmarketItems.get(i).getItemMeta().getDisplayName()).replace(" ","_") + "_" + blackmarketItems.get(i).getAmount();
            if (bmItemName.equals(purchasedItemName)) {
                if (Saves.get().getInt("Blackmarket.ActiveItemsStock." + purchasedItemName) <= 0) {
                    player.sendMessage(Util.colorcode(plugin.getConfig().getString("Blackmarket.Prefix") + "This item is out of stock!"));
                    return false;
                }

                if (Saves.get().get("Blackmarket.dollar." + purchasedItemName) != null) {
                    Economy economy = VaultHook.getEconomy();
                    if (economy.getBalance(player) < blackmarketPrice.get(i)) {
                        player.sendMessage(Util.colorcode(plugin.getConfig().getString("Blackmarket.Prefix") + "You don't have enough money!"));
                        return false;
                    }
                    economy.withdrawPlayer(player, blackmarketPrice.get(i));
                } else {
                    PlayerPointsAPI ppAPI = PlayerPoints.getInstance().getAPI();
                    int solcoinPrice = (int) Math.round(blackmarketPrice.get(i));
                    if (!ppAPI.take(player.getUniqueId(),  solcoinPrice)) {
                        player.sendMessage(Util.colorcode(plugin.getConfig().getString("Blackmarket.Prefix") + "You don't have enough Solcoins!"));
                        return false;
                    }
                }
                Saves.get().set("Blackmarket.ActiveItemsStock." + purchasedItemName, Saves.get().getInt("Blackmarket.ActiveItemsStock." + purchasedItemName) - 1);
                Saves.save();
                player.getInventory().addItem(blackmarketItems.get(i));
                player.sendMessage(Util.colorcode(plugin.getConfig().getString("Blackmarket.Prefix") + "You purchased a " + blackmarketItems.get(i).getItemMeta().getDisplayName() + "&#E2E2E2!"));

                Saves.get().set("Blackmarket.Cooldown." + player.getUniqueId(), System.currentTimeMillis() + 1800000); // 30 minutes
                Saves.save();
                return true;
            }
        }
        return false;
    }


    public void marketResetter(SolUtilities plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (System.currentTimeMillis() >= Saves.get().getLong("Blackmarket.ResetTime")) {
                MarketItemPreview.initMarketItemPreviewGui();
                refreshMarketItems();
                reselectMarket();
                Bukkit.broadcastMessage(Util.colorcode(plugin.getConfig().getString("Blackmarket.Message").replace("%prefix%", Market.plugin.getConfig().getString("Blackmarket.Prefix"))));

                long randomHour = new Random().nextLong(plugin.getConfig().getLong("Blackmarket.ResetMin"),plugin.getConfig().getLong("Blackmarket.ResetMax"));

                Saves.get().set("Blackmarket.ResetTime", System.currentTimeMillis()+randomHour*3600000);
                Saves.save();

                Set<String> notifyDiscordPlayers = Saves.get().getConfigurationSection("Blackmarket.NotifyDiscord").getKeys(false);
                for (String uuid: notifyDiscordPlayers) {
                    String discordID = DiscordSRVUtil.INSTANCE.getDiscordIDFromUUID(UUID.fromString(uuid));

                    if (discordID == null) continue;
                    JDAMethods.sendMessageDiscordUser(discordID, "**Market** » The Blackmarket has restocked!");
                }
            }
            if (Saves.get().getConfigurationSection("Blackmarket.Cooldown") == null || Saves.get().getConfigurationSection("Blackmarket.Cooldown").getKeys(false).isEmpty()) return;
            List<String> cooldownPlayers = new ArrayList<>(Saves.get().getConfigurationSection("Blackmarket.Cooldown").getKeys(false));
            for (String cooldownPlayer : cooldownPlayers) {
                if (Saves.get().getLong("Blackmarket.Cooldown." + cooldownPlayer) <= System.currentTimeMillis()) {
                    Saves.get().set("Blackmarket.Cooldown." + cooldownPlayer, null);
                    Saves.save();
                    Player player = Bukkit.getPlayer(UUID.fromString(cooldownPlayer));
                    if (player != null) {
                        player.sendMessage(Util.colorcode(Market.plugin.getConfig().getString("Blackmarket.Prefix") + "Your Blackmarket cooldown has expired!"));
                    }
                }
            }
        },0L, 1200L);
    }


    public static boolean marketSelector(int market) {
        return market == Saves.get().getInt("Blackmarket.ActiveMarket");
    }

    public static int seeActiveMarket() {
        return Saves.get().getInt("Blackmarket.ActiveMarket");
    }

    public static String reselectMarket() {
        int random = new Random().nextInt(1,4); // between 1 and 3
        Saves.get().set("Blackmarket.ActiveMarket", random);
        Saves.save();
        return "Market " + random + " selected!";
    }

    public static long getResetTime() {
        return (Saves.get().getLong("Blackmarket.ResetTime") - System.currentTimeMillis()) / 60000;
    }

    public static String activeMarketNPCName(int market) {
        String npcName = plugin.getConfig().getString("Blackmarket.Return-NPC-Name?.NPC"+market);
        if (npcName == null) {
            return "NPC" + market;
        }
        return npcName;
    }
}
