package me.jsinco.solutilities.utility

import me.jsinco.solutilities.SolUtilities
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerEditBookEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.util.*

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
        val player = event.player
        if (!player.scoreboardTags.contains("solutilities.silent")) {
            player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 0.2f, 0.78f)
        }
    }

    @EventHandler (ignoreCancelled = true)
    fun onPlayerTP(event: PlayerTeleportEvent) {
        if (!event.player.scoreboardTags.contains("solutilities.silent") && event.cause == PlayerTeleportEvent.TeleportCause.COMMAND) {
            val sound: Sound
            val pitch: Float
            val random = Random().nextInt(1, 3)
            if (random == 2) {
                sound = Sound.BLOCK_PORTAL_TRAVEL
                pitch = 8f
            } else {
                sound = Sound.ENTITY_ENDERMAN_TELEPORT
                pitch = 1f
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin,
                { event.player.playSound(event.player.location, sound, 0.2f, pitch) },
                1L)
        }
    }

    @EventHandler (ignoreCancelled = true)
    fun onPlayerEditBook(event: PlayerEditBookEvent) {
        if (!event.player.hasPermission("solutilities.colorbooks")) return
        val book = Util.formatBook(event.newBookMeta)
        event.setNewBookMeta(book)
    }
}
