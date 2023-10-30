package me.jsinco.solutilities.celestial.luna;

import me.jsinco.solutilities.SolUtilities;
import me.jsinco.solutilities.utility.Util;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

import static org.bukkit.attribute.Attribute.*;

public class Wrapping {
    
    private static final SolUtilities plugin = SolUtilities.getPlugin();

    public static boolean isArmor(final ItemStack itemStack) {
        if (itemStack == null)
            return false;
        final String typeNameString = itemStack.getType().name();
        return typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS");
    }

    private static String getArmorType (final ItemStack itemStack) {
        final String typeNameString = itemStack.getType().name();
        if (typeNameString.endsWith("_HELMET")) {
            return "_HELMET";
        } else if (typeNameString.endsWith("_CHESTPLATE")) {
            return "_CHESTPLATE";
        } else if (typeNameString.endsWith("_LEGGINGS")) {
            return "_LEGGINGS";
        } else if (typeNameString.endsWith("_BOOTS")) {
            return "_BOOTS";
        }
        return null;
    }

    public static ItemStack convertArmorToLeather(ItemStack armor) {
        String type = armor.getType().toString().toLowerCase();

        if (type.contains("helmet")) {
            addNetheriteAttributes(armor, Material.LEATHER_HELMET, 3, EquipmentSlot.HEAD);
        } else if (type.contains("chestplate")) {
            addNetheriteAttributes(armor, Material.LEATHER_CHESTPLATE, 8, EquipmentSlot.CHEST);
        } else if (type.contains("leggings")) {
            addNetheriteAttributes(armor, Material.LEATHER_LEGGINGS, 6, EquipmentSlot.LEGS);
        } else if (type.contains("boots")) {
            addNetheriteAttributes(armor, Material.LEATHER_BOOTS, 3, EquipmentSlot.FEET);
        }
        return armor;
    }

    private static void addNetheriteAttributes(ItemStack armor, Material armorPiece, double genericArmor, EquipmentSlot equipmentSlot) {
        ItemMeta armorMeta = armor.getItemMeta();

        if (armorMeta.hasAttributeModifiers()) {
            armorMeta.removeAttributeModifier(GENERIC_ARMOR);
            armorMeta.removeAttributeModifier(GENERIC_ARMOR_TOUGHNESS);
            armorMeta.removeAttributeModifier(GENERIC_KNOCKBACK_RESISTANCE);
        }

        armorMeta.addAttributeModifier(GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", genericArmor, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot));
        armorMeta.addAttributeModifier(GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot));
        armorMeta.addAttributeModifier(GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", 0.1, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot));

        if (armorPiece.equals(Material.PAPER)) {
            armorMeta.getPersistentDataContainer().set(new NamespacedKey(plugin,"LunaCosmeticHelmet"), PersistentDataType.INTEGER, 407);
        } else {
            armorMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "LUNA_NETHERITE"), PersistentDataType.SHORT, (short) 0);
        }
        armor.setType(armorPiece);
        armor.setItemMeta(armorMeta);
    }

    public static void wrapItem(ItemStack oldItem,ItemStack newItem, ItemStack wrap, Player player, Inventory wrapGUI) {
        if (wrap == null || !wrap.hasItemMeta()) return;
        PersistentDataContainer wrapData = wrap.getItemMeta().getPersistentDataContainer();
        if (wrapData.has(new NamespacedKey(plugin, "type"), PersistentDataType.STRING) && wrapData.has(new NamespacedKey(plugin, "model"), PersistentDataType.INTEGER)) {
            String type = wrapData.get(new NamespacedKey(plugin, "type"), PersistentDataType.STRING);
            int model = wrapData.get(new NamespacedKey(plugin,"model"), PersistentDataType.INTEGER);

            //if (newItem.getType().toString().toLowerCase().contains(type.toLowerCase()) || compatiblePaperWrap(newItem, wrap)) {
                ItemMeta itemMeta = oldItem.getItemMeta();
                itemMeta.setCustomModelData(model);
                newItem.setItemMeta(itemMeta);
                wrapGUI.removeItem(wrap);

                if (convertNetheritePaper(newItem, wrap)) {
                    newItem.setType(Material.PAPER);
                }
                addArmorColor(newItem, wrap);
                wrapGUI.setItem(12,newItem);
                player.sendMessage(Util.colorcode("&#b9ddffW&#badcffo&#bbdcffw&#bcdbff!&#bedbff! &#bfdaffT&#c0daffh&#c1d9ffa&#c2d8fft&#c3d8ff'&#c4d7ffs &#c5d7ffs&#c7d6ffu&#c8d6ffc&#c9d5ffh &#cad4ffa &#cbd4ffc&#ccd3ffo&#cdd3ffo&#cfd2ffl &#d0d2ffw&#d1d1ffr&#d2d1ffa&#d3d0ffp&#d4cfffp&#d5cfffe&#d7ceffd &#d8ceffi&#d9cdfft&#dacdffe&#dbccffm&#dccbff, &#ddcbffe&#decaffn&#e0caffj&#e1c9ffo&#e2c9ffy&#e3c8ff!"));
            //} else {
            //    player.sendMessage(Util.colorcode("&#b9ddffW&#baddffe&#bbdcffl&#bcdcffl &#bcdbfft&#bddbffh&#bedaffi&#bfdaffs &#c0daffi&#c1d9ffs &#c2d9ffa&#c2d8ffw&#c3d8ffk&#c4d7ffw&#c5d7ffa&#c6d7ffr&#c7d6ffd&#c8d6ff.&#c8d5ff.&#c9d5ff. &#cad4ffT&#cbd4ffh&#ccd4ffe &#cdd3ffw&#ced3ffr&#ced2ffa&#cfd2ffp&#d0d1ffs &#d1d1ffa&#d2d1ffr&#d3d0ffe &#d4d0fft&#d4cfffw&#d5cfffo &#d6ceffd&#d7ceffi&#d8cefff&#d9cdfff&#dacdffe&#daccffr&#dbccffe&#dccbffn&#ddcbfft &#decbfft&#dfcaffy&#e0caffp&#e0c9ffe&#e1c9ffs&#e2c8ff?&#e3c8ff!"));
            //}
        } else {
            player.sendMessage(Util.colorcode("&#b9ddffT&#bbdcffh&#bddbffa&#c0dafft&#c2d9ff'&#c4d7ffs &#c6d6ffn&#c8d5ffo&#cbd4fft &#cdd3ffa &#cfd2ffw&#d1d1ffr&#d4d0ffa&#d6cfffp &#d8ceffs&#daccffi&#dccbffl&#dfcaffl&#e1c9ffy&#e3c8ff!"));
        }
    }

    public static void removeWrap(ItemStack item, Player player, Inventory wrapGUI) {
        if (item == null || !item.hasItemMeta()) {
            player.sendMessage(Util.colorcode("&#b9ddffA&#bbdcffw&#bcdcffw &#bedbffb&#bfdaffu&#c1d9ffm&#c2d9ffm&#c4d8ffe&#c5d7ffr&#c7d6ff.&#c8d6ff.&#cad5ff. &#cbd4ffI &#cdd3ffc&#ced3ffa&#d0d2ffn&#d1d1ff'&#d3d0fft &#d4d0ffu&#d6cfffn&#d7ceffw&#d9cdffr&#dacdffa&#dcccffp &#ddcbfft&#dfcaffh&#e0caffi&#e2c9ffs&#e3c8ff!"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(null);

        if (meta.getPersistentDataContainer().has((new NamespacedKey(plugin, "LUNA_NETHERITE")), PersistentDataType.SHORT) ||
                meta.getPersistentDataContainer().has((new NamespacedKey(plugin, "LunaCosmeticHelmet")), PersistentDataType.INTEGER)) { // added support for paper helmets
            meta.getPersistentDataContainer().getKeys().forEach(key -> {
                if (key.getKey().equalsIgnoreCase("LUNA_NETHERITE") || key.getKey().equalsIgnoreCase("LunaCosmeticHelmet")) {
                    meta.getPersistentDataContainer().remove(key);
                }
            });
            if (item.getType().equals(Material.PAPER)) {
                ItemStack newItem = new ItemStack(Material.NETHERITE_HELMET);
                if (meta.hasLore()){
                    List<String> metaLore = meta.getLore();
                    for (int i = 0; i < metaLore.size(); i++) {
                        if (ChatColor.stripColor(metaLore.get(i)).contains("Durability:")){
                            metaLore.remove(i);
                        }
                    }
                    meta.setLore(metaLore);
                }

                newItem.setItemMeta(meta);

                wrapGUI.setItem(12,newItem);
                return;
            }

            item.setType(Material.valueOf("NETHERITE" + getArmorType(item)));

            ItemStack newItem = new ItemStack(Material.valueOf("LEATHER" + getArmorType(item)));
            newItem.setItemMeta(meta);
            newItem.setType(Material.valueOf("NETHERITE" + getArmorType(item)));

            wrapGUI.setItem(12,newItem);
        } else {
            item.setItemMeta(meta);
        }
        player.sendMessage(Util.colorcode("&#b9ddffU&#badcffn&#bcdcffw&#bddbffr&#bedaffa&#c0daffp&#c1d9ffp&#c2d8ffe&#c4d8ffd &#c5d7ffi&#c6d6fft &#c7d6fff&#c9d5ffo&#cad4ffr &#cbd4ffy&#cdd3ffo&#ced3ffu&#cfd2ff! &#d1d1ffA&#d2d1ffl&#d3d0ffl &#d5cfffn&#d6cfffi&#d7ceffc&#d9cdffe &#dacdffa&#dbccffn&#dccbffd &#decbffn&#dfcaffe&#e0c9ffw&#e2c9ff!&#e3c8ff!"));
    }

    private static void addArmorColor(ItemStack item, ItemStack wrap) {
        if (!isArmor(item)) return;
        PersistentDataContainer wrapData = wrap.getItemMeta().getPersistentDataContainer();
        if (wrapData.has(new NamespacedKey(plugin,"red"), PersistentDataType.INTEGER)) {
            int red = wrapData.get(new NamespacedKey(plugin,"red"), PersistentDataType.INTEGER);
            int green = wrapData.get(new NamespacedKey(plugin,"green"), PersistentDataType.INTEGER);
            int blue = wrapData.get(new NamespacedKey(plugin,"blue"), PersistentDataType.INTEGER);

            LeatherArmorMeta itemMeta = (LeatherArmorMeta) item.getItemMeta();
            itemMeta.setColor(Color.fromRGB(red,green,blue));
            item.setItemMeta(itemMeta);
        }
    }

    private static boolean compatiblePaperWrap(ItemStack item, ItemStack wrap) {
        if (!wrap.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin,"PaperHelmet"), PersistentDataType.SHORT) ||
        !item.getType().equals(Material.NETHERITE_HELMET)) return false;
        return true;
    }
    
    private static boolean convertNetheritePaper(ItemStack item, ItemStack wrap) {
        if (!wrap.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin,"PaperHelmet"), PersistentDataType.SHORT) ||
                !item.getType().equals(Material.NETHERITE_HELMET)) return false;
        item.setType(Material.PAPER);
        addNetheriteAttributes(item, Material.PAPER,3, EquipmentSlot.HEAD);
        return true;
    }
}
