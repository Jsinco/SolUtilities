package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.FileManager
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CopyItem : SubCommand {

    companion object {
        val fileManager = FileManager("data/copiedItems.yml")
        val copiedItems = fileManager.getFileYaml()
    }

    init {
        fileManager.generateFile()
    }

    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        sender as Player
        if  (args.isEmpty()) {
            sender.sendMessage("Usage: /solutilities copyitem <pull|write> <itemname>")
            return
        }
        when (args[1].lowercase()) {
            "pull" -> {
                val itemName = args[2]
                val item = copiedItems.getItemStack(itemName)
                if (item != null) {
                    sender.inventory.addItem(item)
                }
            }

            "write" -> {
                val item = sender.inventory.itemInMainHand
                val itemName = (if (item.itemMeta.hasDisplayName()) ChatColor.stripColor(item.itemMeta.displayName) else item.type.name)!!
                    .replace(" ", "_").lowercase()
                copiedItems.set("items.$itemName", item)
                fileManager.saveFileYaml()
                sender.sendMessage("${Util.prefix}Successfully copied item to file.")
            }
        }
    }

    override fun tabComplete(
        plugin: SolUtilities,
        sender: CommandSender,
        args: Array<out String>
    ): MutableList<String>? {
        when (args.size) {
            2 -> {
                return mutableListOf("pull", "write")
            }

            3 -> {
                if (args[1].lowercase() == "pull") {
                    return copiedItems.getConfigurationSection("items")!!.getKeys(false).toMutableList()
                }
            }
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.command.copyitem"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}