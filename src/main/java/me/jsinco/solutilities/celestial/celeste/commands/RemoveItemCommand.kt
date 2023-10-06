package me.jsinco.solutilities.celestial.celeste.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util.colorcode
import me.jsinco.solutilities.celestial.CelestialFile.get
import me.jsinco.solutilities.celestial.CelestialFile.save
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RemoveItemCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val p = sender as Player

        var name = ""
        if (args.size < 2) {
            val item = p.inventory.itemInMainHand
            if (!item.hasItemMeta() || !item.itemMeta.hasDisplayName()) {
                p.sendMessage(colorcode("&#ffc8c8T&#ffcac8h&#ffcbc8i&#ffcdc8s &#ffcfc8i&#ffd0c8t&#ffd2c7e&#ffd4c7m &#ffd5c7d&#ffd7c7o&#ffd9c7e&#ffdac7s&#ffdcc7n&#ffdec7'&#ffdfc7t &#ffe1c7h&#ffe3c7a&#ffe4c6v&#ffe6c6e &#ffe7c6e&#ffe9c6n&#ffebc6o&#ffecc6u&#ffeec6g&#fff0c6h &#fff1c6m&#fff3c6e&#fff5c5t&#fff6c5a&#fff8c5d&#fffac5a&#fffbc5t&#fffdc5a"))
            } else {
                name = ChatColor.stripColor(item.itemMeta.displayName)!!.replace(" ", "_")
                get()["Items.$name"] = null
                get()["Prices.$name"] = null
                save()
            }
        } else {
            name = args[1]
            if (get()["Items.$name"] != null) {
                get()["Items.$name"] = null
                get()["Prices.$name"] = null
            }
        }

        p.sendMessage(colorcode("&#ffc8c8I &#ffd0c8r&#ffd7c7e&#ffdfc7m&#ffe6c6o&#ffeec6v&#fff5c5e&#fffdc5d &f$name &#ffc8c8f&#ffcdc8r&#ffd3c7o&#ffd8c7m &#ffddc7t&#ffe3c7h&#ffe8c6e &#ffedc6s&#fff2c6h&#fff8c5o&#fffdc5p"))

    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return get().getConfigurationSection("Items")?.getKeys(false)?.toMutableList()
    }

    override fun permission(): String {
        return "solutilities.celestial.celeste"
    }

    override fun playerOnly(): Boolean {
        return true
    }


}