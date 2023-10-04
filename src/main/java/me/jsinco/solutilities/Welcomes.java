package me.jsinco.solutilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class Welcomes implements Listener, CommandExecutor {

    boolean openForWelcomes = false;
    List<String> alreadyWelcomed = new ArrayList<>();
    String joinedPlayer = "";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        openForWelcomes = true;
        joinedPlayer = event.getPlayer().getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
            openForWelcomes = false;
            alreadyWelcomed.clear();
            joinedPlayer = "";
        }, 200L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerWelcome(AsyncPlayerChatEvent event) {
        if (!openForWelcomes) return;

        String message = event.getMessage();
        if (message.toLowerCase().contains("wb") || message.toLowerCase().contains("welcome")) {
            if (alreadyWelcomed.contains(event.getPlayer().getName()) || event.getPlayer().getScoreboardTags().contains("solwelcomes.off") ||
            joinedPlayer.equals(event.getPlayer().getName())) return;
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+1 Welcome point §7(Total: " + Saves.get().getInt("Welcomes." + event.getPlayer().getUniqueId()) + ")"));
            if (Saves.get().getConfigurationSection("Welcomes") == null) Saves.get().createSection("Welcomes");
            if (!Saves.get().contains("Welcomes." + event.getPlayer().getUniqueId())){
                Saves.get().set("Welcomes." + event.getPlayer().getUniqueId(), 1);
                Saves.save();
            }
            Saves.get().set("Welcomes." + event.getPlayer().getUniqueId(), Saves.get().getInt("Welcomes." + event.getPlayer().getUniqueId()) + 1);
            Saves.save();
            alreadyWelcomed.add(event.getPlayer().getName());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1 && commandSender instanceof Player player ) {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "You have "
                    + Saves.get().getInt("Welcomes." + player.getUniqueId()) + " welcome points!"));
            return true;
        }


        switch (args[0].toLowerCase()) {
            case "see" -> {
                try {
                    int points = Saves.get().getInt("Welcomes." + Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    commandSender.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + Bukkit.getOfflinePlayer(args[1]).getName()
                            + " has " + points + " welcome points!"));
                } catch (Exception e) {
                    commandSender.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "That player has no welcome points!"));
                }
            }
            case "toggle" -> {
                if (!(commandSender instanceof Player player)) return true;
                if (player.getScoreboardTags().contains("solwelcomes.off")) {
                    player.removeScoreboardTag("solwelcomes.off");
                    player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "You will now receive welcome points!"));
                } else {
                    player.addScoreboardTag("solwelcomes.off");
                    player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "You will no longer receive welcome points!"));
                }
            }
        }
        return true;
    }
}
