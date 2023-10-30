package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.utility.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandSpy : BukkitCommand(
    "commandspy", "Toggles commandspy", "/commandspy", listOf("cmdspy")
), Listener {

    companion object {
        val commandSpyList: MutableMap<Player, String> = mutableMapOf()
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!Util.checkPermission(sender, "solutilities.command.commandspy")) return true
        val player = sender as? Player ?: return false
        if (args.isEmpty()) {
            setSpy(player, "ALL", false)
        } else {
            val target = Bukkit.getPlayerExact(args[0]) ?: return false
            setSpy(player, target.uniqueId.toString(), true)
            sender.sendMessage(Util.colorcode("${Util.prefix}Watching commands for only &d${target.name}"))
        }
        return true
    }


    private fun setSpy(player: Player, target: String, boolean: Boolean) {
        val switch: String = if (commandSpyList.contains(player) && !boolean) {
            commandSpyList.remove(player)
            "&cdisabled"
        } else {
            commandSpyList.put(player, target)
            "&aenabled"
        }
        player.sendMessage(Util.colorcode("${Util.prefix}Command spy $switch"))
    }

    @EventHandler
    fun onCommandPreProcess(event: PlayerCommandPreprocessEvent) {
        for (player in commandSpyList.keys) {
            if (player == event.player) continue
            if (commandSpyList[player] == "ALL") {
                player.sendMessage(Util.colorcode("${Util.prefix}&d${event.player.name} &#E2E2E2executed &d${event.message}"))
            } else if (commandSpyList[player] == event.player.uniqueId.toString()) {
                player.sendMessage(Util.colorcode("${Util.prefix}&d${event.player.name} &#E2E2E2executed &d${event.message}"))
            }
        }
    }
}