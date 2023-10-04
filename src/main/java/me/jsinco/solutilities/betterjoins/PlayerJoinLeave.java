package me.jsinco.solutilities.betterjoins;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Random;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class PlayerJoinLeave implements Listener {
    Messages_Effects messagesEffects = new Messages_Effects();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (isVanished(player)) {
            event.setJoinMessage(null);
            return;
        }

        String sounds = pl.getConfig().getString("Sounds");
        String particles = pl.getConfig().getString("ParticleType");
        boolean UseFirstJoinMsg = pl.getConfig().getBoolean("UseFirstJoin?");
        boolean UseTitle = pl.getConfig().getBoolean("UseTitle?");
        boolean UseActionBar = pl.getConfig().getBoolean("UseActionBar?");

        if (UseTitle) messagesEffects.sendTitle(player);
        if (UseActionBar) messagesEffects.sendActionBar(player);
        if (particles != null) messagesEffects.sendParticleEffect(player);
        if (sounds != null) messagesEffects.sendSound(player, sounds);

        if (!player.hasPlayedBefore() && UseFirstJoinMsg){
            Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add seed");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add player");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tags set rankseed " + player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " 1000");
            }, 20L);
            event.setJoinMessage(null); //disable default join message

            List<String> firstJoinMsgs = pl.getConfig().getStringList("FirstJoin");
            messagesEffects.sendFirstJoinMsg(player, firstJoinMsgs, 1);

        } else { // normal join
            String prefix = pl.getConfig().getString("JoinPrefix");

            List<String> AJoinMsg = pl.getConfig().getStringList("JoinMsg");
            int random = new Random().nextInt(AJoinMsg.size());

            if (prefix!= null) {
                event.setJoinMessage(ColorUtils.colorcode(prefix) + ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));

            } else {
                event.setJoinMessage(ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
            }

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if (isVanished(player)) {
            event.setQuitMessage(null);
            return;
        }

        String prefix = pl.getConfig().getString("QuitPrefix");
        List<String> AJoinMsg = pl.getConfig().getStringList("QuitMsg");
        int random = new Random().nextInt(AJoinMsg.size());

        if (prefix != null) {
            event.setQuitMessage(ColorUtils.colorcode(prefix) + ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
        } else {
            event.setQuitMessage(ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
        }

        if (pl.getConfig().getString("QuitSound") != null) messagesEffects.sendLeaveSound();
    }


    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}