package me.jsinco.solutilities.celestial.celeste.aries;

import me.jsinco.solutilities.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AriesAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) return false;

        switch (strings[0]) {
            case "add" -> addItem(p, Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
            case "remove" -> removeItem(p);
            case "shop" -> MiscShop.openInventory(p, Integer.parseInt(strings[1]));
            case "reload" -> {
                MiscShop.adminInitializeShop();
                p.sendMessage(ColorUtils.colorcode("&#ffb89cR&#ffb8a8e&#ffb8b3l&#ffb8bfo&#ffb9caa&#ffb9d6d&#ffb9e1e&#ffb9edd&#ffb9f8!"));
            }
        }


        return true;
    }

    private void addItem (Player p, int price, int solCoins) {
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            p.sendMessage(ColorUtils.colorcode("&#ffb89cI &#ffb8a3c&#ffb8aaa&#ffb8b1n&#ffb8b8'&#ffb8bft &#ffb8c6a&#ffb9ced&#ffb9d5d &#ffb9dct&#ffb9e3h&#ffb9eai&#ffb9f1s&#ffb9f8?"));
        } else {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ","_");
            MiscFile.get().set("Items." + name, item);
            MiscFile.get().set("Prices." + name, price);
            MiscFile.get().set("SolCoins." + name, solCoins);
            p.sendMessage(ColorUtils.colorcode("&#ffb89cA&#ffb8aed&#ffb8c1d&#ffb9d3e&#ffb9e6d&#ffb9f8! &f" + name));
            MiscFile.save();
        }
    }

    private void removeItem (Player p) {
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            p.sendMessage(ColorUtils.colorcode("&#ffb89c?&#ffb9ca?&#ffb9f8?"));
        } else {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ","_");
            MiscFile.get().set("Items." + name, null);
            MiscFile.get().set("Prices." + name, null);
            MiscFile.get().set("SolCoins." + name, null);
            p.sendMessage(ColorUtils.colorcode("&#ffb89cR&#ffb89fe&#ffb8a2m&#ffb8a5o&#ffb8a8v&#ffb8abe&#ffb8aed &#ffb8b1f&#ffb8b5r&#ffb8b8o&#ffb8bbm &#ffb8bes&#ffb8c1h&#ffb8c4o&#ffb8c7p&#ffb9ca, &f" + name +
                    ColorUtils.colorcode(" &#ffb9cdi&#ffb9d0f &#ffb9d3i&#ffb9d6t &#ffb9d9w&#ffb9dca&#ffb9dfs &#ffb9e3t&#ffb9e6h&#ffb9e9e&#ffb9ecr&#ffb9efe&#ffb9f2.&#ffb9f5.&#ffb9f8.")));
            MiscFile.save();
        }
    }
}
