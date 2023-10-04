package me.jsinco.solutilities.celestial.luna;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.SolUtilities;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ModelAdmin implements CommandExecutor {

    public static SolUtilities pl = SolUtilities.getPlugin(SolUtilities.class);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if (strings.length == 0) return false;
        if (strings[0].equalsIgnoreCase("see")) {
            // DEBUG
            player.sendMessage(meta.getAttributeModifiers().toString());
            return true;
        }

        if (strings[0].equalsIgnoreCase("wraps")){
            WrapGUI.openInventory(player);
            return true;
        }

        if (strings[0].equalsIgnoreCase("wrapinv")) {
            invWrap(player, strings[1], strings[2], strings[3]);
            return true;
        }
        player.getInventory().setItemInMainHand(wrapCreation(strings[0], meta, ColorUtils.colorcode(String.join(" ", strings).replace(strings[0], "").strip()), player.getInventory().getItemInMainHand()));

        return true;
    }


    private void invWrap(Player player, String name, String gradient1, String gradient2) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                if (item.getType().toString().toLowerCase().contains("leather") || item.getType().toString().toLowerCase().contains("netherite") ||
                        item.getType().toString().toLowerCase().contains("rod") || item.getType().toString().toLowerCase().contains("bow") ||
                        item.getType().toString().toLowerCase().contains("shield") || item.getType().toString().toLowerCase().contains("paper")) {
                    String type = item.getType().toString().substring(item.getType().toString().lastIndexOf("_") + 1).toLowerCase();
                    String typeName;
                    if (item.getType().equals(Material.PAPER)) {
                        typeName = "Helmet";
                    } else {
                        typeName = type.substring(0, 1).toUpperCase() + type.substring(1);
                    }
                    String wrapName = IridiumColorAPI.process("<GRADIENT:"+gradient1+">"+"&l"+name.replace("_"," ")
                            +" "+typeName+" Wrap"+"</GRADIENT:"+gradient2+">");
                    player.getInventory().setItem(i, wrapCreation(type, item.getItemMeta(), wrapName, item));
                }
            }
        }

    }

    private ItemStack wrapCreation(String type, ItemMeta meta, String wrapName, ItemStack wrap) {
        ItemMeta wrapMeta = wrap.getItemMeta();
        wrapMeta.addEnchant(Enchantment.LUCK,1, true);
        wrapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer wrapData = wrapMeta.getPersistentDataContainer();

        wrapData.set(new NamespacedKey(pl, "type"), PersistentDataType.STRING, type);
        wrapData.set(new NamespacedKey(pl, "model"), PersistentDataType.INTEGER, meta.getCustomModelData());

        if (type.equalsIgnoreCase("helmet") || type.equalsIgnoreCase("chestplate") || type.equalsIgnoreCase("leggings") || type.equalsIgnoreCase("boots")) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            wrapData.set(new NamespacedKey(pl, "red"), PersistentDataType.INTEGER, leatherArmorMeta.getColor().getRed());
            wrapData.set(new NamespacedKey(pl, "green"), PersistentDataType.INTEGER, leatherArmorMeta.getColor().getGreen());
            wrapData.set(new NamespacedKey(pl, "blue"), PersistentDataType.INTEGER, leatherArmorMeta.getColor().getBlue());
        } else if (type.equalsIgnoreCase("paper")) {
            wrapData.set(new NamespacedKey(pl, "PaperHelmet"), PersistentDataType.SHORT, (short) 0);
        }

        wrapMeta.setDisplayName(wrapName);
        wrap.setItemMeta(wrapMeta);
        return wrap;
    }
}

/*
if (!strings[0].isBlank()) {
            type = strings[0];
            name = strings[1];
        } else {

            type = item.getType().toString().substring(item.getType().toString().lastIndexOf("_") + 1);
        }

        ItemStack wrap = new ItemStack(Material.PAPER);
        ItemMeta wrapMeta = wrap.getItemMeta();
        wrapMeta.addEnchant(Enchantment.LUCK,1, true);
        wrapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        wrapMeta.setCustomModelData(86);
        PersistentDataContainer wrapData = wrapMeta.getPersistentDataContainer();

        wrapData.set(new NamespacedKey(pl, "type"), PersistentDataType.STRING, type);
        wrapData.set(new NamespacedKey(pl, "model"), PersistentDataType.INTEGER, meta.getCustomModelData());

        if (strings[0].equalsIgnoreCase("helmet") || strings[0].equalsIgnoreCase("chestplate") || strings[0].equalsIgnoreCase("leggings") || strings[0].equalsIgnoreCase("boots")) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            wrapData.set(new NamespacedKey(pl, "red"), PersistentDataType.INTEGER, leatherArmorMeta.getColor().getRed());
            wrapData.set(new NamespacedKey(pl, "green"), PersistentDataType.INTEGER, leatherArmorMeta.getColor().getGreen());
            wrapData.set(new NamespacedKey(pl, "blue"), PersistentDataType.INTEGER, leatherArmorMeta.getColor().getBlue());
        }

        wrapMeta.setDisplayName(meta.getDisplayName() + " WRAP " + type);
        wrap.setItemMeta(wrapMeta);
        player.getInventory().addItem(wrap);
 */
