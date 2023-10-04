package me.jsinco.solutilities.solace;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.utility.Saves;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class SolCoinWithdrawCMD {

    public static void solsWithdraw(Player player, String arg) {
        if (arg.equalsIgnoreCase("set") && player.hasPermission("solutilities.admin")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().isAir()) return;
            Saves.get().set("SolCoinItem", item);
            Saves.save();
            player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You have set the &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&#E2E2E2\uE54C item to the item in your hand!"));
            return;
        }

        try {
            if (Integer.parseInt(arg) < 1) {
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Integers must be greater than 0"));
                return;
            }
            if (SolUtilities.getPPAPI().take(player.getUniqueId(), Integer.parseInt(arg))) {
                ItemStack item = Saves.get().getItemStack("SolCoinItem");
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.getPersistentDataContainer().set(new NamespacedKey(pl, "SolCoins"), PersistentDataType.INTEGER, Integer.parseInt(arg));
                itemMeta.setDisplayName(ColorUtils.colorcode("&#F76A3B&l" + arg + " &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&f\uE54C"));
                itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                itemMeta.setLore(List.of("§7Right click to redeem!"));
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);


                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) == null) {
                        player.getInventory().addItem(item);
                        player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You have withdrawn " + arg + " &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&#E2E2E2\uE54C!"));
                        return;
                    }
                }
                player.getWorld().dropItem(player.getLocation(), item);
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You have withdrawn " + arg + " &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&#E2E2E2\uE54C! Your inv was full so it was dropped on the ground!"));
            } else {
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You do not have enough &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&#E2E2E2\uE54C!"));
            }
        } catch (Exception ex) {
            player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Something went wrong."));
        }

    }

}
