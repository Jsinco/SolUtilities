package me.jsinco.solutilities;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.jsinco.oneannouncer.discord.JDAMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DonationMsgs implements CommandExecutor {

    private static SolUtilities plugin;

    public DonationMsgs(SolUtilities plugin) {
        DonationMsgs.plugin = plugin;
        plugin.getCommand("donationmsg").setExecutor(this);
    }


    // woo it's for donations


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        OfflinePlayer offlinePlayer;
        String packageType;
        try {
            offlinePlayer = Bukkit.getOfflinePlayer(strings[0]);
            packageType = Arrays.toString(strings).replace(strings[0],"").replace("[","").replace("]","").replace(",","").trim();
        } catch (Exception e) {
            commandSender.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "&cInvalid arguments!"));
            return false;
        }


        // process any kind of color
        String message = ColorUtils.colorcode(IridiumColorAPI.process(plugin.getConfig().getString("Announcer.donation-message")
                        .replace("%player%", offlinePlayer.getName()).replace("%package%", packageType)));

        Sound sound = Sound.valueOf(plugin.getConfig().getString("Announcer.donation-sound"));

        Bukkit.broadcastMessage(message);
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 0.5f, 0.5f));
        JDAMethods.sendMessageDiscordUser("425407651480862721", ChatColor.stripColor(message));
        return true;
    }
}
