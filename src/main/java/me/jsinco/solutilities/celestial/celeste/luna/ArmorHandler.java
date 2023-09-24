package me.jsinco.solutilities.celestial.celeste.luna;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class ArmorHandler implements Listener {
    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void itemDamageEvent(PlayerItemDamageEvent event) {
        ItemStack itemStack =  event.getItem();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl, "LUNA_NETHERITE"), PersistentDataType.SHORT)) {
            int chance = new Random().nextInt(8);
            if (chance != 1) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void PlayerDMGEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)||player.getInventory().getHelmet() == null||!player.getInventory().getHelmet().hasItemMeta()||
                !(player.getInventory().getHelmet().getType() == Material.PAPER)) return;
        ItemMeta meta = player.getInventory().getHelmet().getItemMeta();
        if (!meta.getPersistentDataContainer().has(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER)) return; // check if the paper has our nbt tag
        int unbreakingLevel = player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.DURABILITY);
        if (unbreakingLevel > 0) {
            int random = new Random().nextInt(100);
            if (random > 60 + 40 / (unbreakingLevel + 1)) { // unbreaking formula
                return;
            }
        }

        double armorDMG = event.getDamage() / 2; // I'm debugging, I don't know the rate at which normal armor takes dura dmg
        int currentDura = meta.getPersistentDataContainer().get(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER); // get the current dura
        if (currentDura - armorDMG <= 0) { // if the armor is less than or equal to 0, break the armor
            player.getInventory().setHelmet(null);
            player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,1,1);
            player.spawnParticle(Particle.ITEM_CRACK, player.getLocation(), 100, 0.5, 0.5, 0.5, 0.1, new ItemStack(Material.NETHERITE_HELMET));
            return;
        }

        meta.getPersistentDataContainer().set(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER, currentDura - (int) armorDMG); // set the new dura

        if (meta.getLore() == null) {
            meta.setLore(List.of("§7Durability: " + (currentDura - (int)armorDMG) + "/407"));
        } else {
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains("Durability:")) {
                    lore.set(i, "§7Durability: " + (currentDura - (int)armorDMG) + "/407");
                    meta.setLore(lore);
                    player.getInventory().getHelmet().setItemMeta(meta);
                    return;
                }
            }
            lore.add("§7Durability: " + (currentDura - (int)armorDMG) + "/407");
            meta.setLore(lore);
        }

        player.getInventory().getHelmet().setItemMeta(meta); // set the meta
    }


    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void expGainEvent(PlayerPickupExperienceEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> items = new ArrayList<>();
        if (player.getInventory().getHelmet() != null) {
            items.add(player.getInventory().getHelmet());
        }
        if (!player.getInventory().getItemInMainHand().getType().isAir()) {
            items.add(player.getInventory().getItemInMainHand());
        }
        if (!player.getInventory().getItemInOffHand().getType().isAir()) {
            items.add(player.getInventory().getItemInOffHand());
        }
        for (ItemStack item : items) {
            if (!item.hasItemMeta()) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER) &&
            item.getItemMeta().hasEnchant(Enchantment.MENDING)){

                ItemMeta meta = item.getItemMeta();
                int currentDura = meta.getPersistentDataContainer().get(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER);
                int exp = event.getExperienceOrb().getExperience() / 2;
                if (currentDura + exp > 407) {
                    exp = 407 - currentDura;
                }

                meta.getPersistentDataContainer().set(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER, currentDura + exp); // set the new dura

                if (meta.getLore() == null) {
                    meta.setLore(List.of("§7Durability: " + (currentDura + exp) + "/407"));
                } else {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        if (lore.get(i).contains("Durability:")) {
                            lore.set(i, "§7Durability: " + (currentDura + exp) + "/407");
                            meta.setLore(lore);
                            player.getInventory().getHelmet().setItemMeta(meta);
                            return;
                        }
                    }
                    lore.add("§7Durability: " + (currentDura + exp) + "/407");
                    meta.setLore(lore);
                }

                player.getInventory().getHelmet().setItemMeta(meta); // set the meta
            }
        }
    }


    @EventHandler
    public void onClickInHelmetSlot(InventoryClickEvent event){
        if (event.getInventory().getType() != InventoryType.CRAFTING) return;
        if(event.getSlotType().equals(InventoryType.SlotType.ARMOR) &&
                event.getWhoClicked().getItemOnCursor().getType() != Material.AIR &&
                event.getWhoClicked().getItemOnCursor() != event.getWhoClicked().getInventory().getHelmet()){

            Player player = (Player) event.getWhoClicked();
            ItemStack cursorItem = player.getItemOnCursor().clone(); //unknown if clone necessary
            ItemStack hatItem = player.getInventory().getHelmet(); //unknown if clone necessary
            if(cursorItem.hasItemMeta() && cursorItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl, "LunaCosmeticHelmet"), PersistentDataType.INTEGER)){
                event.setCancelled(true);
                player.setItemOnCursor(hatItem);
                player.getInventory().setHelmet(cursorItem);
                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
            }

        }
    }

    @EventHandler
    public void onRightClickArmor(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick() || event.getItem() == null || !event.getItem().hasItemMeta()
        || !event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(pl,"LunaCosmeticHelmet"), PersistentDataType.INTEGER)) return;
        Player player = event.getPlayer();

        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
            if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND)){
                player.getInventory().setItemInMainHand(player.getInventory().getHelmet());
            } else {
                player.getInventory().setItemInOffHand(player.getInventory().getHelmet());
            }
            player.getInventory().setHelmet(event.getItem());
        }, 1L);
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);

    }
}
