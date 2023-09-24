package me.jsinco.solutilities.celestial.celeste.luna;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectGUIOpen implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            Player target = Bukkit.getPlayerExact(strings[0]);
            SelectGUI.init();
            SelectGUI.openInventory(target);
        } catch (Exception e) {
            commandSender.sendMessage("Player not found");
        }
        return true;
    }
}
