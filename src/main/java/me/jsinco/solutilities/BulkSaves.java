package me.jsinco.solutilities;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class BulkSaves {
    private static java.io.File file;
    private static FileConfiguration customFile;

    public static void setup(){
        file = new java.io.File(Bukkit.getServer().getPluginManager().getPlugin("SolUtilities").getDataFolder(), "BulkSaves.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            Bukkit.getLogger().warning("Couldn't save file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}