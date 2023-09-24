package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class Healall implements CommandExecutor {

    List<String> cooldownPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (sender instanceof Player player && cooldownPlayers.contains(player.getName())) {
            player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You are on cooldown for this command!"));
        } else {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.setHealth(20);
                onlinePlayer.setFoodLevel(20);
                onlinePlayer.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You have been healed by " + sender.getName() + "!"));
            });
            Bukkit.broadcastMessage(ColorUtils.colorcode(pl.getConfig().getString("Healall.message")
                    .replace("%prefix%", pl.getConfig().getString("prefix"))
                    .replace("%sender%", sender.getName())));

            if (sender instanceof Player player) {
                cooldownPlayers.add(player.getName());
                Bukkit.getScheduler().runTaskLater(pl, () -> cooldownPlayers.remove(player.getName()), pl.getConfig().getLong("Healall.cooldown") * 20);
            }
        }


        return true;
    }
}
