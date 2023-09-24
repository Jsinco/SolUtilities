package me.jsinco.solutilities.utility;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SolRegions implements CommandExecutor {

    Block block1;
    Block block2;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        switch (strings[0].toUpperCase()) {
            case "SET1" -> {
                block1 = player.getTargetBlockExact(10);
            }

            case "SET2" -> {
                block2 = player.getTargetBlockExact(10);
            }

            case "FINAL" -> {
                if (block1 == null || block2 == null) return false;
                setRegion(block1, block2, strings[1]);
            }
        }

        return true;
    }

    private void setRegion(Block block1, Block block2, String regionName) {
        Saves.get().set("Regions." + regionName.toUpperCase() + ".loc1", block1.getLocation());
        Saves.get().set("Regions." + regionName.toUpperCase() + ".loc2", block2.getLocation());
        Saves.save();
    }
}
