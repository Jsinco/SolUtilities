package me.jsinco.solutilities.celestial.aries.itemprofler;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.utility.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



// Info box slot - 13
// Search button slot - 23
// Empty slot for custom item - 22
//inv.setItem(13, GUIActions.createNBTItem(true,"itemprofilerinfo", 10070, Material.PAPER, "&#f7903b&lC&#f7953b&lu&#f7993b&ls&#f79e3b&lt&#f7a33b&lo&#f7a73b&lm &#f7ac3b&lI&#f7b13b&lt&#f8b63b&le&#f8ba3a&lm &#f8bf3a&li&#f8c43a&ln&#f8c83a&lf&#f8cd3a&lo&#f8d23a&l.&#f8d63a&l.&#f8db3a&l.",
//                "&7Any info on your custom item will appear here."));
public class ItemProfiler extends BukkitCommand implements Listener {

    private static SolUtilities plugin;

    public ItemProfiler(SolUtilities plugin) {
        super("itemprofiler", "Opens the item profiler", "/itemprofiler", List.of("profiler"));
        ItemProfiler.plugin = plugin;
    }


    private static List<Inventory> openItemProfilers = new ArrayList<>();

    public static void openItemProfiler(Player player) {
        Inventory inv = player.getServer().createInventory(null, 45, Util.colorcode("&#ffb89c&lI&#ffb8a4&lt&#ffb8ad&le&#ffb8b5&lm &#ffb8bd&lP&#ffb8c6&lr&#ffb9ce&lo&#ffb9d7&lf&#ffb9df&li&#ffb9e7&ll&#ffb9f0&le&#ffb9f8&lr"));

        inv.setItem(23, GUIActions.createNBTItem(true, "itemprofilersearch", 10071, Material.PAPER, "&#ffb89c&lV&#ffb8a1&li&#ffb8a7&le&#ffb8ac&lw &#ffb8b2&lC&#ffb8b7&lu&#ffb8bc&ls&#ffb8c2&lt&#ffb8c7&lo&#ffb9cd&lm &#ffb9d2&lI&#ffb9d8&lt&#ffb9dd&le&#ffb9e2&lm &#ffb9e8&lI&#ffb9ed&ln&#ffb9f3&lf&#ffb9f8&lo",
                "&7Click to view any info on this custom item!"));

        inv.setItem(13, GUIActions.createNBTItem(true,"itemprofilerinfo", 10070, Material.PAPER, "&#ffb89c&lC&#ffb8a2&lu&#ffb8a8&ls&#ffb8ad&lt&#ffb8b3&lo&#ffb8b9&lm &#ffb8bf&lI&#ffb8c4&lt&#ffb9ca&le&#ffb9d0&lm &#ffb9d6&lI&#ffb9db&ln&#ffb9e1&lf&#ffb9e7&lo&#ffb9ed&l.&#ffb9f2&l.&#ffb9f8&l.",
                "&7Any info on your custom item will appear here."));

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null && i != 22) {
                inv.setItem(i, GUIActions.createNBTItem(false, "itemprofilerborder", 0, Material.BLACK_STAINED_GLASS_PANE, "&0"));
            }
        }

        openItemProfilers.add(inv);
        player.sendMessage(Util.colorcode(plugin.getConfig().getString("prefix") + "Opening Item Profiler!"));
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!openItemProfilers.contains(event.getInventory())) return;
        Inventory inv = event.getInventory();

        if (inv.getItem(22) != null && inv.getItem(22).getType() != Material.AIR) {
            event.getPlayer().getInventory().addItem(inv.getItem(22));
        }
        openItemProfilers.remove(inv);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!openItemProfilers.contains(event.getInventory())) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;

        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "itemprofilersearch"), PersistentDataType.SHORT)) {
            event.setCancelled(true);

            if (event.getInventory().getItem(22) == null)  return;
            ItemStack customItem = event.getInventory().getItem(22);
            String profile = Util.colorcode(ItemProfilerMethods.setProfilePlaceholders(plugin, player, ItemProfilerMethods.profileItem(customItem)));

            // Experimental
            ItemStack infoBox = event.getInventory().getItem(13);
            ItemMeta infoBoxMeta = infoBox.getItemMeta();

            List<String> infoBoxLore = new ArrayList<>(List.of(""));

            List<String> string = ItemProfilerMethods.divideString(8, profile);


            if (string != null) {

                int size = Math.min(string.size(), 7);

                for (int i = 0; i < size; i++) { // 7 lines of lore max
                    infoBoxLore.add(Util.colorcode("&#E2E2E2" + string.get(i)));
                }
            } else {
                infoBoxLore.add(Util.colorcode("&#E2E2E2" + profile));
            }

            infoBoxLore.addAll(List.of("", Util.colorcode("&#E2E2E2&l... Click for full info")));

            infoBoxMeta.setLore(infoBoxLore);

            infoBoxMeta.addItemFlags(ItemFlag.HIDE_DYE);
            infoBox.setItemMeta(infoBoxMeta);
        } else if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "itemprofilerinfo"), PersistentDataType.SHORT)) {
            event.setCancelled(true);

            if (event.getInventory().getItem(22) == null || !item.getItemMeta().hasItemFlag(ItemFlag.HIDE_DYE))  return;
            String rgb1 = ItemProfilerFile.get().getString("IridiumColors.rgb1");
            String rgb2 = ItemProfilerFile.get().getString("IridiumColors.rgb2");
            ItemStack customItem = event.getInventory().getItem(22);

            String profile = Util.colorcode(ItemProfilerMethods.setProfilePlaceholders(plugin, player, ItemProfilerMethods.profileItem(customItem)));
            player.sendMessage(IridiumColorAPI.process("<GRADIENT:"+rgb1+">" + ItemProfilerFile.get().getString("IridiumColors.border1") + "</GRADIENT:"+rgb2+">"));
            player.sendMessage(profile);
            player.sendMessage(IridiumColorAPI.process("<GRADIENT:"+rgb1+">" + ItemProfilerFile.get().getString("IridiumColors.border2") + "</GRADIENT:"+rgb2+">"));

            player.closeInventory();
        } else if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "itemprofilerborder"), PersistentDataType.SHORT)) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return false;
        openItemProfiler(p);

        return true;
    }
}
