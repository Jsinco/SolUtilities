package me.jsinco.solutilities;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AddEnchant implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (strings[0].equalsIgnoreCase("ariesprep")) {
            assert meta != null;
            meta.setLore(List.of("Aries Prep","",ColorUtils.colorcode("&#ff71e8&lC&#f07aee&le&#e283f4&ll&#d38cf9&le&#c495ff&ls&#b090fc&lt&#9b8af8&li&#8785f5&la&#727ff1&ll")));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
            return true;
        }

        try {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(strings[0]));
            int level = Integer.parseInt(strings[1]);

            assert enchantment != null;
            assert meta != null;
            meta.addEnchant(enchantment, level, true);
            item.setItemMeta(meta);
        } catch (Exception e) {
            player.sendMessage("Something went wrong!");
            return false;
        }

        return true;
    }
}
