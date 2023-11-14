package me.jsinco.solutilities.features

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.persistence.PersistentDataType

class ItemLocker (val plugin: SolUtilities) : BukkitCommand(
    "itemlock", "Command for locking items", "/itemlock", listOf()
), Listener {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        val player = sender as? Player ?: return false

        val item = player.inventory.itemInMainHand
        if (item.type.isAir) return true

        val meta = item.itemMeta
        val data = meta.persistentDataContainer
        var string = "No permission!"

        if (data.has(NamespacedKey(plugin, "lockeditem"), PersistentDataType.BOOLEAN)) {
            data.remove(NamespacedKey(plugin, "lockeditem"))
            string = "Item lock &cdisabled"
            item.itemMeta = meta
        } else if (sender.hasPermission("solutilities.command.itemlock")) {
            data.set(NamespacedKey(plugin, "lockeditem"), PersistentDataType.BOOLEAN, true)
            string = "Item lock &aenabled"
            item.itemMeta = meta
        }

        player.sendMessage(Util.colorcode("${Util.prefix}$string"))
        return true
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onItemDrop(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.itemMeta.persistentDataContainer.has(NamespacedKey(plugin, "lockeditem"), PersistentDataType.BOOLEAN)) {
            event.player.sendMessage("${Util.prefix}This item is locked and cannot be dropped! Use [/itemlock] to unlock it!")
            event.isCancelled = true
        }
    }
}