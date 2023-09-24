package me.jsinco.solutilities.ranks;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RankUpgrades implements CommandExecutor, Listener {

    private final SolUtilities plugin;

    public RankUpgrades(SolUtilities plugin) {
        this.plugin = plugin;
        plugin.getCommand("rankupgrade").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        RanksQueue.setup();
        RanksQueue.save();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[0]);

        String oldRank = strings[1].toLowerCase();
        String newRank = strings[2].toLowerCase();

        if (!offlinePlayer.isOnline()) {
            commandSender.sendMessage("§c" + offlinePlayer.getName() + " is not online! Adding to queue...");
            RanksQueue.get().set(offlinePlayer.getUniqueId().toString(), List.of(oldRank, newRank));
            RanksQueue.save();
            commandSender.sendMessage("§aAdded to queue!");
        } else {
            Player player = offlinePlayer.getPlayer();
            assert player != null;
            upgradeRank(player, oldRank, newRank);
        }


        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (RanksQueue.get().get(event.getPlayer().getUniqueId().toString()) == null) return;
        upgradeRank(event.getPlayer(), RanksQueue.get().getStringList(event.getPlayer().getUniqueId().toString()).get(0),
                RanksQueue.get().getStringList(event.getPlayer().getUniqueId().toString()).get(1));
    }


    private void upgradeRank(Player player, String oldRank, String newRank) {
        if (player.hasPermission("group."+oldRank)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove " + oldRank);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add " + newRank);
        } else {
            player.sendMessage("§cYou do not have the rank: " + oldRank + "! Please contact an admin!");

            List<String> notified = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.hasPermission("solutilities.admin")) {
                    p.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix")) + "§c" + player.getName() + " does not have the rank: " + oldRank + " they were not upgraded." );
                    notified.add(p.getName());
                }
            });

            plugin.getLogger().info(player.getName() + " does not have the rank: " + oldRank + " they were not upgraded." );
            plugin.getLogger().info("Notified: " + notified);
        }
        RanksQueue.get().set(player.getUniqueId().toString(), null);
        RanksQueue.save();
    }
}
