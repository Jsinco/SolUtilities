package me.jsinco.solutilities.betterjoins;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static me.jsinco.solutilities.betterjoins.PlayerJoinLeaveTest.messagesEffects;
import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class BetterJoinsCMD {

    public static void betterJoinsCMD(Player player, String arg) {
        if (player.hasMetadata("silentVanish")) {
            player.sendMessage(ColorUtils.colorcode(pl.getConfig().getString("prefix") + "Silent vanish metadata. Not sending a join/quit message."));
            player.removeMetadata("silentVanish", pl);
            return;
        }

        if (arg.equalsIgnoreCase("join")) {
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
                //event.setJoinMessage(null); //disable default join message

                List<String> firstJoinMsgs = pl.getConfig().getStringList("FirstJoin");
                messagesEffects.sendFirstJoinMsg(player, firstJoinMsgs, 1);

                //Saves.get().set("TotalJoins", Saves.get().getInt("TotalJoins") + 1); //count up
            } else { // normal join
                String prefix = pl.getConfig().getString("JoinPrefix");

                List<String> AJoinMsg = pl.getConfig().getStringList("JoinMsg");
                int random = new Random().nextInt(AJoinMsg.size());

                if (prefix!= null) {
                    Bukkit.broadcastMessage(ColorUtils.colorcode(prefix) + ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));

                } else {
                    Bukkit.broadcastMessage(ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
                }

            }

        } else if (arg.equalsIgnoreCase("quit")) {
            //PlayerJoinLeaveTest.TestPlayerQuit(player.getName());
            String prefix = pl.getConfig().getString("QuitPrefix");

            List<String> AJoinMsg = pl.getConfig().getStringList("QuitMsg");
            int random = new Random().nextInt(AJoinMsg.size());

            if (prefix != null) {
                Bukkit.broadcastMessage(ColorUtils.colorcode(prefix) + ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
            } else {
                Bukkit.broadcastMessage(ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
            }

            if (pl.getConfig().getString("QuitSound") != null) messagesEffects.sendLeaveSound();

        } else if (arg.equalsIgnoreCase("firstjoin")) {
            PlayerJoinLeaveTest.TestPlayerFirstJoin(player.getName(), 1);

        } else {
            player.sendMessage("§c/betterjoins <reload|join|quit|firstjoin> (Permission: betterjoins.admin)");
            player.sendMessage("§c/solace sounds - toggle sounds when joining or leaving");
        }

    }

    public static void betterJoinsSilent(Player player) {
        if (player.getScoreboardTags().contains("betterjoins.silent")) {
            player.removeScoreboardTag("betterjoins.silent");
        } else {
            player.addScoreboardTag("betterjoins.silent");
        }

    }
}
