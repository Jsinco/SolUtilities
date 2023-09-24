package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;
import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class TagParticleVouchers implements Listener {

    static List<Player> confirmList = new ArrayList<>();

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta.getPersistentDataContainer().has(new NamespacedKey(pl,"tagID"), PersistentDataType.STRING)) {
            playerTagRedeem(player);
            event.setCancelled(true);
        } else if (meta.getPersistentDataContainer().has(new NamespacedKey(pl,"particleID"), PersistentDataType.STRING)) {
            playerParticleRedeem(player);
            event.setCancelled(true);
        }
    }
    
    public static void playerTagRedeem(Player player) {
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta.getPersistentDataContainer().has(new NamespacedKey(pl,"tagID"), PersistentDataType.STRING)) {
            if (!confirmList.contains(player)) {
                confirmList.add(player);
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Are you sure you want to use this tag? &6Right-click &#E2E2E2again to confirm."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> confirmList.remove(player), 140L);
            } else {
                String tagID = meta.getPersistentDataContainer().get(new NamespacedKey(pl,"tagID"), PersistentDataType.STRING);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set eternaltags.tag." + tagID + " true");
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You have redeemed the " + meta.getDisplayName() + "&r&#E2E2E2!"));
                ItemStack tag = player.getInventory().getItemInMainHand().clone();
                tag.setAmount(1);
                player.getInventory().removeItem(tag);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }

    public static void playerParticleRedeem(Player player) {
        if (!confirmList.contains(player)) {
            confirmList.add(player);
            player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Are you sure you want to use this tag? &6Right-click &#E2E2E2again to confirm."));
            Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> confirmList.remove(player), 140L);
        } else {
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            String permissionNode = meta.getPersistentDataContainer().get(new NamespacedKey(pl,"particleID"), PersistentDataType.STRING);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set particle." + permissionNode + " true");
            ItemStack item = player.getInventory().getItemInMainHand().clone();
            item.setAmount(1);
            player.getInventory().removeItem(item);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
    }

    public static void createTag(Player player, String tagID, String tagName) {
        ItemStack tag = new ItemStack(Material.NAME_TAG);
        ItemMeta tagMeta = tag.getItemMeta();

        tagMeta.setDisplayName(ColorUtils.colorcode(tagName + " &r&7Tag"));
        tagMeta.setLore(List.of("§7Right click to redeem!"));
        tagMeta.getPersistentDataContainer().set(new NamespacedKey(pl,"tagID"), PersistentDataType.STRING, tagID);
        tagMeta.addEnchant(Enchantment.LUCK, 1, true);
        tagMeta.addItemFlags(HIDE_ENCHANTS);

        tag.setItemMeta(tagMeta);
        player.getInventory().addItem(tag);
    }

    public static void createParticleVoucher(String material, String permissionNode, Player player) {
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(pl, "particleID"), PersistentDataType.STRING, permissionNode);
        meta.setLore(List.of(ColorUtils.colorcode("&7Right click to redeem!")));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(HIDE_ENCHANTS,HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }
}
