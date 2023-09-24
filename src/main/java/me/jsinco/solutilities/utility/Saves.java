package me.jsinco.solutilities.utility;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Saves {
    private static File file;
    private static FileConfiguration customFile;

    //Finds or generates YAML used for storing how many points each player has using UUIDs as keys
    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SolUtilities").getDataFolder(), "Saves.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                //ow
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
            //
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
