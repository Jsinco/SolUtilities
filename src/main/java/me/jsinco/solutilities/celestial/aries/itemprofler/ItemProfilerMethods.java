package me.jsinco.solutilities.celestial.aries.itemprofler;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.utility.UtilMethods;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemProfilerMethods {


    public static String profileItem(SolUtilities plugin, ItemStack item) {
        @NotNull Set<NamespacedKey> keys;

        try {
            keys = item.getItemMeta().getPersistentDataContainer().getKeys();
        } catch (NullPointerException e) {
            return plugin.ItemProfilesFile().getString("NoProfile");
        }

        for (NamespacedKey key : keys) {
            String profile = plugin.ItemProfilesFile().getString("Profiles." + key.getKey());

            if (profile != null) {
                return profile.replace("%itemname%", item.getItemMeta().getDisplayName());
            }
        }

        for (NamespacedKey key : keys) {
            String profile = plugin.ItemProfilesFile().getString("Profiles." + key.getNamespace());

            if (profile != null) {
                return profile.replace("%itemname%", item.getItemMeta().getDisplayName());
            }
        }

        return plugin.ItemProfilesFile().getString("NoProfile");
    }


    public static String setProfilePlaceholders(SolUtilities plugin, Player player, String string) {

        List<String> placeholders = List.copyOf(plugin.ItemProfilesFile().getConfigurationSection("Placeholders").getKeys(false));

        for (String s : placeholders) {
            if (string.contains(s)) {
                String placeholder = PlaceholderAPI.setPlaceholders(player, plugin.ItemProfilesFile().getString("Placeholders." + s));
                try {
                    string = string.replace(s, (int) UtilMethods.evalMath(placeholder) + "");
                } catch (Exception ex) {
                    string = string.replace(s, Util.colorcode(placeholder));
                }
            }
        }

        return string;
    }


    public static List<String> divideString(int divideWords, String string) {
        List<String> words = List.of(string.split(" "));
        List<String> dividedwords = new ArrayList<>();

        if (words.size() / divideWords == 0) {
            return null;
        }

        for (int i = 0; i < words.size() / divideWords; i++) {
            dividedwords.add(String.join(" ", words.subList(i * divideWords, (i + 1) * divideWords)));
        }
        int remainder = words.size() % divideWords;
        if (remainder != 0) {
            dividedwords.add(String.join(" ", words.subList(words.size() - remainder, words.size())));
        }

        return dividedwords;
    }
}
