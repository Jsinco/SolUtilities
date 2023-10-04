package me.jsinco.solutilities;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override // FIXME
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {

        if (command.getName().equalsIgnoreCase("solace")) {
            if (args.length == 1) {

                if (sender.hasPermission("solutilities.admin")) {
                    return List.of("reload", "sounds", "solcoin", "betterjoins","createtagitem","armorstandhand","armorstandhelm","particlevoucher","discordreward","sudo","nbtsee","nickpreview","ping");
                } else if (sender.hasPermission("solutilities.armorstand")) {
                    return List.of("sounds", "solcoin", "armorstandhand", "armorstandhelm","discordreward", "nickpreview");
                } else {
                    return List.of("sounds", "solcoin","discordreward", "nickpreview");
                }
            } else if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("betterjoins")) {
                    return List.of("join", "quit", "firstjoin");
                } else if (args[0].equalsIgnoreCase("solcoin")) {
                    return List.of("[<amount>]");
                } else if (args[0].equalsIgnoreCase("createtagitem")) {
                    if (args.length == 2) {
                        return List.of("[<tagID>]");
                    } else {
                        return List.of("[<tagName>]");
                    }

                }
            }
        }

        else if (command.getName().equalsIgnoreCase("welcomes") && args.length == 1) {
            return List.of("see", "toggle");
        }
        else if (command.getName().equalsIgnoreCase("say")) {
            return List.of("<message>");
        }

        else if (command.getName().equalsIgnoreCase("jobsperks")) {
            if (args.length == 1) {
                return List.of("Alchemist","Blacksmith","Builder","Fisherman","Cook","Digger","Farmer","Hunter","Lumberjack","Miner");
            }
        }

        return null;
    }
}
