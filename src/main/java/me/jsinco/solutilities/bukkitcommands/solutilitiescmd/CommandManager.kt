package me.jsinco.solutilities.bukkitcommands.solutilitiescmd

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands.*
import me.jsinco.solutilities.utility.Util
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.util.*

class CommandManager(val plugin: SolUtilities) : BukkitCommand(
    "solutilities", "Main command for SolUtilities", "/solutilities <subcommand>", listOf("solace")
) {

    private val subCommands: MutableMap<String, SubCommand> = mutableMapOf()

    init {
        subCommands["donationmsg"] = DonationMsgCommand()
        subCommands["reload"] = ReloadCommand()
        subCommands["silent"] = SilentCommand()
        subCommands["rankupbroadcast"] = RankupBroadcastCommand()
        subCommands["nbtsee"] = NBTSeeCommand()
        subCommands["nicksee"] = NickPreviewCommand()
        subCommands["voucher"] = Vouchers()
        subCommands["armorstand"] = ArmorstandCommand()
        subCommands["discordreward"] = DiscordRewardCommand()
        subCommands["checkpermission"] = CheckPermissionCommand()
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (args.isEmpty() || !subCommands.containsKey(args[0])) {
            sender.sendMessage("${Util.prefix}Unknown subcommand.")
            return true
        }

        val subCommand: SubCommand = subCommands[args[0]]!!
        if (subCommand.playerOnly() && sender !is Player) {
            sender.sendMessage("${Util.prefix}This command can only be executed by players.")
            return true
        } else if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission()!!)) {
            sender.sendMessage("${Util.prefix}You do not have permission to execute this command.")
            return true
        }

        subCommand.execute(plugin, sender, args)
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        if (args.size == 1) {
            val subCommandNames: MutableList<String> = mutableListOf()
            for (subcommand in subCommands) {
                if (subcommand.value.permission() == null || sender.hasPermission(subcommand.value.permission()!!)) {
                    subCommandNames.add(subcommand.key)
                }
            }
            return subCommandNames
        }

        if (!subCommands.containsKey(args[0])) return Collections.emptyList()
        val subCommand: SubCommand = subCommands[args[0]]!!

        return subCommand.tabComplete(plugin, sender, args) ?: Util.getOnlinePlayers()
    }
}