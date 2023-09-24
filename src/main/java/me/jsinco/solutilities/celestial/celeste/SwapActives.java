package me.jsinco.solutilities.celestial.celeste;

import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.celestial.celeste.File;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SwapActives {
    // 259200000 = 3 days
    private static final SolUtilities plugin = SolUtilities.getPlugin(SolUtilities.class);

    public static void swapActives() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long BongOClock = File.get().getLong("BongOClock");

            if (BongOClock == 0) {
                File.get().set("BongOClock", System.currentTimeMillis() + 60000);
                File.save();
            } else if (System.currentTimeMillis() >= BongOClock) {
                List<ItemStack> itemStacks = new ArrayList<>();
                Set<String> itemNames = File.get().getConfigurationSection("Items").getKeys(false);
                itemNames.forEach(name -> itemStacks.add(File.get().getItemStack("Items." + name)));

                List<ItemStack> activeItemStacks = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    int random = new Random().nextInt(itemStacks.size());
                    if (!activeItemStacks.contains(itemStacks.get(random))) {
                        activeItemStacks.add(itemStacks.get(random));
                    } else {
                        i++;
                    }
                }


                File.get().set("ActiveItems", activeItemStacks);
                File.get().set("BongOClock", System.currentTimeMillis() + 60000);
                File.save();

                Bukkit.broadcastMessage("§6§l[§e§lCeleste§6§l] §eThe active items have been swapped!");
            }
        },0L, 400L);
    }
}
