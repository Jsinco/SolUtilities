package me.jsinco.solutilities;

import me.jsinco.solutilities.celestial.celeste.aries.*;
import me.jsinco.solutilities.celestial.celeste.aries.itemprofler.ItemProfiler;
import me.jsinco.solutilities.betterjoins.PlayerJoinLeave;
import me.jsinco.solutilities.blackmarket.MarketCommand;
import me.jsinco.solutilities.celestial.celeste.File;
import me.jsinco.solutilities.celestial.celeste.Shop;
import me.jsinco.solutilities.celestial.celeste.ShopAdmin;
import me.jsinco.solutilities.celestial.celeste.ShopOpen;
import me.jsinco.solutilities.celestial.celeste.luna.ArmorHandler;
import me.jsinco.solutilities.celestial.celeste.luna.CustomModel;
import me.jsinco.solutilities.celestial.celeste.luna.SelectGUI;
import me.jsinco.solutilities.ranks.GUI;
import me.jsinco.solutilities.ranks.RankUpgrades;
import me.jsinco.solutilities.ranks.RanksMenuOpen;
import me.jsinco.solutilities.solace.Furniture;
import me.jsinco.solutilities.solace.PVGUI;
import me.jsinco.solutilities.solace.Referrals;
import me.jsinco.solutilities.solace.SetItem_Redeem;
import me.jsinco.solutilities.utility.*;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class SolUtilities extends JavaPlugin {
    private static PlayerPointsAPI ppAPI;
    private static Economy econ = null;
    java.io.File itemProfilesFile = new java.io.File(getDataFolder(), "ItemProfiles.yml");
    private final Plugin SolItems = Bukkit.getServer().getPluginManager().getPlugin("SolItems");

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        setupItemProfilesFile();
        Saves.setup(); // Sol withdrawal vouchers
        Saves.save();
        ppAPI = PlayerPoints.getInstance().getAPI();
        setupEconomy();



        getServer().getPluginManager().registerEvents(new PlayerJoinLeave(), this);
        getServer().getPluginManager().registerEvents(new UtilListeners(), this);
        getServer().getPluginManager().registerEvents(new TagParticleVouchers(), this);
        getServer().getPluginManager().registerEvents(new GUI(), this);
        getServer().getPluginManager().registerEvents(new InvisibleFrames(), this);
        getServer().getPluginManager().registerEvents(new Welcomes(), this);

        getServer().getPluginManager().registerEvents(new SetItem_Redeem(this, ppAPI), this);
        getCommand("addenchant").setExecutor(new AddEnchant());
        getCommand("solace").setExecutor(new SolaceCMD(this));
        getCommand("solace").setTabCompleter(new TabCompletion());
        getCommand("solcoin").setExecutor(new SolaceCMD(this));
        getCommand("ranks").setExecutor(new RanksMenuOpen());

        getCommand("ls").setExecutor(new SearchShop());
        getCommand("lb").setExecutor(new SearchShop());
        getCommand("ls").setTabCompleter(new TabCompletion());
        getCommand("lb").setTabCompleter(new TabCompletion());
        getCommand("welcomes").setExecutor(new Welcomes());
        getCommand("welcomes").setTabCompleter(new TabCompletion());
        getCommand("healall").setExecutor(new Healall());
        getCommand("solwarp").setExecutor(new Warps());
        getCommand("solwarp").setTabCompleter(new Warps());
        
        new PlaceHolders().register(); //register PAPI placeholder
        new RankUpgrades(this);
        new Furniture(this);
        new UtilListeners();
        new DonationMsgs(this);
        new ItemProfiler(this);
        new MarketCommand(this);
        new PVGUI(this);
        new Referrals(this);


        getCommand("solrg").setExecutor(new SolRegions());

        // Celeste
        File.setup();
        File.save();
        getCommand("celesteadmin").setExecutor(new ShopAdmin());
        getCommand("celesteshop").setExecutor(new ShopOpen());
        getServer().getPluginManager().registerEvents(new Shop(), this);
        Shop.adminInitializeShop();

        // Luna
        getCommand("modelitem").setExecutor(new CustomModel());
        getCommand("lunaadmin").setExecutor(new me.jsinco.solutilities.celestial.celeste.luna.ModelAdmin());
        getCommand("lunawrapper").setExecutor(new me.jsinco.solutilities.celestial.celeste.luna.SelectGUIOpen());
        getServer().getPluginManager().registerEvents(new SelectGUI(), this);
        getServer().getPluginManager().registerEvents(new me.jsinco.solutilities.celestial.celeste.luna.WrapGUI(), this);
        getServer().getPluginManager().registerEvents(new me.jsinco.solutilities.celestial.celeste.luna.RemoveWrapGUI(), this);
        getServer().getPluginManager().registerEvents(new ArmorHandler(), this);

        // Aries
        MiscFile.setup();
        MiscFile.save();
        getCommand("ariesadmin").setExecutor(new AriesAdmin());
        getCommand("ariesmisc").setExecutor(new ChooseGUIOpen());
        getServer().getPluginManager().registerEvents(new MiscShop(ppAPI), this);
        getServer().getPluginManager().registerEvents(new ChooseGUI(), this);
        getServer().getPluginManager().registerEvents(new CandleApplyGUI(), this);
        MiscShop.adminInitializeShop();
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

    public static PlayerPointsAPI getPPAPI() {
        return ppAPI;
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
    }
}

