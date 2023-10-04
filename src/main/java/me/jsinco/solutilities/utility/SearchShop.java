package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class SearchShop implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (strings.length == 0) {
            player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Specify an item to search for."));
            return true;
        }
        if (command.getName().equalsIgnoreCase("ls")){
            Bukkit.dispatchCommand(player, "searchshop TO_BUY " + strings[0].toUpperCase());
        } else if (command.getName().equalsIgnoreCase("lb")) {
            Bukkit.dispatchCommand(player, "searchshop TO_SELL " + strings[0].toUpperCase());
        }
        return true;
    }
}
