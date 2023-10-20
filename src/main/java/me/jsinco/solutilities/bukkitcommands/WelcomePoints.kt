package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.Saves.get
import me.jsinco.solutilities.Saves.save
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import me.jsinco.solutilities.utility.Util.colorcode
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

class WelcomePoints(val plugin: SolUtilities) : Listener, BukkitCommand(
    "welcomes", "Command for welcome points", "/welcomes", listOf()
) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean { // Don't judge my code
        if (args.isEmpty()) {
            val player = sender as? Player ?: return true
            player.sendMessage(colorcode(Util.prefix + "You have &a" + String.format("%,d",get().getInt("Welcomes." + player.uniqueId)) + " &#E2E2E2welcome points!"))
        } else if (args[0] == "open" && sender.hasPermission("solutilities.admin")) {
            if (args.size < 2) return true
            openWelcomes(Bukkit.getPlayerExact(args[1]) ?: return true, 400L)
            sender.sendMessage("${Util.prefix}Opened welcomes for ${args[1]} for 400 ticks")
        } else {
            Bukkit.getOfflinePlayer(args[0]).let {
                sender.sendMessage(colorcode(Util.prefix + it.name + " has &a" + String.format("%,d",get().getInt("Welcomes." + it.uniqueId)) + " &#E2E2E2welcome points!"))
            }
        }
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        val list = Util.getOnlinePlayers()
        if (sender.hasPermission("solutilities.admin") && args.size == 1) {
            list.add("open")
            return list
        }
        return list
    }

    companion object {
        var open = false
        val recentlyWelcomed: MutableList<UUID> = mutableListOf()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        openWelcomes(event.player, 400L)
    }

    private fun openWelcomes(player: Player, openTime: Long) {
        if (recentlyWelcomed.contains(player.uniqueId)) return
        recentlyWelcomed.clear()
        recentlyWelcomed.add(player.uniqueId)
        open = true

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            open = false
            recentlyWelcomed.clear()
        }, openTime)
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (!open || recentlyWelcomed.contains(event.player.uniqueId)) return

        val message = event.message.lowercase()
        val keywords: List<String> = plugin.config.getStringList("Welcome-keywords")

        var welcomed = false
        for (keyword in keywords) {
            if (message.contains(keyword)) {
                welcomed = true
                break
            }
        }
        if (!welcomed) return
        val player = event.player
        if (get().getConfigurationSection("Welcomes") == null) get().createSection("Welcomes")
        if (!get().contains("Welcomes.${player.uniqueId}")) {
            get().set("Welcomes.${player.uniqueId}", 1)
        }
        get().set("Welcomes.${player.uniqueId}", get().getInt("Welcomes." + event.player.uniqueId) + 1)
        save()

        player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§a+1 Welcome point §7(Total: " + String.format("%,d",get().getInt("Welcomes." + event.player.uniqueId)) + ")"))
        recentlyWelcomed.add(event.player.uniqueId)
    }
}