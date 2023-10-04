package me.jsinco.solutilities.celestial.luna;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomModel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (strings.length  > 0) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType().isAir() || !item.hasItemMeta()) return false;
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(Integer.parseInt(strings[0]));
            item.setItemMeta(meta);
        } else {
            player.sendMessage("Please specify a number");
        }

        return true;
    }
}
