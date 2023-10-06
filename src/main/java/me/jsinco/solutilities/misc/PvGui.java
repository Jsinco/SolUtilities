package me.jsinco.solutilities.misc;

import me.jsinco.solutilities.Saves;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PvGui implements Listener {

    private static SolUtilities plugin;

    public PvGui(SolUtilities plugin) {
        PvGui.plugin = plugin;
        initPaneColorGUI();
    }


    private static final Map<String, List<Inventory>> multiPagePVs = new HashMap<>();
    private static final List<Inventory> singlePagePVs = new ArrayList<>();
    private static final Inventory paneColorGUI = Bukkit.createInventory(null, 18, "Change Vault Color");
    private static final Map<String, Integer> mappedPaneColor = new HashMap<>();

    private void initPaneColorGUI() {
        for (Material material : Material.values()) {
            if (material.toString().contains("PANE")) {
                ItemStack pane = GUIActions.createGuiItem(false, new ItemStack(material), "&f&l" + material.toString().replace("_STAINED_GLASS_PANE", "").replace("_", " ").strip());
                paneColorGUI.addItem(pane);
            }
        }
        paneColorGUI.addItem(GUIActions.createGuiItem(false, new ItemStack(Material.BARRIER), "&c&lCancel"));
    }



    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        switch (event.getMessage()) {
            case "/pv", "/playervaults", "/chest", "/vc", "/vault" -> {
                event.setCancelled(true);
                createVaultsGUI(event.getPlayer());
            }
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) { // this is a mess
        if (!event.getPlayer().hasMetadata("pv-viewing")) {
            multiPagePVs.remove(event.getPlayer().getName());
        } else if (singlePagePVs.contains(event.getInventory())) {
            singlePagePVs.remove(event.getInventory());
        } else {
            event.getPlayer().removeMetadata("pv-viewing", plugin);
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!multiPagePVs.containsKey(event.getWhoClicked().getName()) && !singlePagePVs.contains(event.getInventory()) && !event.getInventory().equals(paneColorGUI)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        if (event.isRightClick() && clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "vault"), PersistentDataType.INTEGER)) {
            int vaultNum = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "vault"), PersistentDataType.INTEGER);

            mappedPaneColor.put(player.getName(), vaultNum);
            player.setMetadata("coloring-vault", new FixedMetadataValue(plugin, true));
            player.openInventory(paneColorGUI);
            return;
        }


        if (multiPagePVs.containsKey(player.getName())) {
            List<Inventory> pages = multiPagePVs.get(player.getName());

            if (!clickedItem.hasItemMeta()) return;
            switch (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName())) {
                case "Next Page" -> {
                    int currentPage = pages.indexOf(event.getInventory());
                    if (currentPage == pages.size() - 1) return;
                    player.setMetadata("pv-viewing", new FixedMetadataValue(plugin, true));
                    updateMultiPage(pages, pages.get(currentPage + 1));
                    player.openInventory(pages.get(currentPage + 1));
                }
                case "Previous Page" -> {
                    int currentPage = pages.indexOf(event.getInventory());
                    if (currentPage == 0) return;
                    player.setMetadata("pv-viewing", new FixedMetadataValue(plugin, true));
                    updateMultiPage(pages, pages.get(currentPage - 1));
                    player.openInventory(pages.get(currentPage - 1));
                }
            }
        } else if (event.getInventory().equals(paneColorGUI)) {
            if (clickedItem.getType().equals(Material.BARRIER)) {
                mappedPaneColor.remove(player.getName());
                player.closeInventory();
                createVaultsGUI(player);
                return;
            }
            int vaultNum = mappedPaneColor.get(player.getName());
            Saves.get().set("PVGUI.Vault"+vaultNum+"."+player.getUniqueId(), clickedItem.getType().toString());
            Saves.save();
            mappedPaneColor.remove(player.getName());
            player.closeInventory();
            createVaultsGUI(player);
            player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "Color changed!"));
            
        }


        if (!clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "vault"), PersistentDataType.INTEGER)) return;
        int vaultNum = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "vault"), PersistentDataType.INTEGER);
        Bukkit.dispatchCommand(player, "pv " + vaultNum);
    }


    public static void createVaultsGUI(Player player) {
        final int maxVaults = getMaxVaults(player);

        if (maxVaults > 54) {
            int putItem = 0;
            List<Inventory> pages = new ArrayList<>();
            for (int i = 0; i < maxVaults; i++) {
                if (pages.isEmpty() || pages.get(pages.size() - 1).getItem(44).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    pages.add(createMultiPageGUI());
                    putItem = 0;
                } else {
                    Material pane = Material.WHITE_STAINED_GLASS_PANE;
                    if (Saves.get().getString("PVGUI.Vault"+i+"."+player.getUniqueId()) != null) {
                        pane = Material.valueOf(Saves.get().getString("PVGUI.Vault"+i+"."+player.getUniqueId()));
                    }

                    ItemStack openVault = GUIActions.createGuiItem(true, new ItemStack(pane), Util.colorcode(plugin.getConfig().getString("PVGUI.OpenVault.Title")).replace("$vaultnum", String.valueOf(i)));
                    ItemMeta openVaultMeta = openVault.getItemMeta();
                    openVaultMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "vault"), PersistentDataType.INTEGER, i);

                    final int finalI = i;
                    List<String> lore = new ArrayList<>();
                    plugin.getConfig().getStringList("PVGUI.OpenVault.Lore").forEach(line -> lore.add(Util.colorcode(line.replace("$vaultnum", String.valueOf(finalI)))));
                    openVaultMeta.setLore(lore);

                    openVault.setItemMeta(openVaultMeta);

                    pages.get(pages.size() - 1).setItem(putItem, openVault);
                    putItem++;
                }
            }

            multiPagePVs.put(player.getName(), pages);
            updateMultiPage(pages, pages.get(0));
            player.openInventory(pages.get(0));
        } else {
            Inventory gui = createRegularGUI();
            for (int i = 0; i < maxVaults; i++) {
                Material pane = Material.WHITE_STAINED_GLASS_PANE;
                if (Saves.get().getString("PVGUI.Vault"+(i + 1)+"."+player.getUniqueId()) != null) {
                    pane = Material.valueOf(Saves.get().getString("PVGUI.Vault"+(i + 1)+"."+player.getUniqueId()));
                }

                int vaultNum = i + 1;
                ItemStack openVault = GUIActions.createGuiItem(true, new ItemStack(pane), Util.colorcode(plugin.getConfig().getString("PVGUI.OpenVault.Title")).replace("$vaultnum", String.valueOf(vaultNum)));
                ItemMeta openVaultMeta = openVault.getItemMeta();
                openVaultMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "vault"), PersistentDataType.INTEGER, vaultNum);
                List<String> lore = new ArrayList<>();
                plugin.getConfig().getStringList("PVGUI.OpenVault.Lore").forEach(line -> lore.add(Util.colorcode(line.replace("$vaultnum", String.valueOf(vaultNum)))));
                openVaultMeta.setLore(lore);
                openVault.setItemMeta(openVaultMeta);
                gui.setItem(i, openVault);
            }
            player.openInventory(gui);
            singlePagePVs.add(gui);
        }
    }



    private static void updateMultiPage(List<Inventory> pages, Inventory page) {
        if (pages.indexOf(page) == 0) {
            page.setItem(48, GUIActions.createGuiItem(false, new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "&0"));
        } else if (pages.indexOf(page) == pages.size() - 1) {
            page.setItem(50, GUIActions.createGuiItem(false, new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "&0"));
        }
    }

    private static int getMaxVaults(Player player) {
        int maxVaults = 0;
        for (int i = 0; i < plugin.getConfig().getInt("PVGUI.MaxVaults"); i++) {
            if (player.hasPermission("playervaults.amount." + i)) {
                maxVaults = i;
            }
        }
        return maxVaults;
    }

    private static Inventory createMultiPageGUI() {
        Inventory inv = createRegularGUI();
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, GUIActions.createGuiItem(false, new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "&0"));
        }

        inv.setItem(48, GUIActions.createNBTItem(false, "previouspage", 10041, Material.PAPER, "&#f76a3bP&#f7743br&#f77f3be&#f7893bv&#f7933bi&#f79d3bo&#f8a83au&#f8b23as &#f8bc3aP&#f8c63aa&#f8d13ag&#f8db3ae"));
        inv.setItem(50, GUIActions.createNBTItem(false, "nextpage", 10043, Material.PAPER, "&#f76a3bN&#f77a3be&#f78a3bx&#f79a3bt &#f8ab3aP&#f8bb3aa&#f8cb3ag&#f8db3ae"));

        return inv;
    }

    private static Inventory createRegularGUI() {
        Inventory inv = Bukkit.createInventory(null,54, Util.colorcode(plugin.getConfig().getString("PVGUI.Title")));

        ItemStack lockedVault = GUIActions.createGuiItem(false, new ItemStack(Material.GRAY_STAINED_GLASS_PANE), Util.colorcode(plugin.getConfig().getString("PVGUI.LockedVault.Title")));
        ItemMeta meta = lockedVault.getItemMeta();
        meta.setLore(Util.colorArrayList(plugin.getConfig().getStringList("PVGUI.LockedVault.Lore")));
        lockedVault.setItemMeta(meta);
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, lockedVault);
        }

        return inv;
    }

}
