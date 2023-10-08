package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import me.jsinco.solutilities.utility.Util.colorcode
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class Vouchers : SubCommand, Listener {
    companion object {
        val plugin: SolUtilities = SolUtilities.getPlugin()
        val USAGE: String = "${Util.prefix}Usages: \n/solutilities voucher tag <tagID> <name>\n/solutilities voucher particle <material> particle.<node> <name>"
        val IDS: List<String> = listOf("tagid", "particleid")
        val QUEUE: MutableList<UUID> = mutableListOf()
    }

    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player

        val material: Material
        val dataType: String
        val data: String
        val name: String
        if (args.size < 3) {
            player.sendMessage(USAGE)
            return
        }
        when (args[1].lowercase()) {
            "tag" -> {
                dataType = "tagid"
                material = Material.NAME_TAG
                data = args[2].lowercase()
                name = args.joinToString(" ")
                    .replace("${args[0]} ${args[1]} ${args[2]} ", "")
                    .trim()
            }
            "particle" -> {
                dataType = "particleid"
                if (args.size < 4) {
                    player.sendMessage(USAGE)
                    return
                }
                material = Material.getMaterial(args[2].uppercase()) ?: return
                data = args[3].lowercase()
                name = args.joinToString(" ")
                    .replace("${args[0]} ${args[1]} ${args[2]} ${args[3]} ", "")
                    .trim()
            }
            else -> {
                player.sendMessage(USAGE)
                return
            }
        }

        val item = ItemStack(material)
        val meta = item.itemMeta!!

        meta.setDisplayName(IridiumColorAPI.process(colorcode(name)))
        meta.lore = listOf("§7Right-click to redeem!")
        meta.persistentDataContainer.set(NamespacedKey(plugin, dataType), PersistentDataType.STRING, data.replace("particle.", "").trim())

        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
        item.itemMeta = meta
        player.inventory.addItem(item)
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        when (args.size) {
            2 -> {
                return mutableListOf("tag", "particle")
            }
            3 -> {
                if (args[1] == "tag") {
                    return mutableListOf("<tagID>")
                } else if (args[1] == "particle") {
                    val list: MutableList<String> = ArrayList(Util.MATERIALS_STRING)
                    return list
                }
            }
            4 -> {
                if (args[1] == "particle") {
                    return mutableListOf("particle.<node>")
                } else if (args[1] == "tag") {
                    return mutableListOf("<name>")
                }
            }
            5 -> {
                if (args[1] == "particle") {
                    return mutableListOf("<name>")
                }
            }
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.command.vouchers"
    }

    override fun playerOnly(): Boolean {
        return true
    }


    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val meta = item.itemMeta ?: return
        var dataType: String? = null
        for (id in IDS) {
            if (meta.persistentDataContainer.has(NamespacedKey(plugin, id), PersistentDataType.STRING)) {
                dataType = id
                break
            }
        }
        if (dataType == null) return
        else if (!QUEUE.contains(event.player.uniqueId)) {
            event.player.sendMessage(colorcode(Util.prefix + "Are you sure you want to use this tag? &6Right-click &#E2E2E2again to confirm."))
            QUEUE.add(event.player.uniqueId)
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                QUEUE.remove(event.player.uniqueId)
            }, 100L)
            event.isCancelled = true
            return
        }

        val player = event.player
        val data = meta.persistentDataContainer.get(NamespacedKey(plugin, dataType), PersistentDataType.STRING) ?: return

        when (dataType) {
            "tagid" -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission set eternaltags.tag.$data true")
            }
            "particleid" -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission set particle.$data true")
            }
        }
        event.player.sendMessage(colorcode("${Util.prefix}You have redeemed a ${meta.displayName}&#E2E2E2 voucher!"))
        item.amount -= 1
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        QUEUE.remove(player.uniqueId)
        event.isCancelled = true
    }
}