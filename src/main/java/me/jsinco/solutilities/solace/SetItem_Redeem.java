package me.jsinco.solutilities.solace;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SetItem_Redeem implements Listener {
    private final SolUtilities plugin;
    private final PlayerPointsAPI ppAPI;

    public SetItem_Redeem(SolUtilities plugin, PlayerPointsAPI ppAPI) {
        this.plugin = plugin;
        this.ppAPI = ppAPI;
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item.getType().isAir() || !item.hasItemMeta() || !e.getAction().isRightClick()) return;
        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "SolCoins"), PersistentDataType.INTEGER)) {
            int amount = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "SolCoins"), PersistentDataType.INTEGER) * e.getPlayer().getInventory().getItemInMainHand().getAmount();
            ppAPI.give(e.getPlayer().getUniqueId(), amount);
            e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
            e.getPlayer().sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You have redeemed " + amount + " &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&#E2E2E2\uE54C!"));
        }

    }
}