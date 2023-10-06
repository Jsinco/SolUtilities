package me.jsinco.solutilities.bukkitcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;

public class CommandMapper {
    /**
     * Registers a BukkitCommand to the server's command map using reflection, no need to declare in Plugin.yml
     * @param commandName The name of the command to register.
     * @param bukkitCommand The BukkitCommand to register.
     */
    public static void registerBukkitCommand(String commandName, BukkitCommand bukkitCommand) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());


            commandMap.register(commandName, "solutilities", bukkitCommand);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
