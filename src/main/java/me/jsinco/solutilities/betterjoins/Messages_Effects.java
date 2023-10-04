package me.jsinco.solutilities.betterjoins;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.utility.Saves;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static me.jsinco.solutilities.celestial.luna.ModelAdmin.pl;

public class Messages_Effects {



    public void sendTitle(Player player) {
        List <String> TitleMsg = pl.getConfig().getStringList("TitleMsg");
        String string1 = ColorUtils.colorcode(TitleMsg.get(0).replace("%player%", player.getName()));
        String string2 = ColorUtils.colorcode(TitleMsg.get(1).replace("%player%", player.getName()));
        player.sendTitle(string1, string2, pl.getConfig().getInt("FadeIn"), pl.getConfig().getInt("Stays"), pl.getConfig().getInt("FadeOut"));
    }

    public void sendParticleEffect(Player player){
        player.getWorld().spawnParticle(Particle.valueOf(pl.getConfig().getString("ParticleType")),
                player.getLocation(), pl.getConfig().getInt("ParticleAmount"), 0.5, 0.5, 0.5, 0.01);
        //joining player cannot see particles spawned by world, latter is for observing players
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
            player.getWorld().spawnParticle(Particle.valueOf(pl.getConfig().getString("ParticleType")),
                    player.getLocation(), pl.getConfig().getInt("ParticleAmount"), 0.5, 0.5, 0.5, 0.01);
        }, 18L);
    }

    public void sendSound(Player player, String sounds) {
        if (sounds.equalsIgnoreCase("player")){
            player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("JoinSound")), 0.5f, 1);
        } else if (sounds.equalsIgnoreCase("all")){
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getScoreboardTags().contains("betterjoins.silent")) return;
                p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("JoinSound")), 0.5f, 1);
            });
        }
    }

    public void sendActionBar(Player player){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorUtils.colorcode(pl.getConfig().getString("JoinBarMsg").replace("%player%", player.getName()))));

    }

    public void sendFirstJoinMsg(Player player, List<String> firstJoinMsg, int incriment) {
        for (String msg : firstJoinMsg) {
            Bukkit.broadcastMessage(ColorUtils.colorcode(msg.replace("%player%", player.getName()).replace("%joins%", String.valueOf(Saves.get().getInt("TotalJoins"))).replace("[","").replace("]","")));
        }
        sendSound(player, pl.getConfig().getString("Sounds"));
        Saves.get().set("TotalJoins", Saves.get().getInt("TotalJoins") + incriment);
        Saves.save();
    }


    //leave sfx
    public void sendLeaveSound() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.getScoreboardTags().contains("betterjoins.silent")) return;
            p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("QuitSound")), 0.5f, 1);
        });
    }
}
