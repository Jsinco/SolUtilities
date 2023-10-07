package me.jsinco.solutilities.celestial.celeste.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util.colorcode
import me.jsinco.solutilities.celestial.CelestialFile.get
import me.jsinco.solutilities.celestial.CelestialFile.save
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.Collections

class AddItemCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        if (args.size < 2) return
        val p = sender as Player
        val item: ItemStack = p.inventory.itemInMainHand
        if (!item.hasItemMeta() || !item.itemMeta.hasDisplayName()) {
            p.sendMessage(colorcode("&#ffc8c8T&#ffcac8h&#ffcbc8i&#ffcdc8s &#ffcfc8i&#ffd0c8t&#ffd2c7e&#ffd4c7m &#ffd5c7d&#ffd7c7o&#ffd9c7e&#ffdac7s&#ffdcc7n&#ffdec7'&#ffdfc7t &#ffe1c7h&#ffe3c7a&#ffe4c6v&#ffe6c6e &#ffe7c6e&#ffe9c6n&#ffebc6o&#ffecc6u&#ffeec6g&#fff0c6h &#fff1c6m&#fff3c6e&#fff5c5t&#fff6c5a&#fff8c5d&#fffac5a&#fffbc5t&#fffdc5a"))
        } else {
            val name = ChatColor.stripColor(item.itemMeta.displayName)!!.replace(" ", "_")
            get()["Items.$name"] = item
            get()["Prices.$name"] = args[1].toInt()
            p.sendMessage(colorcode("&#ffc8c8I &#ffd3c7a&#ffddc7d&#ffe8c6d&#fff2c6e&#fffdc5d &f$name &#ffc8c8t&#ffcfc8o &#ffd5c7t&#ffdcc7h&#ffe3c7e &#ffe9c6s&#fff0c6h&#fff6c5o&#fffdc5p"))
            save()
        }
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return Collections.singletonList("<price>")
    }

    override fun permission(): String {
        return "solutilities.celestial.celeste"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}