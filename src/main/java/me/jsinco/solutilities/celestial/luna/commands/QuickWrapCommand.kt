package me.jsinco.solutilities.celestial.luna.commands

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.celestial.luna.LunaUtil
import me.jsinco.solutilities.utility.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class QuickWrapCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        sender as Player

        val item = sender.inventory.itemInMainHand
        val usage = "/luna quickwrap <rgb1> <rgb2> <name>"
        if (args.size < 4) {
            sender.sendMessage("${Util.prefix}Usage: $usage")
            return
        }

        val rgb1 = args[1].replace("#", "").trim()
        val rgb2 = args[2].replace("#", "").trim()
        var name = args.joinToString(" ")
            .replace(args[0], "")
            .replace(args[1], "")
            .replace(args[2], "")
            .trim()

        if (name.contains("-inv")) {
            name = name.replace("-inv", "").trim()

            for (item1 in sender.inventory.contents) {
                if (item1 != null && item1.hasItemMeta()) {
                    val type = LunaUtil.getItemType(item1)?.substring(0, 1)?.uppercase() + LunaUtil.getItemType(item1)?.substring(1)
                    val wrapName = IridiumColorAPI.process("<GRADIENT:$rgb1>&l$name $type Wrap</GRADIENT:$rgb2>")
                    LunaUtil.createItemAsWrap(item1, wrapName)
                }
            }
        } else {
            val type = LunaUtil.getItemType(item)?.substring(0, 1)?.uppercase() + LunaUtil.getItemType(item)?.substring(1)
            val wrapName = IridiumColorAPI.process("<GRADIENT:$rgb1>&l$name $type Wrap</GRADIENT:$rgb2>")
            LunaUtil.createItemAsWrap(item, wrapName)
        }
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        when (args.size) {
            2 -> {
                return mutableListOf("<rgb1>")
            }

            3 -> {
                return mutableListOf("<rgb2>")
            }

            4 -> {
                return mutableListOf("<name>")
            }
        }
        return mutableListOf("-inv")
    }

    override fun permission(): String {
        return "solutilities.celestial.luna"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}