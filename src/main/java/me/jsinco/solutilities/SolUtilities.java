package me.jsinco.solutilities;


import me.jsinco.solutilities.blackmarket.MarketCommand;
import me.jsinco.solutilities.bukkitcommands.*;
import me.jsinco.solutilities.celestial.CelestialFile;
import me.jsinco.solutilities.celestial.aries.CandleApplyGUI;
import me.jsinco.solutilities.celestial.aries.ChooseGUI;
import me.jsinco.solutilities.celestial.aries.ChooseGUIOpen;
import me.jsinco.solutilities.celestial.aries.itemprofler.ItemProfiler;
import me.jsinco.solutilities.celestial.celeste.Shop;
import me.jsinco.solutilities.celestial.celeste.ShopAdmin;
import me.jsinco.solutilities.celestial.celeste.ShopOpen;
import me.jsinco.solutilities.celestial.luna.*;
import me.jsinco.solutilities.hooks.PermissionCheckPlaceholder;
import me.jsinco.solutilities.misc.joins.JoinsCommand;
import me.jsinco.solutilities.misc.joins.Listeners;
import me.jsinco.solutilities.misc.furniture.Furniture;
import me.jsinco.solutilities.misc.PvGui;
import me.jsinco.solutilities.misc.Referrals;
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.CommandManager;
import me.jsinco.solutilities.misc.InvisibleFrames;
import me.jsinco.solutilities.utility.TagParticleVouchers;
import me.jsinco.solutilities.utility.UtilListeners;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class SolUtilities extends JavaPlugin {
    private static Economy econ = null;
    File itemProfilesFile = new java.io.File(getDataFolder(), "ItemProfiles.yml");
    private final Plugin SolItems = Bukkit.getServer().getPluginManager().getPlugin("SolItems");
    private static SolUtilities plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Util.loadPrefix();
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        setupItemProfilesFile();
        Saves.setup();
        Saves.save();
        setupEconomy();



        getServer().getPluginManager().registerEvents(new UtilListeners(), this);
        getServer().getPluginManager().registerEvents(new TagParticleVouchers(), this);

        // New stuff, slowly refactoring
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new WelcomePoints(this), this);
        getServer().getPluginManager().registerEvents(new InvisibleFrames(), this);
        getServer().getPluginManager().registerEvents(new RanksMenu(), this);
        getServer().getPluginManager().registerEvents(new PvGui(this), this);
        CommandMapper.registerBukkitCommand("ping", new PingCommand());
        CommandMapper.registerBukkitCommand("ls", new BuyShopAlias());
        CommandMapper.registerBukkitCommand("lb", new SellShopAlias());
        CommandMapper.registerBukkitCommand("ranks", new RanksMenu());
        CommandMapper.registerBukkitCommand("joins", new JoinsCommand(this));
        CommandMapper.registerBukkitCommand("welcomes", new WelcomePoints(this));
        CommandMapper.registerBukkitCommand("solutilities", new CommandManager(this));
        CommandMapper.registerBukkitCommand("furniture", new Furniture(this));

        new PlaceHolders().register(); //register PAPI placeholder
        new UtilListeners();
        new ItemProfiler(this);
        new MarketCommand(this);
        new Referrals(this);

        new PermissionCheckPlaceholder().register();

        // Celeste
        CelestialFile.setup();
        CelestialFile.save();
        getCommand("celesteadmin").setExecutor(new ShopAdmin());
        getCommand("celesteshop").setExecutor(new ShopOpen());
        getServer().getPluginManager().registerEvents(new Shop(), this);
        Shop.adminInitializeShop();

        // Luna
        getCommand("modelitem").setExecutor(new CustomModel());
        getCommand("lunaadmin").setExecutor(new ModelAdmin());
        getCommand("lunawrapper").setExecutor(new SelectGUIOpen());
        getServer().getPluginManager().registerEvents(new SelectGUI(), this);
        getServer().getPluginManager().registerEvents(new WrapGUI(), this);
        getServer().getPluginManager().registerEvents(new RemoveWrapGUI(), this);
        getServer().getPluginManager().registerEvents(new ArmorHandler(), this);

        // Aries
        getCommand("ariesmisc").setExecutor(new ChooseGUIOpen());
        getServer().getPluginManager().registerEvents(new ChooseGUI(), this);
        getServer().getPluginManager().registerEvents(new CandleApplyGUI(), this);

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }




    public void setupItemProfilesFile() {
        try {
            if (!itemProfilesFile.exists()) {
                itemProfilesFile.createNewFile();
                InputStream inputStream = this.getResource("ItemProfiles.yml");
                OutputStream outputStream = Files.newOutputStream(itemProfilesFile.toPath());
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException ignored) {
        }
    }


    public YamlConfiguration ItemProfilesFile(){
        return YamlConfiguration.loadConfiguration(itemProfilesFile);
    }


    public static Economy getEconomy() {
        return econ;
    }

    public Plugin getSolItems() {
        return SolItems;
    }

    public static SolUtilities getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getLogger().info("SolUtilities has been disabled!");
        new PlaceHolders().unregister();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (inv.getHolder() == null) {
                player.closeInventory();
            }
        }

        new PermissionCheckPlaceholder().unregister();
    }
}

