package me.jsinco.solutilities;


import me.jsinco.solutilities.blackmarket.MarketCommand;
import me.jsinco.solutilities.bukkitcommands.*;
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands.Vouchers;
import me.jsinco.solutilities.celestial.CelestialFile;
import me.jsinco.solutilities.celestial.aries.CandleApplyGUI;
import me.jsinco.solutilities.celestial.aries.ChooseGUI;
import me.jsinco.solutilities.celestial.aries.ChooseGUIOpen;
import me.jsinco.solutilities.celestial.aries.itemprofler.ItemProfiler;
import me.jsinco.solutilities.celestial.aries.itemprofler.ItemProfilerFile;
import me.jsinco.solutilities.celestial.celeste.Shop;
import me.jsinco.solutilities.celestial.celeste.commands.CelesteCommandManager;
import me.jsinco.solutilities.celestial.luna.*;
import me.jsinco.solutilities.celestial.luna.commands.LunaCommandManager;
import me.jsinco.solutilities.hooks.PermissionCheckPlaceholder;
import me.jsinco.solutilities.misc.joins.JoinsCommand;
import me.jsinco.solutilities.misc.joins.Listeners;
import me.jsinco.solutilities.misc.furniture.Furniture;
import me.jsinco.solutilities.misc.PvGui;
import me.jsinco.solutilities.misc.Referrals;
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.CommandManager;
import me.jsinco.solutilities.misc.InvisibleFrames;
import me.jsinco.solutilities.utility.UtilListeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class SolUtilities extends JavaPlugin {
    private static SolUtilities plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Util.INSTANCE.loadUtils();
        getConfig().options().copyDefaults(); // Move to file manager?
        saveDefaultConfig();
        Saves.setup();
        Saves.save();
        CelestialFile.setup();
        CelestialFile.save();
        ItemProfilerFile.setup();



        getServer().getPluginManager().registerEvents(new UtilListeners(), this);

        // New stuff, slowly refactoring
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new WelcomePoints(this), this);
        getServer().getPluginManager().registerEvents(new InvisibleFrames(), this);
        getServer().getPluginManager().registerEvents(new RanksMenu(), this);
        getServer().getPluginManager().registerEvents(new PvGui(this), this);
        getServer().getPluginManager().registerEvents(new Shop(), this); // Celestial Celeste
        getServer().getPluginManager().registerEvents(new SelectGUI(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new WrapGUI(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new RemoveWrapGUI(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new ArmorHandler(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new Vouchers(), this);
        getServer().getPluginManager().registerEvents(new ItemProfiler(this), this);
        CommandMapper.registerBukkitCommand("ping", new PingCommand());
        CommandMapper.registerBukkitCommand("ls", new BuyShopAlias());
        CommandMapper.registerBukkitCommand("lb", new SellShopAlias());
        CommandMapper.registerBukkitCommand("ranks", new RanksMenu());
        CommandMapper.registerBukkitCommand("joins", new JoinsCommand(this));
        CommandMapper.registerBukkitCommand("welcomes", new WelcomePoints(this));
        CommandMapper.registerBukkitCommand("solutilities", new CommandManager(this));
        CommandMapper.registerBukkitCommand("furniture", new Furniture(this));
        CommandMapper.registerBukkitCommand("luna", new LunaCommandManager(this)); // Celestial Luna
        CommandMapper.registerBukkitCommand("celeste", new CelesteCommandManager(this)); // Celestial Celeste
        CommandMapper.registerBukkitCommand("itemprofiler", new ItemProfiler(this));


        new PlaceHolders().register(); //register PAPI placeholder
        new ItemProfiler(this);
        new MarketCommand(this);
        new Referrals(this);

        new PermissionCheckPlaceholder().register();

        // Aries
        getCommand("ariesmisc").setExecutor(new ChooseGUIOpen());
        getServer().getPluginManager().registerEvents(new ChooseGUI(), this);
        getServer().getPluginManager().registerEvents(new CandleApplyGUI(), this);

    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) { // Better solution to this?
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (inv.getHolder() == null) {
                player.closeInventory();
            }
        }
        new PlaceHolders().unregister();
        new PermissionCheckPlaceholder().unregister();
    }


    public static SolUtilities getPlugin() {
        return plugin;
    }

    public Plugin getSolItems() {
        return getServer().getPluginManager().getPlugin("SolItems");
    }
}

