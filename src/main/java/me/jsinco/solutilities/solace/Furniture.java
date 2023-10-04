package me.jsinco.solutilities.solace;

import me.jsinco.solutilities.Saves;
import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.Util;
import me.jsinco.solutilities.utility.GUIActions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Furniture implements CommandExecutor, TabCompleter, Listener {

    private final SolUtilities plugin;

    public Furniture(SolUtilities plugin) {
        this.plugin = plugin;
        Saves.setup();
        Saves.save();
        FurnitureGUI.initFurnitureGui();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getPluginManager().registerEvents(new FurnitureGUI(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FurnitureAdminGUI(), plugin);

        plugin.getCommand("furniture").setExecutor(this);
        plugin.getCommand("furniture").setTabCompleter(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = commandSender instanceof Player ? (Player) commandSender : null;

        if (player != null) {
            if (strings.length < 1 || !player.hasPermission("solutilities.admin")) {
                FurnitureGUI.openInventory(player, 0);
                return true;
            }
        }

        if (strings[0].equalsIgnoreCase("addinv") && player != null){
            List<ItemStack> furnitures = new ArrayList<>();
            player.getInventory().forEach(furniture -> {
                if (furniture != null && !furniture.getType().isAir()) {
                    furnitures.add(furniture);
                }
            });
            try {
                Saves.get().getList("Furniture").forEach(furniture -> furnitures.add((ItemStack) furniture));
                Saves.get().set("Furniture", furnitures);
            } catch (Exception e) {
                Saves.get().set("Furniture", furnitures);
            }
            Saves.save();
            return true;
        } else if (strings[0].equalsIgnoreCase("reload")) {
            FurnitureGUI.initFurnitureGui();
        } else if (strings[0].equalsIgnoreCase("orb")){
            ItemStack furnitureOrb = GUIActions.createGuiItem(true,new ItemStack(Material.SCUTE), Util.colorcode("&#a8ff92&lFurniture &#E2E2E2Orb"),
                    Util.colorcode("&7Right Click to open!"));
            ItemMeta tagOrbMeta = furnitureOrb.getItemMeta();
            tagOrbMeta.getPersistentDataContainer().set(new NamespacedKey(plugin,"FurnitureOrb"), PersistentDataType.SHORT, (short) 10);
            furnitureOrb.setItemMeta(tagOrbMeta);

            if (strings.length > 1) {
                Player target = Bukkit.getPlayerExact(strings[1]);
                assert target != null;
                try {
                    for (int i = 0; i < Integer.parseInt(strings[2]); i++) {
                        target.getInventory().addItem(furnitureOrb);
                    }
                } catch (Exception e) {
                    target.getInventory().addItem(furnitureOrb);
                }
            } else if (player != null) {
                player.getInventory().addItem(furnitureOrb);
            }
        } else if (strings[0].equalsIgnoreCase("edit") && player != null) {
            FurnitureAdminGUI.initFurnitureAdminGui();
            FurnitureAdminGUI.openInventory(player, 0);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("furniture") && commandSender.hasPermission("solutilities.admin")) {
            if (strings.length == 1) {
                return List.of("addinv", "reload", "orb", "edit");
            } else if (strings.length == 3) {
                return List.of("<amount?>");
            }

        }
        return null;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        if (event.getItem() == null || !event.getItem().hasItemMeta()) return;

        if (event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin,"FurnitureOrb"), PersistentDataType.SHORT)) {
            List<ItemStack> furnitures = new ArrayList<>();
            Saves.get().getList("Furniture").forEach(furniture -> furnitures.add((ItemStack) furniture));
            int random = new Random().nextInt(furnitures.size());
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            event.getPlayer().getInventory().addItem(furnitures.get(random));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GLOW_SQUID_AMBIENT, 1, 1);
        }

    }

}