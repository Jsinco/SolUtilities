package me.jsinco.solutilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jsinco.solutilities.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PlaceHolders extends PlaceholderExpansion {
    private SolUtilities plugin = SolUtilities.getPlugin();

    @Override
    public @NotNull String getIdentifier() {
        return "solutilities";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Jsinco";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("welcomes")){
            return String.valueOf(Saves.get().getInt("Welcomes." + Bukkit.getOfflinePlayer(player.getName()).getUniqueId()));
        } else if (params.equals("prefix")) {
            return solPrefixes(player);
        }
        return null;
    }

    private String solPrefixes(Player player) {
        String prefix;
        if (player.hasPermission("solutilities.prefix.staff")) prefix = plugin.getConfig().getString("Prefixes.Staff");
        else return "";

        // yeah, I know you can use a loop but there's only a handful of prefixes and I don't really care anyway.
        String color;
        if(player.hasPermission("solutilities.color.owner")) color = plugin.getConfig().getString("Prefixes.Colors.owner");
        else if(player.hasPermission("solutilities.color.admin")) color = plugin.getConfig().getString("Prefixes.Colors.admin");
        else if(player.hasPermission("solutilities.color.mod")) color = plugin.getConfig().getString("Prefixes.Colors.mod");
        else if(player.hasPermission("solutilities.color.builder")) color = plugin.getConfig().getString("Prefixes.Colors.builder");
        else if(player.hasPermission("solutilities.color.helper")) color = plugin.getConfig().getString("Prefixes.Colors.helper");
        else if(player.hasPermission("solutilities.color.dev")) color = plugin.getConfig().getString("Prefixes.Colors.dev");
        else if(player.hasPermission("solutilities.color.content")) color = plugin.getConfig().getString("Prefixes.Colors.content");
        else if(player.hasPermission("solutilities.color.recruit")) color = plugin.getConfig().getString("Prefixes.Colors.recruit");
        else return "";

        return Util.colorcode(color + prefix);
    }
}
