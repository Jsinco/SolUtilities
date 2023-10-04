package me.jsinco.solutilities.betterjoins;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class PlayerJoinLeaveTest {
    static Messages_Effects messagesEffects = new Messages_Effects();

    public static void TestPlayerFirstJoin(String playerName, int incriment){
        Player player = Bukkit.getPlayerExact(playerName);

        List<String> firstJoinMsgs = pl.getConfig().getStringList("FirstJoin");
        messagesEffects.sendFirstJoinMsg(player, firstJoinMsgs, incriment);
    }

    @SuppressWarnings("DuplicatedCode")
    public static void TestPlayerJoin(String playerName){
        Player player = Bukkit.getPlayerExact(playerName);

        boolean UseTitle = pl.getConfig().getBoolean("UseTitle?");
        boolean UseActionBar = pl.getConfig().getBoolean("UseActionBar?");
        String sounds = pl.getConfig().getString("Sounds");
        String particles = pl.getConfig().getString("ParticleType");

        String prefix = pl.getConfig().getString("JoinPrefix");

        List<String> AJoinMsg = pl.getConfig().getStringList("JoinMsg");
        int random = new Random().nextInt(AJoinMsg.size());

        if (prefix!= null) {
            player.sendMessage(ColorUtils.colorcode(prefix) + ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
        } else {
            player.sendMessage(ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
        }


        if (UseTitle) messagesEffects.sendTitle(player);
        if (UseActionBar) messagesEffects.sendActionBar(player);

        if (particles != null) messagesEffects.sendParticleEffect(player);
        if (sounds != null) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("JoinSound")), pl.getConfig().getInt("Volume"), pl.getConfig().getInt("Pitch"));
    }


    @SuppressWarnings("DuplicatedCode")
    public static void TestPlayerQuit(String playerName){
        Player player = Bukkit.getPlayerExact(playerName);

        String prefix = pl.getConfig().getString("QuitPrefix");

        List<String> AJoinMsg = pl.getConfig().getStringList("QuitMsg");
        int random = new Random().nextInt(AJoinMsg.size());

        if (prefix!= null) {
            player.sendMessage(ColorUtils.colorcode(prefix) + ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));

        } else {
            player.sendMessage(ColorUtils.colorcode(AJoinMsg.get(random).replace("%player%", player.getName())));
        }

        if (pl.getConfig().getString("QuitSound") != null) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("QuitSound")), pl.getConfig().getInt("Volume"), pl.getConfig().getInt("Pitch"));
    }
}
