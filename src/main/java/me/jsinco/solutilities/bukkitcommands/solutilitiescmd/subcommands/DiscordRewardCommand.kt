package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.utility.Util
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class DiscordRewardCommand : SubCommand {
    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player
        if (player.persistentDataContainer.has(NamespacedKey(plugin, "discordreward"), PersistentDataType.BOOLEAN)) {
            player.sendMessage("${Util.prefix}You have already claimed your discord booster reward!")
            return
        }
        player.persistentDataContainer.set(NamespacedKey(plugin, "discordreward"), PersistentDataType.BOOLEAN, true)

        val commands: List<String> = plugin.config.getStringList("discord-booster-rewards")
        for (command in commands) {
            plugin.server.dispatchCommand(plugin.server.consoleSender, command.replace("%player%", player.name))
        }
        player.sendMessage("${Util.prefix}You have claimed your discord booster reward! Thanks for boosting our server \uE0DF")
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>? {
        return null
    }

    override fun permission(): String {
        return "solutilities.booster" // Must use legacy booster permission node
    }

    override fun playerOnly(): Boolean {
        return true
    }
}