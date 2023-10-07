package me.jsinco.solutilities.features

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.hooks.PlayerPointsHook
import me.jsinco.solutilities.utility.Util
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


class LegacySolcoins(val plugin: SolUtilities) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val item: ItemStack = event.player.inventory.itemInMainHand
        if (item.type.isAir || !item.hasItemMeta() || !event.action.isRightClick) return
        if (item.itemMeta.persistentDataContainer.has(NamespacedKey(plugin, "SolCoins"), PersistentDataType.INTEGER)) {
            val amount: Int = item.itemMeta.persistentDataContainer.get(NamespacedKey(plugin, "SolCoins"), PersistentDataType.INTEGER)?.times(
                event.player.inventory.itemInMainHand.amount) ?: return

            val ppAPI: PlayerPointsAPI = PlayerPointsHook.playerPointsAPI ?: return
            ppAPI.give(event.player.uniqueId, amount)
            item.amount -= 1
            event.player.sendMessage("${Util.prefix}Redeeming a legacy Solcoin voucher!")
            event.player.sendMessage(Util.colorcode(Util.prefix + "You have redeemed " + amount + " &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&lc&#f8ab3a&lo&#f8bb3a&li&#f8cb3a&ln&#f8db3a&ls&r&#E2E2E2\uE54C!"))
        }
    }
}