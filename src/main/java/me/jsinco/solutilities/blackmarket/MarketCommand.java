package me.jsinco.solutilities.blackmarket;

import me.jsinco.oneannouncer.api.DiscordCommandManager;
import me.jsinco.solutilities.Saves;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.utility.UtilMethods;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class MarketCommand implements CommandExecutor, TabCompleter {


    public MarketCommand(SolUtilities plugin) {
        if (Saves.get().get("Blackmarket.Items") != null && plugin.getConfig().getBoolean("Blackmarket.Enabled")) {
            new Market().marketResetter(plugin);
        } else {
            plugin.getLogger().warning("The Black market was not started. Check if items are null or disabled in config.");
        }

        MarketItemPreview.initMarketItemPreviewGui();
        plugin.getCommand("blackmarket").setExecutor(this);
        plugin.getCommand("blackmarket").setTabCompleter(this);

        plugin.getServer().getPluginManager().registerEvents(new MarketItemPreview(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MarketGUI(), plugin);
        DiscordCommandManager.registerGlobalCommand(new BlackMarketNotifyCommand()); // discord command
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;



        // /blackmarket <currency> <price> <weight>
        if (player != null) {
            if (args.length < 1) {
                String cmd = pl.getConfig().getString("Blackmarket.BlackMarketDefaultCommand");
                if (cmd.contains("[P]")) {
                    Bukkit.dispatchCommand(player, cmd.replace("[P]", "").strip());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
                return true;
            }

            if (!player.hasPermission("solutilities.blackmarket.admin")) return true;

            switch (args[0].toLowerCase()) {
                case "add" -> {
                    if (args.length < 4) return false;
                    String currency = args[1].toLowerCase();
                    double price = Double.parseDouble(args[2].replace(",", "").strip());
                    int weight = Integer.parseInt(args[3]);
                    int stock = Integer.parseInt(args[4].replace(",", "").strip());
                    addItemBlackMarket(currency, price, weight, stock, player);
                }

                case "remove" -> {
                    if (args.length < 2 || args[1].equalsIgnoreCase("<itemInHand>")) {
                        removeItemBlackMarket(player, null);
                    } else {
                        removeItemBlackMarket(player, ignoreCapsItemNames(args[1]));
                    }
                }

                case "adjust" -> {
                    if (args.length < 5) return false;
                    String itemName = ignoreCapsItemNames(args[1]);
                    String currency = args[2].toLowerCase();
                    double price = Double.parseDouble(args[3].replace(",", "").strip());
                    int weight = Integer.parseInt(args[4]);
                    int stock = Integer.parseInt(args[5].replace(",", "").strip());
                    adjustItemBlackMarket(itemName, currency, price, weight, stock, player);
                }

                case "preview" -> MarketItemPreview.openInventory(player,0);
            }
        }

        if (args.length < 1) return false;
        switch (args[0].toLowerCase()) {
            case "market" -> {
                try {
                    Player target = Bukkit.getPlayerExact(args[2]);
                    if (target == null) return false;
                    int market = Integer.parseInt(args[1]);

                    if (Market.marketSelector(market)) {
                        MarketGUI.loadMarketGUI();
                        MarketGUI.openInventory(target);
                    } else if (new Random().nextBoolean()) {
                        target.sendMessage(Util.colorcode("&#df4a4aH&#dd4a4aa&#db494a! &#d9494aI&#d8484a'&#d6484am &#d4484an&#d2474ao&#d0474at &#ce474at&#cc464bh&#cb464be &#c9454bm&#c7454ba&#c5454br&#c3444bk&#c1444be&#bf434bt &#be434bk&#bc434be&#ba424be&#b8424bp&#b6424be&#b4414br&#b2414b, &#b1404bI&#af404bt&#ad404b'&#ab3f4bs&#a93f4c: "+Market.activeMarketNPCName(Market.seeActiveMarket())+" &#a73e4cF&#a53e4co&#a33e4cr &#a23d4ct&#a03d4ce&#9e3c4cl&#9c3c4cl&#9a3c4ci&#983b4cn&#963b4cg &#953b4cy&#933a4co&#913a4cu&#8f394c, &#8d394cI&#8b394c'&#89384cm &#88384ct&#86374da&#84374dk&#82374di&#80364dn&#7e364dg &#7c364da &#7b354df&#79354de&#77344de&#75344d!"));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + target.getName() + " 500");
                    } else {
                        target.sendMessage(Util.colorcode("&#df4a4aI&#db494a'&#d7484am &#d3474an&#cf474ao&#cb464bt &#c7454bt&#c2444bh&#be434be &#ba424bm&#b6424ba&#b2414br&#ae404bk&#aa3f4ce&#a63e4ct &#a23d4ck&#9e3c4ce&#9a3c4ce&#963b4cp&#923a4ce&#8d394cr&#89384c! &#85374dI&#81374dt&#7d364d'&#79354ds&#75344d: "+Market.activeMarketNPCName(Market.seeActiveMarket())+"&#75344d!"));
                    }

                } catch (Exception ex) {
                    sender.sendMessage("/blackmarket market <1-3> <player>");
                }
            }
            case "time" -> sender.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") +"Time until next market refresh: " + Market.getResetTime() + " mins"));
            case "reset" -> {
                Saves.get().set("Blackmarket.ResetTime", System.currentTimeMillis());
                Saves.save();
                sender.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Reset time set to now... (T-60s)"));
            }

            case "previewrl" -> {
                MarketItemPreview.initMarketItemPreviewGui();
                if (player != null) MarketItemPreview.openInventory(player,0);
            }

            case "cooldownreset","cdreset" -> {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) return false;
                else if (Saves.get().get("Blackmarket.Cooldown." + target.getUniqueId()) == null) {
                    sender.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + target.getName() + " does not have an active cooldown!"));
                    return true;
                }
                Saves.get().set("Blackmarket.Cooldown." + target.getUniqueId(), null);
                Saves.save();
                target.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Your Blackmarket cooldown has expired!"));
                sender.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Reset cooldown for " + target.getName()));
            }
        }
        return true;
    }

    private static String ignoreCapsItemNames(@NotNull String itemName) {
        List<String> itemNames = List.copyOf(Saves.get().getConfigurationSection("Blackmarket.Items").getKeys(false));
        for (String name : itemNames) {
            if (itemName.equalsIgnoreCase(name)) {
                itemName = name;
                break;
            }
        }
        return itemName;
    }

    private void addItemBlackMarket(@NotNull String currency, double price, int weight, int stock, Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Hold the specified item to add."));
            return;
        } else if (!currency.equals("dollar") && !currency.equals("solcoin")) {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Invalid currency! (dollar/solcoin)"));
        }
        String itemName;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            item = item.clone();
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(UtilMethods.defaultMinecraftColor(item, true) + UtilMethods.itemNameFromMaterial(item));
            item.setItemMeta(meta);

        }
        itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_") + "_" + item.getAmount();

        Saves.get().set("Blackmarket.Items." + itemName, item); // set the physical item
        Saves.get().set("Blackmarket.Weight." + itemName, weight); // set the weight of the item
        Saves.get().set("Blackmarket.DeadStocks." + itemName, stock);
        if (currency.equals("solcoin")) {
            Saves.get().set("Blackmarket." + currency + "." + itemName, (int) price);
        } else {
            Saves.get().set("Blackmarket." + currency + "." + itemName, price); // set the price of the item & currency
        }
        Saves.save();
        Saves.reload();

        TextComponent message = new TextComponent(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Item added to the blackmarket! [Click to reload preview]"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/blackmarket previewrl")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blackmarket previewrl"));
        player.sendMessage(message);
    }

    private void removeItemBlackMarket(Player player, @Nullable String itemName) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir() && itemName == null) {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "Hold the specified item to remove or specify it's name."));
            return;
        }

        String name;

        if (itemName != null) {
            ItemStack fileItem = Saves.get().getItemStack("Blackmarket.Items." + itemName);
            if (fileItem == null) {
                player.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Item not found!"));
                return;
            }
            name = itemName;

        } else {
            String physicalItemName = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_") + "_" + item.getAmount();
            ItemStack fileItem = Saves.get().getItemStack("Blackmarket.Items." + physicalItemName);
            if (fileItem == null) {
                player.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Item not found!"));
                return;
            }
            name = physicalItemName;
        }

        Saves.get().set("Blackmarket.Items." + name, null);
        Saves.get().set("Blackmarket.Weight." + name, null);
        Saves.get().set("Blackmarket.DeadStocks." + name, null);
        Saves.get().set("Blackmarket.dollar." + name, null);
        Saves.get().set("Blackmarket.solcoin." + name, null);
        Saves.save();
        Saves.reload();

        TextComponent message = new TextComponent(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Item removed from the blackmarket! [Click to reload preview]"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/blackmarket previewrl")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blackmarket previewrl"));
        player.sendMessage(message);
    }

    private void adjustItemBlackMarket(String itemName, String currency, double price, int weight, int stock, Player player) {
        if (!currency.equals("dollar") && !currency.equals("solcoin")) {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Invalid currency! (dollar/solcoin)"));
        }

        ItemStack fileItem = Saves.get().getItemStack("Blackmarket.Items." + itemName);
        if (fileItem == null) {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Item not found!"));
            return;
        }

        Saves.get().set("Blackmarket.dollar." + itemName, null);
        Saves.get().set("Blackmarket.solcoin." + itemName, null);
        Saves.save();

        Saves.get().set("Blackmarket.Weight." + itemName, weight);
        Saves.get().set("Blackmarket.DeadStocks." + itemName, stock);
        if (currency.equals("solcoin")) {
            Saves.get().set("Blackmarket." + currency + "." + itemName, (int) price);
        } else {
            Saves.get().set("Blackmarket." + currency + "." + itemName, price); // set the price of the item & currency
        }
        Saves.save();
        Saves.reload();

        TextComponent message = new TextComponent(Util.colorcode(pl.getConfig().getString("Blackmarket.Prefix") + "Item adjusted. "+currency+": "+price+" Weight: "+weight+" Default Stock: "+stock + " [Click to reload preview]"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/blackmarket previewrl")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blackmarket previewrl"));
        player.sendMessage(message);
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // /blackmarket add <currency> <price> <weight> <stock>
        // /blackmarket remove <itemname|iteminhand>
        if (!sender.hasPermission("solutilities.admin")) return null;
        if (args.length == 1) {
            return List.of("add", "remove", "adjust","preview", "market", "reset", "time", "previewrl","cooldownreset","cdreset");
        }
        if (args[0].equalsIgnoreCase("add")) {
            switch (args.length) {
                case 2 -> {
                    return List.of("dollar", "solcoin");
                }
                case 3 -> {
                    return List.of("<price>");
                }
                case 4 -> {
                    return List.of("<weight>");
                }
                case 5 -> {
                    return List.of("<stock>");
                }
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            List<String> returnThis = new ArrayList<>(getItemNamesLowerCase());
            returnThis.add("<itemInHand>");
            return returnThis;
        } else if (args[0].equalsIgnoreCase("adjust")) {
            switch (args.length) {
                case 2 -> {
                    return getItemNamesLowerCase();
                }
                case 3 -> {
                    return List.of("dollar", "solcoin");
                }
                case 4 -> {
                    return List.of("<price>");
                }
                case 5 -> {
                    return List.of("<weight/chance>");
                }
                case 6 -> {
                    return List.of("<stock>");
                }
            }

        } else if (args[0].equalsIgnoreCase("market") && args.length == 2) {
            return List.of(String.valueOf(Market.seeActiveMarket()));
        }


        return null;
    }

    private List<String> getItemNamesLowerCase() {
        List<String> itemNames = new ArrayList<>();
        for (String name : Saves.get().getConfigurationSection("Blackmarket.Items").getKeys(false)) {
            itemNames.add(name.toLowerCase());
        }
        return itemNames;
    }
}
