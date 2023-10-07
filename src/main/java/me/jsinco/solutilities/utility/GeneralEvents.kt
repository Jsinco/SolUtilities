package me.jsinco.solutilities.utility

import me.jsinco.solutilities.SolUtilities
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerEditBookEvent

class GeneralEvents(private val plugin: SolUtilities) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDeath(event: EntityDeathEvent) {
        for (entity in plugin.getConfig().getStringList("DropBlocker.DeathWhitelist")) {
            if (event.entity.type == EntityType.valueOf(entity!!)) return
        }
        for (cause in plugin.getConfig().getStringList("DropBlocker.CauseWhitelist")) {
            if (event.entity.lastDamageCause != null && event.entity.lastDamageCause!!.cause == EntityDamageEvent.DamageCause.valueOf(cause!!)) return
        }
        if (event.entity.killer != null) {
            for (entity in plugin.getConfig().getStringList("DropBlocker.KillerWhitelist")) {
                if (event.entity.killer!!.type == EntityType.valueOf(entity!!)) return
            }
        }
        event.drops.clear()
        event.droppedExp = 0
    }

    @EventHandler
    fun commandPreProcess(event: PlayerCommandPreprocessEvent) {
        // TODO: TEMPORARY
        if (event.message == "/crates" && !event.player.hasPermission("excellentcrates.command")) {
            event.message = "/warp crates"
        }
    }

    @EventHandler (ignoreCancelled = true)
    fun onPlayerEditBook(event: PlayerEditBookEvent) {
        if (!event.player.hasPermission("solutilities.colorbooks")) return
        val book = Util.formatBook(event.newBookMeta)
        event.setNewBookMeta(book)
    }
}
