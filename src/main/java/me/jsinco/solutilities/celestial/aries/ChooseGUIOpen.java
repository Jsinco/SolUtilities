package me.jsinco.solutilities.celestial.aries;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChooseGUIOpen implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            Player target = Bukkit.getPlayerExact(strings[0]);
            ChooseGUI.init();
            ChooseGUI.openInventory(target);
        } catch (Exception e) {
            commandSender.sendMessage("Player not found");
        }
        return true;
    }
}
