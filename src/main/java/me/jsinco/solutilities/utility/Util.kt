package me.jsinco.solutilities.utility

import me.jsinco.solutilities.SolUtilities
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.*
import kotlin.math.*

object Util {

    lateinit var prefix: String
    val MATERIALS_STRING: MutableList<String> = Material.entries.map { it.name.lowercase() }.toMutableList()
    val ONLINE_PLAYERS = Bukkit.getOnlinePlayers().map { it.name.lowercase() }.toMutableList()
    fun loadUtils() {
        prefix = colorcode(SolUtilities.getPlugin().config.getString("prefix")!!)
    }

    const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    @JvmStatic
    fun colorcode(text: String): String {
        val texts = text.split(String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equals("&", ignoreCase = true)) {
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7))
                } else {
                    finalText.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }


    @JvmStatic
    fun colorArrayList(list: List<String>): List<String>? {
        val coloredList: MutableList<String> = ArrayList()
        for (string in list) {
            coloredList.add(colorcode(string))
        }
        return coloredList
    }

    @JvmStatic
    fun colorArray(array: Array<String>): Array<String>? {
        for (i in array.indices) {
            array[i] = colorcode(array[i])
        }
        return array
    }

    @JvmStatic
    fun getOnlinePlayers(): MutableList<String> {
        return Bukkit.getOnlinePlayers().map { it.name.lowercase() }.toMutableList()
    }

    // https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    @JvmStatic
    fun evalMath(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm() // addition
                    else if (eat('-'.code)) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor() // multiplication
                    else if (eat('/'.code)) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return +parseFactor() // unary plus
                if (eat('-'.code)) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    if (!eat(')'.code)) throw RuntimeException("Missing ')'")
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                    while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
                    val func = str.substring(startPos, pos)
                    if (eat('('.code)) {
                        x = parseExpression()
                        if (!eat(')'.code)) throw RuntimeException("Missing ')' after argument to $func")
                    } else {
                        x = parseFactor()
                    }
                    x =
                        if (func == "sqrt") sqrt(x) else if (func == "sin") sin(Math.toRadians(x)) else if (func == "cos") cos(
                            Math.toRadians(x)
                        ) else if (func == "tan") tan(Math.toRadians(x)) else throw RuntimeException(
                            "Unknown function: $func"
                        )
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.code)) x = x.pow(parseFactor()) // exponentiation
                return x
            }
        }.parse()
    }


    fun playServerSound(sound: Sound, volume: Float, pitch: Float) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.scoreboardTags.contains("solutilities.silent")) {
                player.playSound(player.location, sound, volume, pitch)
            }
        }
    }

    fun checkPermission(sender: CommandSender, permission: String): Boolean {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(colorcode(prefix + "You do not have permission to use this command"))
            return false
        }
        return true
    }

    fun formatBook(book: BookMeta): BookMeta {
        val pages = book.pages // Get
        val newPages: MutableList<String> = java.util.ArrayList()

        for (page in pages) { // Format
            newPages.add(colorcode(page!!))
        }
        book.pages = newPages
        return book
    }

    @JvmStatic
    fun itemNameFromMaterial(item: ItemStack): String {
        var name = item.type.toString().lowercase(Locale.getDefault()).replace("_", " ")
        name = name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        for (i in 0 until name.length) {
            if (name[i] == ' ') {
                name =
                    name.substring(0, i) + " " + name[i + 1].toString().uppercase(Locale.getDefault()) + name.substring(
                        i + 2
                    ) // Capitalize first letter of each word
            }
        }
        return name
    }

    @JvmStatic
    fun defaultMinecraftColor(itemStack: ItemStack, b: Boolean): String {
        val colorChar = if (b) '§' else '&'
        val material = itemStack.type
        val materialName = material.name
        var returnColor = colorChar.toString() + "f"
        if (materialName.endsWith("_HEAD")) {
            returnColor = colorChar.toString() + "e"
        } else if (materialName.contains("DISC")) {
            returnColor = colorChar.toString() + "b"
        }
        when (material) {
            Material.DRAGON_EGG, Material.ENCHANTED_GOLDEN_APPLE, Material.MOJANG_BANNER_PATTERN -> returnColor =
                colorChar.toString() + "d"

            Material.CONDUIT, Material.END_CRYSTAL, Material.GOLDEN_APPLE -> returnColor = colorChar.toString() + "b"
            Material.TOTEM_OF_UNDYING, Material.HEART_OF_THE_SEA, Material.NETHER_STAR, Material.CREEPER_BANNER_PATTERN, Material.SKULL_BANNER_PATTERN, Material.EXPERIENCE_BOTTLE, Material.ENCHANTED_BOOK, Material.ELYTRA -> returnColor =
                colorChar.toString() + "e"

            Material.BEACON -> returnColor = colorChar.toString() + "7"

            else -> {
            }
        }
        if (itemStack.hasItemMeta() && itemStack.itemMeta.hasEnchants()) { // enchants override colors
            returnColor = colorChar.toString() + "b"
        }
        return returnColor
    }
}