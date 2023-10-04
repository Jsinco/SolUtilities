package me.jsinco.solutilities.solcmd.subcommands

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.oneannouncer.discord.JDAMethods
import me.jsinco.solutilities.Util
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.solcmd.SubCommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender


class DonationMsgCommand : SubCommand {

    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        if (args.size < 3) {
            sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}Invalid usage. §c/solutilities donationmsg <player?> <package!!>"))
            return
        }

        val player = Bukkit.getOfflinePlayer(args[1])
        val packageType: String = Util.colorcode(args.joinToString(" ").replace(args[0], "").replace(args[1], "").trim())
        Bukkit.broadcastMessage(IridiumColorAPI.process(Util.colorcode("&#f76a3b&lS&#f7863b&lt&#f8a33b&lo&#f8bf3a&lr&#f8db3a&le &8» <GRADIENT:FFAA00>&l${player.name}</GRADIENT:FDD394> &#E2E2E2has just purchased a ${packageType}&#E2E2E2! Thank you for supporting &#f76a3b&lS&#f77a3b&lo&#f78a3b&ll&#f79a3b&la&#f8ab3a&lc&#f8bb3a&le&#f8cb3a&lM&#f8db3a&lC&r&f!")))
        for (otherPlayer in Bukkit.getOnlinePlayers()) {
            otherPlayer.playSound(otherPlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 0.5f)
        }

        JDAMethods.sendMessageDiscordUser("425407651480862721", "**${player.name}** has just purchased a `${ChatColor.stripColor(packageType)}`")
    }

    override fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): List<String?>? {
        when (args.size) {
            3 -> return listOf("<package>")
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.command.donationmsg"
    }

    override fun playerOnly(): Boolean {
        return false
    }

}