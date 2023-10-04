package me.jsinco.solutilities;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jsinco.solutilities.utility.TagParticleVouchers;
import me.jsinco.solutilities.utility.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
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
            case "reload" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                plugin.reloadConfig();
                player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Config reloaded!"));
                return true;
            }
            case "sounds" -> {
                BetterJoinsCMD.betterJoinsSilent(player);
                player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Sounds toggled!"));
                return true;
            }
            case "betterjoins" -> {
                if (player.hasPermission("solutilities.admin") || player.hasPermission("solutilities.staff")) {
                    BetterJoinsCMD.betterJoinsCMD(player, args[1]);

                }
                return true;
            }
            case "createtagitem" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                TagParticleVouchers.createTag(player, args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                return true;
            }
            case "armorstandhand" -> {
                if (!player.hasPermission("solutilities.armorstand") || !player.hasPermission("solutilities.admin")) {
                    player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "No permission!"));
                    return true;
                }
                UtilMethods.armorStandHands(player, false);
                return true;
            }
            case "armorstandhelm" -> {
                if (!player.hasPermission("solutilities.armorstand")  || !player.hasPermission("solutilities.admin")) {
                    player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "No permission!"));
                    return true;
                }
                UtilMethods.armorStandHands(player, true);
                return true;
            }
            case "particlevoucher" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                TagParticleVouchers.createParticleVoucher(args[1].toUpperCase(), args[2], player);
            }
            case "discordreward" -> {
                if (!player.hasPermission("solutilities.booster")) {
                    player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You aren't boosting our discord server, silly!"));
                    return true;
                }
                if (Saves.get().getString("DiscordReward.Players."+player.getUniqueId()) != null) {
                    player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You have already claimed your discord reward!"));
                    return true;
                }

                Saves.get().set("DiscordReward.Players."+player.getUniqueId(), true);
                Saves.save();

                player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You have claimed your discord reward!"));
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
                    player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Set your inventory as the discord reward!"));
                    return true;
                }
            }
            case "rankupbroadcast" -> {
                if (commandSender instanceof Player) return false;
                Bukkit.broadcastMessage(ColorUtils.colorcode(String.join(" ", args).replace("rankupbroadcast ", "")));
                Bukkit.getOnlinePlayers().forEach(playerOnline -> {
                    if (!playerOnline.getScoreboardTags().contains("betterjoins.silent")){
                        playerOnline.playSound(playerOnline.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                });
                return true;
            }
            case "sudo" -> {
                if (!commandSender.hasPermission("solutilities.admin")) return false;
                if (args.length < 3) return false;
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) return false;
                String commandToExecute = String.join(" ", args).replace(args[0], "").replace(args[1], "").strip();
                Bukkit.broadcastMessage(commandToExecute);
                Bukkit.dispatchCommand(target, commandToExecute);
                return true;
            }
            case "wraptoken" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                ItemStack item = new ItemStack(Material.valueOf(args[1].toUpperCase()));
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "WrapToken"), PersistentDataType.SHORT, (short) 0);
                meta.setDisplayName(ColorUtils.colorcode(Arrays.toString(args).replace(args[0], "").replace(args[1], "")
                        .replace("[", "").replace(",", "").replace("]", "").strip()));
                meta.setLore(List.of("§7Use this to unlock a wrap from this set","§7@/wraps!","","§7Use your wrap to wrap your item @/celestial!"));
                meta.addEnchant(Enchantment.DURABILITY, 10, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
                return true;
            }
            case "cratekey" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                meta.addEnchant(Enchantment.DURABILITY, 10, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
            }
            case "nbtsee" -> {
                if (!player.hasPermission("solutilities.admin")) return false;
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    player.sendMessage("NameSpacedKeys: ");
                    player.sendMessage(meta.getPersistentDataContainer().getKeys().toString());
                }
                return true;
            }
            case "nickpreview" -> {
                if (args.length < 2) {
                    assert player != null;
                    player.sendMessage(ColorUtils.colorcode(PlaceholderAPI.setPlaceholders(player,"%solutilities_prefix%%eternaltags_tag% &7•&#E2E2E2 %player_displayname%&7:&#E2E2E2 This is how your nickname looks in chat!")));
                } else {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    commandSender.sendMessage(ColorUtils.colorcode(PlaceholderAPI.setPlaceholders(target,"%solutilities_prefix%%eternaltags_tag% &7•&#E2E2E2 %player_displayname%&7:&#E2E2E2 This is how "+ target.getName() +"'s nickname looks in chat!")));
                }
                return true;
            }
            case "ping" -> {
                if (args.length < 2) {
                    assert player != null;
                    player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + player.getPing() + "ms"));
                } else {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    commandSender.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + target.getPing() + "ms"));
                }
                return true;
            }
        }

        return false;
    }

}
