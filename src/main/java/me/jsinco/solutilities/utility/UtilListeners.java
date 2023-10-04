package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class UtilListeners implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        for (String entity : pl.getConfig().getStringList("DropBlocker.DeathWhitelist")) {
            if (event.getEntity().getType().equals(EntityType.valueOf(entity))) return;
        }
        for (String cause : pl.getConfig().getStringList("DropBlocker.CauseWhitelist")) {
            if (event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.valueOf(cause))) return;
        }
        if (event.getEntity().getKiller() != null) {
            for (String entity : pl.getConfig().getStringList("DropBlocker.KillerWhitelist")) {
                if (event.getEntity().getKiller().getType().equals(EntityType.valueOf(entity))) return;
            }
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().getName().endsWith("_nether") || e.getPlayer().getLocation().getY() < 127 || e.getPlayer().hasPermission("solutilities.netherroof.bypass"))
            return;

        Player player = e.getPlayer();
        Bukkit.dispatchCommand(player, pl.getConfig().getString("NetherRoof.dispatchCommand"));
        player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + pl.getConfig().getString("NetherRoof.message")));
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("NetherRoof.sound")), 1, 1), 1L);
    }

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().getScoreboardTags().contains("solutilities.silent") && !pl.getConfig().getStringList("SilentCommands").contains(event.getMessage().split(" ")[0])) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2f, 0.78f);
        }
        String m = event.getMessage();
        Player p = event.getPlayer();
        if (m.equals("/crates") && !p.hasPermission("excellentcrates.command")) {
            event.setMessage("/warp crates");
        } else if (m.contains("/premiumvanish")||m.contains("/vanish")||m.contains("/v")) {
            if (m.contains("-s") && p.hasPermission("solutilities.silentvanish")) {
                p.setMetadata("silentVanish", new FixedMetadataValue(pl, true));
            }
        }
    }

    @EventHandler
    public void onPlayerTP(PlayerTeleportEvent event) {
        if (!event.getPlayer().getScoreboardTags().contains("solutilities.silent") && event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) {
            Sound sound;
            float pitch;
            int random = new Random().nextInt(1,3);
            if (random == 2) {
                sound = Sound.BLOCK_PORTAL_TRAVEL;
                pitch = 8f;
            }
            else {
                sound = Sound.ENTITY_ENDERMAN_TELEPORT;
                pitch = 1f;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
                event.getPlayer().playSound(event.getPlayer().getLocation(), sound, 0.2f, pitch);
            }, 1L);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta()) return;
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        if (data.has(new NamespacedKey(pl, "WrapToken"), PersistentDataType.SHORT)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;
        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"model"), PersistentDataType.INTEGER)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Util.colorcode(pl.getConfig().getString("prefix")  + "This is a wrap! It should not be used as a normal item. Please speak to &#a8ff92Luna &#E2E2E2at [/celestial] to apply!"));
        } else if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"wraptoken"), PersistentDataType.SHORT)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Util.colorcode(pl.getConfig().getString("prefix")  + "This is a wrap token! Head to [/wraps] to exchange!"));
        }
    }

    //@EventHandler // temporary
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inv = event.getInventory();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);

            if (item != null && item.hasItemMeta()) {
                if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"model"), PersistentDataType.INTEGER)) {
                    ItemMeta meta = item.getItemMeta();

                    List<String> lore = new ArrayList<>(List.of("§fUse this item to wrap your","§ftools or armor!","", Util.colorcode("§fSpeak to Luna §fat &#a8ff92/celestial"),"§fto apply!"));

                    lore.addAll(List.of("", Util.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "),
                            Util.colorcode("&#EEE1D5Tier • &#b9ddff&lC&#bedaff&le&#c4d8ff&ll&#c9d5ff&le&#ced3ff&ls&#d3d0ff&lt&#d9cdff&li&#decbff&la&#e3c8ff&ll"),
                            Util.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")));
                    // ⋆⁺₊⋆ ★ ⋆⁺₊⋆
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ITEM_SPECIFICS,ItemFlag.HIDE_DYE,ItemFlag.HIDE_UNBREAKABLE);

                    item.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if (!event.getPlayer().hasPermission("solutilities.colorbooks")) return;
        BookMeta book = UtilMethods.formatBook(event.getNewBookMeta());

        // Update
        event.setNewBookMeta(book);
    }
}

/*
else if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"wraptoken"), PersistentDataType.SHORT)) {
                    ItemMeta meta = item.getItemMeta();


                    List<String> lore = new ArrayList<>(List.of("§fUse this token to purchase a",ColorUtils.colorcode("§fwrap from &#a8ff92/wraps§f!"),"",ColorUtils.colorcode("§fRight-click the &#a8ff92NPC §fwith the"),"§fsame name to exchange!"));

                    lore.addAll(List.of("", ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "),
                            ColorUtils.colorcode("&#EEE1D5Tier • &#b9ddff&lC&#bedaff&le&#c4d8ff&ll&#c9d5ff&le&#ced3ff&ls&#d3d0ff&lt&#d9cdff&li&#decbff&la&#e3c8ff&ll"),
                            ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")));

                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ITEM_SPECIFICS,ItemFlag.HIDE_DYE,ItemFlag.HIDE_UNBREAKABLE);

                    item.setItemMeta(meta);
                }




                @EventHandler
    public void onPlayerArmSwing(PlayerArmSwingEvent event) {
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"model"), PersistentDataType.INTEGER)) {
                ItemMeta meta = item.getItemMeta();


                List<String> lore = new ArrayList<>(List.of(ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "),ColorUtils.colorcode("      &#b9ddff&lL&#bfdaff&lu&#c5d7ff&ln&#cbd4ff&la &#d1d1ff&lW&#d7ceff&lr&#ddcbff&la&#e3c8ff&lp"),""
                        ,"§fUse this item to wrap your","§ftools or armor!","",ColorUtils.colorcode("§fSpeak to Luna §fat &#a8ff92/celestial"),"§fto apply!"));

                lore.addAll(List.of("", ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "),
                        ColorUtils.colorcode("&#EEE1D5Tier • &#b9ddff&lC&#bedaff&le&#c4d8ff&ll&#c9d5ff&le&#ced3ff&ls&#d3d0ff&lt&#d9cdff&li&#decbff&la&#e3c8ff&ll"),
                        ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")));

                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ITEM_SPECIFICS,ItemFlag.HIDE_DYE,ItemFlag.HIDE_UNBREAKABLE);

                item.setItemMeta(meta);
            } else if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"wraptoken"), PersistentDataType.SHORT)) {
                ItemMeta meta = item.getItemMeta();


                List<String> lore = new ArrayList<>(List.of("§fUse this token to purchase a",ColorUtils.colorcode("§fwrap from &#a8ff92/wraps§f!"),"",ColorUtils.colorcode("§fRight-click the &#a8ff92NPC §fwith the"),"§fsame name to exchange!"));

                lore.addAll(List.of("", ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "),
                        ColorUtils.colorcode("&#EEE1D5Tier • &#b9ddff&lC&#bedaff&le&#c4d8ff&ll&#c9d5ff&le&#ced3ff&ls&#d3d0ff&lt&#d9cdff&li&#decbff&la&#e3c8ff&ll"),
                        ColorUtils.colorcode("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")));

                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ITEM_SPECIFICS,ItemFlag.HIDE_DYE,ItemFlag.HIDE_UNBREAKABLE);

                item.setItemMeta(meta);
            }
        }
    }
 */
