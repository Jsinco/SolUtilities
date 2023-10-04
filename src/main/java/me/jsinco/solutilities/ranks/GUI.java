package me.jsinco.solutilities.ranks;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class GUI implements Listener {

    static private final List<Inventory> pages = new ArrayList<>();


    private static Inventory createPage(Player player) {
        Inventory page = Bukkit.createInventory(null, 54, ColorUtils.colorcode("&#f76a3b&lS&#f7753b&lo&#f7813b&ll&#f78c3b&la&#f7973b&lc&#f8a33b&le &#f8ae3a&lR&#f8b93a&la&#f8c43a&ln&#f8d03a&lk&#f8db3a&ls"));
        int[] whiteTulip = {0,8,45,53};
        int[] orangeTulip = {3,5,48,50};
        int[] torchFlower = {4,49};
        int[] seaGrass = {1,2,6,7,46,47,51,52};

        for (int tulip : whiteTulip) {
            page.setItem(tulip, GUIActions.createGuiItem(false,new ItemStack(Material.WHITE_TULIP),"§9"));
        }
        for (int tulip : orangeTulip) {
            page.setItem(tulip,GUIActions.createGuiItem(false,new ItemStack(Material.ORANGE_TULIP),"§9"));
        }
        for (int torch : torchFlower) {
            page.setItem(torch,GUIActions.createGuiItem(false,new ItemStack(Material.TORCHFLOWER),"§9"));
        }
        for (int grass : seaGrass) {
            page.setItem(grass,GUIActions.createGuiItem(false,new ItemStack(Material.SEAGRASS),"§9"));
        }

        int zeroHolder = 0;
        for (int i = 19; i < 26; i++) {
            String rankName = ColorUtils.colorcode(pl.getConfig().getStringList("Ranks").get(zeroHolder));

            List<String> rawLore = pl.getConfig().getStringList("RanksLore." + ChatColor.stripColor(rankName).toLowerCase());
            ItemStack item = GUIActions.createGuiItem(false, new ItemStack(Material.RED_CONCRETE_POWDER), rankName,"");
            ItemMeta meta = item.getItemMeta();
            List<String> rawDivider = pl.getConfig().getStringList("RanksDivider");
            List<String> divider = new ArrayList<>();
            rawDivider.forEach(line -> divider.add(ColorUtils.colorcode(line.replace("%rankprice%", pl.getConfig().getString("RanksPrices." + ChatColor.stripColor(rankName).toLowerCase())))));
            meta.setLore(divider);

            List<String> lore = meta.getLore();
            rawLore.forEach(line -> lore.add(ColorUtils.colorcode(line)));
            meta.setLore(lore);
            item.setItemMeta(meta);

            page.setItem(i, item);
            zeroHolder++;
        }
        zeroHolder = 0;
        for (int i = 29; i < 34; i++) {
            String rankName = ColorUtils.colorcode(pl.getConfig().getStringList("Ranks2").get(zeroHolder));

            List<String> rawLore = pl.getConfig().getStringList("RanksLore." + ChatColor.stripColor(rankName).toLowerCase());
            ItemStack item = GUIActions.createGuiItem(false, new ItemStack(Material.RED_CONCRETE_POWDER), rankName,"");
            ItemMeta meta = item.getItemMeta();
            List<String> rawDivider = pl.getConfig().getStringList("RanksDivider");
            List<String> divider = new ArrayList<>();
            rawDivider.forEach(line -> divider.add(ColorUtils.colorcode(line.replace("%rankprice%", pl.getConfig().getString("RanksPrices." + ChatColor.stripColor(rankName).toLowerCase())))));
            meta.setLore(divider);


            List<String> lore = meta.getLore();
            rawLore.forEach(line -> lore.add(ColorUtils.colorcode(line)));
            meta.setLore(lore);
            item.setItemMeta(meta);

            page.setItem(i, item);
            zeroHolder++;
        }


        int currentRank = getGroupNum(player);
        for (int i = currentRank; i > 0; i--) {
            if (page.getItem(i) != null && page.getItem(i).getType() == Material.RED_CONCRETE_POWDER) {
                page.getItem(i).setType(Material.LIME_CONCRETE_POWDER);
            }
        }
        for (int i = currentRank; i < page.getSize(); i++) {
            if (page.getItem(i) != null && page.getItem(i).getType() == Material.RED_CONCRETE_POWDER) {
                page.getItem(i).setType(Material.YELLOW_CONCRETE_POWDER);
                break;
            }
        }

        ItemStack ranksInfo = GUIActions.createGuiItem(true,new ItemStack(Material.BOOK),ColorUtils.colorcode(pl.getConfig().getString("RanksInfo"))," ");

        List<String> lore = new ArrayList<>();
        pl.getConfig().getStringList("RanksInfoLore").forEach(line -> lore.add(ColorUtils.colorcode(line)));
        ranksInfo.setLore(lore);
        page.setItem(13, ranksInfo);

        page.setItem(40, GUIActions.createGuiItem(false,new ItemStack(Material.NETHER_STAR),ColorUtils.colorcode("&#f76a3b&lP&#f7743b&lr&#f77f3b&le&#f7893b&lm&#f7933b&li&#f79d3b&lu&#f8a83a&lm &#f8b23a&lR&#f8bc3a&la&#f8c63a&ln&#f8d13a&lk&#f8db3a&ls"),
                        "§7Click to view our donator ranks!"));

        pages.add(page);
        return page;
    }

    private static int getGroupNum(Player player) {
        if (player.hasPermission("group.redwood")) return 33;
        else if (player.hasPermission("group.sequoia")) return 32;
        else if (player.hasPermission("group.cypress")) return 31;
        else if (player.hasPermission("group.spruce")) return 30;
        else if (player.hasPermission("group.maple")) return 29;
        else if (player.hasPermission("group.cherry")) return 25;
        else if (player.hasPermission("group.magnolia")) return 24;
        else if (player.hasPermission("group.elder")) return 23;
        else if (player.hasPermission("group.juniper")) return 22;
        else if (player.hasPermission("group.sapling")) return 21;
        else if (player.hasPermission("group.sprout")) return 20;
        else if (player.hasPermission("group.seed")) return 19;
        else return 0;
    }



    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (!pages.contains(event.getInventory())) return;
        pages.remove(event.getInventory());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!pages.contains(event.getInventory())) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        switch (clickedItem.getType()) {
            case YELLOW_CONCRETE_POWDER -> Bukkit.dispatchCommand(player, "rankup");
            case LIME_CONCRETE_POWDER -> player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You are already this rank!"));
            case RED_CONCRETE_POWDER -> player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You cannot rank up to this rank yet!"));
            case NETHER_STAR -> Bukkit.dispatchCommand(player, "premium");
        }
    }


    public static void openPage(Player player) {
        player.openInventory(createPage(player));
    }
}
