package me.jsinco.solutilities.ranks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

import static me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin.pl;

public class RanksQueue {
    private static java.io.File file;
    private static FileConfiguration customFile;

    public static void setup(){
        file = new java.io.File(pl.getDataFolder(), "RankUpgradesQueue.yml");

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
            Bukkit.getLogger().warning("Couldn't save file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
