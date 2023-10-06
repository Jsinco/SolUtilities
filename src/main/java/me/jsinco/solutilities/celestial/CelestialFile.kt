package me.jsinco.solutilities.celestial;

import me.jsinco.solutilities.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CelestialFile {

    private static final FileManager filemanager = new FileManager("CelesteItemsAndPrices.yml");
    private static final YamlConfiguration celesteFile = filemanager.getFileYaml();

    public static void setup(){
        filemanager.setFolder("data");
        filemanager.generateFile();
    }

    public static YamlConfiguration get(){
        return celesteFile;
    }

    public static void save(){
        filemanager.saveFileYaml();
    }

    public static void reload(){
        filemanager.reloadFileYaml();
    }
}
