package me.jsinco.solutilities.ranks;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class RanksMenuOpen implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Opening ranks menu!"));
        GUI.openPage(player);
        return true;
    }
}
