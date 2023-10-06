package me.jsinco.solutilities.bukkitcommands

import io.papermc.paper.event.player.AsyncChatEvent
import me.jsinco.solutilities.Saves.get
import me.jsinco.solutilities.Saves.save
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.Util
import me.jsinco.solutilities.Util.colorcode
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

class WelcomePoints(val plugin: SolUtilities) : Listener, BukkitCommand(
    "welcomes", "Command for welcome points", "/welcomes", listOf()
) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        val player = sender as? Player ?: return true
        if (args == null) {
            player.sendMessage(colorcode(Util.prefix + "You have &a" + get().getInt("Welcomes." + player.uniqueId) + " &#E2E2E2welcome points!"))
        } else {
            Bukkit.getOfflinePlayer(args[0]).let {
                player.sendMessage(colorcode(Util.prefix + it.name + " has &a" + get().getInt("Welcomes." + it.uniqueId) + " &#E2E2E2welcome points!"))
            }
        }
        return true
    }


    companion object {
        var open = false
        val recentlyWelcomed: MutableList<UUID> = mutableListOf()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        openWelcomes(event.player)
    }

    private fun openWelcomes(player: Player) {
        if (recentlyWelcomed.contains(player.uniqueId)) return
        recentlyWelcomed.clear()
        recentlyWelcomed.add(player.uniqueId)
        open = true

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            open = false
            recentlyWelcomed.clear()
        }, 400L)
    }

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        if (!open || recentlyWelcomed.contains(event.player.uniqueId)) return

        val message = event.message().toString().lowercase()
        val keywords: List<String> = plugin.config.getStringList("Welcomes-keywords")


        var welcomed = false
        for (keyword in keywords) {
            if (!message.contains(keyword)) continue
            welcomed = true
        }

        if (!welcomed) return
        event.player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§a+1 Welcome point §7(Total: " + get().getInt("Welcomes." + event.player.uniqueId) + ")"))
        if (get().getConfigurationSection("Welcomes") == null) get().createSection("Welcomes")
        if (!get().contains("Welcomes." + event.player.uniqueId)) {
            get()["Welcomes." + event.player.uniqueId] = 1
        }
        get()["Welcomes." + event.player.uniqueId] = get().getInt("Welcomes." + event.player.uniqueId) + 1
        save()

        recentlyWelcomed.add(event.player.uniqueId)
    }
}