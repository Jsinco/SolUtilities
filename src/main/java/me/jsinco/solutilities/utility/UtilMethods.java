package me.jsinco.solutilities.utility;

import me.jsinco.solutilities.Util;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

import static me.jsinco.solutilities.celestial.luna.plugin;

public class UtilMethods {

    public static void armorStandHands(Player player, boolean helm) {
        Entity ent = player.getTargetEntity(5);
        if (ent instanceof ArmorStand stand) {
            ItemStack item;
            if (!helm) {
                item = stand.getEquipment().getItemInMainHand();
                stand.getEquipment().setItemInMainHand(player.getInventory().getItemInMainHand());
            } else {
                item = stand.getEquipment().getHelmet();
                stand.getEquipment().setHelmet(player.getInventory().getItemInMainHand());
            }
            player.getInventory().setItemInMainHand(item);
        }
        else {
            player.sendMessage(Util.colorcode(pl.getConfig().getString("prefix") + "You must be looking at an armor stand to do this!"));
        }
    }


    // bless who made this, credit:
    // https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    public static double evalMath(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public static BookMeta formatBook(BookMeta book) {
        // Get
        List<String> pages = book.getPages();
        List<String> newPages = new ArrayList<>();

        // Format
        for (String page : pages) {
            newPages.add(Util.colorcode(page));
        }

        book.setPages(newPages);
        return book;
    }


    public static String itemNameFromMaterial(ItemStack item) {
        String name = item.getType().toString().toLowerCase().replace("_", " ");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                name = name.substring(0, i) + " " + String.valueOf(name.charAt(i + 1)).toUpperCase() + name.substring(i + 2); // Capitalize first letter of each word
            }
        }
        return name;
    }

    public static String defaultMinecraftColor(ItemStack itemStack, boolean b) {
        char colorChar = b ? '§' : '&';

        Material material = itemStack.getType();
        String materialName = material.name();

        String returnColor = colorChar + "f";

        if (materialName.endsWith("_HEAD")) {
            returnColor = colorChar + "e";
        } else if (materialName.contains("DISC")) {
            returnColor = colorChar + "b";
        }

        switch (material) {
            case DRAGON_EGG, ENCHANTED_GOLDEN_APPLE, MOJANG_BANNER_PATTERN -> returnColor = colorChar + "d";
            case CONDUIT, END_CRYSTAL, GOLDEN_APPLE -> returnColor = colorChar + "b";
            case TOTEM_OF_UNDYING, HEART_OF_THE_SEA, NETHER_STAR, CREEPER_BANNER_PATTERN, SKULL_BANNER_PATTERN, EXPERIENCE_BOTTLE, ENCHANTED_BOOK, ELYTRA -> returnColor = colorChar + "e";
            case BEACON -> returnColor = colorChar + "7";
        }

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) { // enchants override colors
            returnColor = colorChar + "b";
        }

        return returnColor;
    }
}
