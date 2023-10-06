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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

import static me.jsinco.solutilities.celestial.luna.plugin;

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
    public void commandPreProcess(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().getScoreboardTags().contains("solutilities.silent") && !pl.getConfig().getStringList("SilentCommands").contains(event.getMessage().split(" ")[0])) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2f, 0.78f);
        }
        String m = event.getMessage();
        Player p = event.getPlayer();
        if (m.equals("/crates") && !p.hasPermission("excellentcrates.command")) {
            event.setMessage("/warp crates");
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
            } else {
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

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if (!event.getPlayer().hasPermission("solutilities.colorbooks")) return;
        BookMeta book = UtilMethods.formatBook(event.getNewBookMeta());

        // Update
        event.setNewBookMeta(book);
    }
}
