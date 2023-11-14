package me.jsinco.solutilities;


import me.jsinco.oneannouncer.api.DiscordCommandManager;
import me.jsinco.solutilities.blackmarket.BlackMarketNotifyCommand;
import me.jsinco.solutilities.blackmarket.MarketCommand;
import me.jsinco.solutilities.blackmarket.MarketGUI;
import me.jsinco.solutilities.blackmarket.MarketItemPreview;
import me.jsinco.solutilities.bukkitcommands.*;
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.CommandManager;
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands.Vouchers;
import me.jsinco.solutilities.celestial.CelestialFile;
import me.jsinco.solutilities.celestial.aries.AriesMainGUI;
import me.jsinco.solutilities.celestial.aries.CandleApplyGUI;
import me.jsinco.solutilities.celestial.aries.commands.OpenAriesGUI;
import me.jsinco.solutilities.celestial.aries.itemprofler.ItemProfiler;
import me.jsinco.solutilities.celestial.aries.itemprofler.ItemProfilerFile;
import me.jsinco.solutilities.celestial.celeste.Shop;
import me.jsinco.solutilities.celestial.celeste.commands.CelesteCommandManager;
import me.jsinco.solutilities.celestial.luna.RemoveWrapGUI;
import me.jsinco.solutilities.celestial.luna.SelectGUI;
import me.jsinco.solutilities.celestial.luna.WrapGUI;
import me.jsinco.solutilities.celestial.luna.WrapsEventHandler;
import me.jsinco.solutilities.celestial.luna.commands.LunaCommandManager;
import me.jsinco.solutilities.features.*;
import me.jsinco.solutilities.features.furniture.Furniture;
import me.jsinco.solutilities.features.joins.JoinsCommand;
import me.jsinco.solutilities.features.joins.Listeners;
import me.jsinco.solutilities.hooks.PAPIManager;
import me.jsinco.solutilities.hooks.papi.PermissionCheckPlaceholder;
import me.jsinco.solutilities.hooks.papi.SolutilitiesPlaceholder;
import me.jsinco.solutilities.utility.GeneralEvents;
import me.jsinco.solutilities.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolUtilities extends JavaPlugin {

    private static SolUtilities plugin;
    private final PAPIManager papiManager = new PAPIManager();

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

        // New stuff
        getServer().getPluginManager().registerEvents(new LegacySolcoins(this), this); // TODO: Legacy
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new WelcomePoints(this), this);
        getServer().getPluginManager().registerEvents(new InvisibleFrames(), this);
        getServer().getPluginManager().registerEvents(new RanksMenu(), this);
        getServer().getPluginManager().registerEvents(new PvGui(this), this);
        getServer().getPluginManager().registerEvents(new Shop(), this); // Celestial Celeste
        getServer().getPluginManager().registerEvents(new SelectGUI(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new WrapGUI(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new RemoveWrapGUI(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new WrapsEventHandler(), this); // Celestial Luna
        getServer().getPluginManager().registerEvents(new Vouchers(), this);
        getServer().getPluginManager().registerEvents(new ItemProfiler(this), this); // Celestial Aries
        getServer().getPluginManager().registerEvents(new AriesMainGUI(), this); // Celestial Aries
        getServer().getPluginManager().registerEvents(new CandleApplyGUI(), this); // Celestial Aries
        getServer().getPluginManager().registerEvents(new MarketItemPreview(), this);
        getServer().getPluginManager().registerEvents(new MarketGUI(), this);
        getServer().getPluginManager().registerEvents(new GeneralEvents(this), this);
        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new ItemLocker(this), this);

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
        CommandMapper.registerBukkitCommand("aries", new OpenAriesGUI()); // Celestial Aries
        CommandMapper.registerBukkitCommand("blackmarket", new MarketCommand(this));
        CommandMapper.registerBukkitCommand("commandspy", new CommandSpy());
        CommandMapper.registerBukkitCommand("itemlock", new ItemLocker(this));

        DiscordCommandManager.registerGlobalCommand(new BlackMarketNotifyCommand());

        papiManager.addPlaceholder(new PermissionCheckPlaceholder());
        papiManager.addPlaceholder(new SolutilitiesPlaceholder());
        papiManager.registerPlaceholders();


        // TODO: edit referrals to use the new command manager
        new Referrals(this);
    }

    @Override
    public void onDisable() {
        papiManager.unregisterPlaceholders();
        CommandMapper.unRegisterBukkitCommands();
        for (Player player : Bukkit.getOnlinePlayers()) { // Better solution to this?
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (inv.getHolder() == null) {
                player.closeInventory();
            }
        }
    }


    public static SolUtilities getPlugin() {
        return plugin;
    }

    public Plugin getSolItems() {
        return getServer().getPluginManager().getPlugin("SolItems");
    }
}

