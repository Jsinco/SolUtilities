package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class InvisibleFrames implements Listener {

    private final List<Inventory> invisibleFramesGuis = new ArrayList<>();

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void entityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        if (!(event.getRightClicked() instanceof ItemFrame frame) || !p.isSneaking() || !p.getInventory().getItemInMainHand().getType().isAir()) return;
        event.setCancelled(true);
        Inventory inv = Bukkit.createInventory(null, 27, ColorUtils.colorcode("&#f76a3b&lI&#f7773b&lt&#f7833b&le&#f7903b&lm &#f79c3b&lF&#f8a93a&lr&#f8b53a&la&#f8c23a&lm&#f8ce3a&le&#f8db3a&ls"));

        if (frame.isVisible()) {
            inv.setItem(11, GUIActions.createGuiItem(false, new ItemStack(Material.ENDER_PEARL), ColorUtils.colorcode("&#f79a3bToggle Visibility")));
        } else {
            inv.setItem(11, GUIActions.createGuiItem(true, new ItemStack(Material.ENDER_PEARL), ColorUtils.colorcode("&#f79a3bToggle Visibility")));
        }

        ItemStack fixedPos;
        if (frame.isFixed()) {
            fixedPos = GUIActions.createGuiItem(true, new ItemStack(Material.BEDROCK), ColorUtils.colorcode("&#f79a3bFix Position"));
        } else {
            fixedPos = GUIActions.createGuiItem(false, new ItemStack(Material.BEDROCK), ColorUtils.colorcode("&#f79a3bFix Position"));
        }
        ItemMeta meta = fixedPos.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(pl, "itemFrameID"), PersistentDataType.STRING, frame.getUniqueId().toString());
        fixedPos.setItemMeta(meta);
        inv.setItem(15, fixedPos);

        invisibleFramesGuis.add(inv);
        p.openInventory(inv);
    }

    @EventHandler
    public void invClose(InventoryCloseEvent event) {
        if (!invisibleFramesGuis.contains(event.getInventory())) return;
        invisibleFramesGuis.remove(event.getInventory());
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (!invisibleFramesGuis.contains(event.getInventory())) return;
        event.setCancelled(true);

        Inventory inv = event.getInventory();
        ItemStack clicked = event.getCurrentItem();

        ItemStack fixedPos = event.getInventory().getItem(15);
        assert fixedPos != null;
        String itemFrameID = fixedPos.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(pl, "itemFrameID"), PersistentDataType.STRING);
        assert itemFrameID != null;
        ItemFrame frame = (ItemFrame) Bukkit.getEntity(java.util.UUID.fromString(itemFrameID));

        if (clicked == null) return;
        if (clicked.getType().equals(Material.ENDER_PEARL)) {
            assert frame != null;
            if (frame.isVisible()) {
                frame.setVisible(false);
                inv.setItem(11, GUIActions.createGuiItem(true, new ItemStack(Material.ENDER_PEARL), ColorUtils.colorcode("&#f79a3bToggle Visibility")));
            } else {
                frame.setVisible(true);
                inv.setItem(11, GUIActions.createGuiItem(false, new ItemStack(Material.ENDER_PEARL), ColorUtils.colorcode("&#f79a3bToggle Visibility")));
            }

        } else if (clicked.getType().equals(Material.BEDROCK)) {
            ItemMeta meta = fixedPos.getItemMeta();
            if (frame.isFixed()) {
                frame.setFixed(false);
                meta.removeEnchant(Enchantment.DIG_SPEED);
            } else {
                frame.setFixed(true);
                meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            }
            fixedPos.setItemMeta(meta);
        }
    }
}
