package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class Warps implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {
            case "set" -> {
                if (sender instanceof Player player && player.hasPermission("solutilities.warp.set") && args.length == 2) {
                    playerSetWarp(player, args[1]);
                }
            }

            case "remove" -> {
                if (sender.hasPermission("solutilities.warp.remove") && args.length == 2) {
                    playerRemoveWarp(sender, args[1]);
                }
            }

            default -> {
                if (sender instanceof Player player && player.hasPermission("solutilities.warp") && args.length == 1) {
                    playerTeleportWarp(player.getName(), args[0]);
                } else if (sender.hasPermission("solutilities.warp") && args.length == 2) {
                    playerTeleportWarp(args[1], args[0]);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("solwarp")) {
            List<String> warps;
            if (Saves.get().getConfigurationSection("Warps") != null) {
                 warps = new java.util.ArrayList<>(List.copyOf(Saves.get().getConfigurationSection("Warps").getKeys(false)));
            } else {
                warps = new ArrayList<>();
            }

            if (strings.length == 1) {
                warps.addAll(List.of("set", "remove"));
                return warps;
            }

            if (strings.length > 0) {
                if (strings[0].equalsIgnoreCase("set")) {
                    return List.of("<name>");
                } else if (strings[0].equalsIgnoreCase("remove")) {
                    return warps;
                }
            }
        }
        return null;
    }

    public void playerSetWarp(Player player, String name) {
        Saves.get().set("Warps." + name, player.getLocation());
        Saves.save();
        player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Warp &c" + name + " &#E2E2E2has been set!"));
    }

    public void playerRemoveWarp(CommandSender sender, String name) {
        Saves.get().set("Warps." + name, null);
        Saves.save();
        sender.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Warp &c" + name + " &#E2E2E2has been removed!"));
    }

    public void playerTeleportWarp(String playerName, String name) {
        Player player = Bukkit.getPlayerExact(playerName);

        if (player != null) {
            Location warp = Saves.get().getLocation("Warps." + name);
            if (warp != null) {
                player.teleport(warp);
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "You have been teleported to &c" + name + "&#E2E2E2!"));
            } else {
                player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "That warp does not exist!"));
            }
        }
    }
}
