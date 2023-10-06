package me.jsinco.solutilities;

import me.jsinco.solutilities.utility.TagParticleVouchers;
import me.jsinco.solutilities.utility.UtilMethods;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolaceCMD implements CommandExecutor {

    private final SolUtilities plugin;

    public SolaceCMD(SolUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1)  return false;
        Player player = commandSender instanceof Player ? (Player) commandSender : null;

        switch (args[0].toLowerCase()) {
            case "armorstandhand" -> {
                if (!player.hasPermission("solutilities.armorstand") || !player.hasPermission("solutilities.admin")) {
                    player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "No permission!"));
                    return true;
                }
                UtilMethods.armorStandHands(player, false);
                return true;
            }
            case "armorstandhelm" -> {
                if (!player.hasPermission("solutilities.armorstand")  || !player.hasPermission("solutilities.admin")) {
                    player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "No permission!"));
                    return true;
                }
                UtilMethods.armorStandHands(player, true);
                return true;
            }
            case "discordreward" -> {
                if (!player.hasPermission("solutilities.booster")) {
                    player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "You aren't boosting our discord server, silly!"));
                    return true;
                }
                if (Saves.get().getString("DiscordReward.Players."+player.getUniqueId()) != null) {
                    player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "You have already claimed your discord reward!"));
                    return true;
                }

                Saves.get().set("DiscordReward.Players."+player.getUniqueId(), true);
                Saves.save();

                player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "You have claimed your discord reward!"));
                List<ItemStack> discordRewards = new ArrayList<>();
                Saves.get().getList("DiscordReward.Items").forEach(item -> discordRewards.add((ItemStack) item));
                for (ItemStack item : discordRewards) {
                    player.getInventory().addItem(item);
                }
                return true;
            }
            case "setdiscordreward"-> {
                if (player.hasPermission("solutilities.admin")) {
                    List<ItemStack> discordRewards = new ArrayList<>();
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        discordRewards.add(item);
                    }
                    Saves.get().set("DiscordReward.Items", discordRewards);
                    Saves.save();
                    player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "Set your inventory as the discord reward!"));
                    return true;
                }
            }
            case "wraptoken" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                ItemStack item = new ItemStack(Material.valueOf(args[1].toUpperCase()));
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "WrapToken"), PersistentDataType.SHORT, (short) 0);
                meta.setDisplayName(Util.colorcode(Arrays.toString(args).replace(args[0], "").replace(args[1], "")
                        .replace("[", "").replace(",", "").replace("]", "").strip()));
                meta.setLore(List.of("§7Use this to unlock a wrap from this set","§7@/wraps!","","§7Use your wrap to wrap your item @/celestial!"));
                meta.addEnchant(Enchantment.DURABILITY, 10, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
                return true;
            }
        }

        return false;
    }

}
