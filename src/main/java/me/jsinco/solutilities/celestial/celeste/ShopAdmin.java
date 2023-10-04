package me.jsinco.solutilities.celestial.celeste;

import me.jsinco.solutilities.ColorUtils;
import me.jsinco.solutilities.celestial.CelestialFile;
import me.jsinco.solutilities.celestial.celeste.Shop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) return false;

        switch (strings[0]) {
            case "add" -> addItem(p, Integer.parseInt(strings[1]));
            case "remove" -> removeItem(p);
            case "shop" -> {
                Shop.openInventory(p, Integer.parseInt(strings[1]));
            }
            case "reload" -> {
                Shop.adminInitializeShop();
                p.sendMessage(ColorUtils.colorcode("&#ffc8c8I &#ffccc8r&#ffcfc8e&#ffd3c7l&#ffd6c7o&#ffdac7a&#ffddc7d&#ffe1c7e&#ffe4c6d &#ffe8c6t&#ffebc6h&#ffefc6e &#fff2c6S&#fff6c5h&#fff9c5o&#fffdc5p"));
            }
            case "get" -> p.getInventory().addItem(CelestialFile.get().getItemStack("Items." + strings[1]));
        }


        return true;
    }

    private void addItem (Player p, int price) {
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            p.sendMessage(ColorUtils.colorcode("&#ffc8c8T&#ffcac8h&#ffcbc8i&#ffcdc8s &#ffcfc8i&#ffd0c8t&#ffd2c7e&#ffd4c7m &#ffd5c7d&#ffd7c7o&#ffd9c7e&#ffdac7s&#ffdcc7n&#ffdec7'&#ffdfc7t &#ffe1c7h&#ffe3c7a&#ffe4c6v&#ffe6c6e &#ffe7c6e&#ffe9c6n&#ffebc6o&#ffecc6u&#ffeec6g&#fff0c6h &#fff1c6m&#fff3c6e&#fff5c5t&#fff6c5a&#fff8c5d&#fffac5a&#fffbc5t&#fffdc5a"));
        } else {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ","_");
            CelestialFile.get().set("Items." + name, item);
            CelestialFile.get().set("Prices." + name, price);
            p.sendMessage(ColorUtils.colorcode("&#ffc8c8I &#ffd3c7a&#ffddc7d&#ffe8c6d&#fff2c6e&#fffdc5d &f" + name +
                    ColorUtils.colorcode(" &#ffc8c8t&#ffcfc8o &#ffd5c7t&#ffdcc7h&#ffe3c7e &#ffe9c6s&#fff0c6h&#fff6c5o&#fffdc5p")));
            CelestialFile.save();
        }
    }

    private void removeItem (Player p) {
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            p.sendMessage(ColorUtils.colorcode("&#ffc8c8T&#ffcac8h&#ffcbc8i&#ffcdc8s &#ffcfc8i&#ffd0c8t&#ffd2c7e&#ffd4c7m &#ffd5c7d&#ffd7c7o&#ffd9c7e&#ffdac7s&#ffdcc7n&#ffdec7'&#ffdfc7t &#ffe1c7h&#ffe3c7a&#ffe4c6v&#ffe6c6e &#ffe7c6e&#ffe9c6n&#ffebc6o&#ffecc6u&#ffeec6g&#fff0c6h &#fff1c6m&#fff3c6e&#fff5c5t&#fff6c5a&#fff8c5d&#fffac5a&#fffbc5t&#fffdc5a"));
        } else {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ","_");
            CelestialFile.get().set("Items." + name, null);
            CelestialFile.get().set("Prices." + name, null);
            p.sendMessage(ColorUtils.colorcode("&#ffc8c8I &#ffd0c8r&#ffd7c7e&#ffdfc7m&#ffe6c6o&#ffeec6v&#fff5c5e&#fffdc5d &f" + name +
                    ColorUtils.colorcode(" &#ffc8c8f&#ffcdc8r&#ffd3c7o&#ffd8c7m &#ffddc7t&#ffe3c7h&#ffe8c6e &#ffedc6s&#fff2c6h&#fff8c5o&#fffdc5p")));
            CelestialFile.save();
        }
    }
}
