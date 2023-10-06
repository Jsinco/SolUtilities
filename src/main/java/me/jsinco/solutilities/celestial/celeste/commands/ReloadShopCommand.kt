package me.jsinco.solutilities.celestial.celeste.commands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util.colorcode
import me.jsinco.solutilities.celestial.celeste.Shop
import org.bukkit.command.CommandSender

class ReloadShopCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        Shop.adminInitializeShop()
        sender.sendMessage(colorcode("&#ffc8c8I &#ffccc8r&#ffcfc8e&#ffd3c7l&#ffd6c7o&#ffdac7a&#ffddc7d&#ffe1c7e&#ffe4c6d &#ffe8c6t&#ffebc6h&#ffefc6e &#fff2c6S&#fff6c5h&#fff9c5o&#fffdc5p"))
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.celestial.celeste"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}