package me.jsinco.solutilities.solace;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.utility.Saves;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// FIXME: bad code
public class Referrals implements CommandExecutor, Listener {

    private final SolUtilities plugin;

    public Referrals(SolUtilities plugin) {
        this.plugin = plugin;
        plugin.getCommand("refer").setExecutor(this);
        plugin.getCommand("rfintl").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (command.getName().equalsIgnoreCase("rfintl")) {
            if (strings[0].equalsIgnoreCase("confref")) {
                Player referrer = Bukkit.getPlayerExact(strings[1]);
                if (referrer == null) return false;
                player.sendMessage(tryReferralRewarding(referrer, player, true));
            }
        } else {
            Player referral = Bukkit.getPlayerExact(strings[0]);
            if (referral == null) return false;
            player.sendMessage(tryReferralRewarding(player, referral, false));
        }


        return true;
    }

    @SuppressWarnings("deprecation")
    public String tryReferralRewarding(Player player, Player referral, boolean confirmed) {
        long playtime = Long.parseLong(PlaceholderAPI.setPlaceholders(referral, "%jetsantiafkpro_timeplayed_minutes_unformatted%"));

        if (playtime > 1440) return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Your referral cannot have more than 24 hours of playtime!");
        else if (playtime < 120) return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Your referral must have at least 2 hours of playtime!");
        else if (referral.getAddress().getHostName().equals(player.getAddress().getHostName())) return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You cannot refer players from the same IP address!");

        if (Saves.get().get("Referrals." + player.getUniqueId()) != null || Saves.get().get("Referrals." + referral.getUniqueId()) != null) {
            if (Saves.get().getLong("Referrals." + player.getUniqueId()) > System.currentTimeMillis() || Saves.get().getLong("Referrals." + referral.getUniqueId()) > System.currentTimeMillis()) {
                return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You or your referral have already claimed a referral today! You can refer players every 16 hours");
            } else {
                Saves.get().set("Referrals." + player.getUniqueId(), null);
                Saves.get().set("Referrals." + referral.getUniqueId(), null);
                Saves.save();
            }
        }

        if (!confirmed) {
            TextComponent confirm = new TextComponent(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You got a referral from " + player.getName() + "! Click &#a8ff92&lhere&#E2E2E2 to accept your referral!"));
            confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to confirm '" + player.getName() + "' as your referrer!")));
            confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rfintl confref " + player.getName()));
            referral.sendMessage(confirm);
            return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You have sent a referral to " + referral.getName() + "!");
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward(true).replace("$player", player.getName()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward(false).replace("$player", referral.getName()));

        Saves.get().set("Referrals." + player.getUniqueId(), System.currentTimeMillis() + 57600000);
        Saves.get().set("Referrals." + referral.getUniqueId(), System.currentTimeMillis() + 57600000);
        Saves.save();
        return ColorUtils.colorcode(plugin.getConfig().getString("prefix") +  "You have successfully claimed your referral reward!");
    }

    private String reward(boolean isReferrer) {
        Map<String, Integer> keysMap = new HashMap<>();
        List<String> keys;
        if (isReferrer) {
            keys = List.copyOf(plugin.getConfig().getConfigurationSection("Referrals.RewardCommands.Referrer").getKeys(false));
        } else {
            keys = List.copyOf(plugin.getConfig().getConfigurationSection("Referrals.RewardCommands.Referred").getKeys(false));
        }
        for (String key : keys) {
            keysMap.put(key, Integer.parseInt(key.replace("chance_", "").strip()));
        }

        int random = new Random().nextInt(keys.size());

        String selectedCommand = null;
        for (int i = 0; i < 1; i++) {
            if (new Random().nextInt(100) <= keysMap.get(keys.get(random))) {
                if (isReferrer) {
                    selectedCommand = plugin.getConfig().getString("Referrals.RewardCommands.Referrer." + keys.get(random));
                } else {
                    selectedCommand = plugin.getConfig().getString("Referrals.RewardCommands.Referred." + keys.get(random));
                }
            } else {
                random = new Random().nextInt(keys.size());
                i--;
            }
        }

        return selectedCommand;
    }
}
