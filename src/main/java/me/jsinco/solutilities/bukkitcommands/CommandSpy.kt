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
        val commandSpyList: MutableList<Player> = mutableListOf()
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!Util.checkPermission(sender, "solutilities.command.commandspy")) return true
        if (args.isEmpty()) {
            val player = sender as? Player ?: return false
            setSpy(player)
        } else {
            Bukkit.getPlayer(args[0]) ?: return false
            val target = Bukkit.getPlayer(args[0])
            if (target != null) {
                setSpy(target)
            }
            sender.sendMessage(Util.colorcode("${Util.prefix}Command spy toggled for ${target?.name}"))
        }
        return true
    }


    private fun setSpy(player: Player) {
        val switch: String = if (commandSpyList.contains(player)) {
            commandSpyList.remove(player)
            "&cdisabled"
        } else {
            commandSpyList.add(player)
            "&aenabled"
        }
        player.sendMessage(Util.colorcode("${Util.prefix}Command spy $switch"))
    }

    @EventHandler
    fun onCommandPreProcess(event: PlayerCommandPreprocessEvent) {
        for (player in commandSpyList) {
            if (player != event.player) {
                player.sendMessage(Util.colorcode("${Util.prefix}&d${event.player.name} &#E2E2E2executed &d${event.message}"))
            }
        }
    }
}