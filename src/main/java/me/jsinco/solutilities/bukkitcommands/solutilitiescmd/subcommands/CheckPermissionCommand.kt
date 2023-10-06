package me.jsinco.solutilities.bukkitcommands.solutilitiescmd.subcommands

import me.jsinco.oneannouncer.discord.JDAMethods
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.SubCommand
import me.jsinco.solutilities.hooks.LuckPermsHook
import me.jsinco.solutilities.utility.Util
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class CheckPermissionCommand : SubCommand {

    private val luckPerms: LuckPerms? = LuckPermsHook.luckPermsInstance

    override fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>) {
        if (luckPerms == null) {
            sender.sendMessage("${Util.prefix} LuckPerms is not enabled!")
            return
        }

        if (args.size < 4) { // &#E2E2E2
            sender.sendMessage(Util.colorcode("${Util.prefix} Usage: /solutilities checkpermission <player> &d(Checking if player has: &a<permission.node>&d) &d(If true, adding &a<permission.node> &dto player)"))
            return
        }

        val offlinePlayer = Bukkit.getOfflinePlayer(args[1])
        val permissionToCheck: Node = Node.builder(args[2]).value(true).build()
        val permissionToAdd = Node.builder(args[3]).value(true).build()
        var user: User? = null
        var addedPermission = false

        if (!offlinePlayer.isOnline) {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                user = luckPerms.userManager.loadUser(offlinePlayer.uniqueId).join()
                if (user!!.data().toCollection().contains(permissionToCheck)) {
                    user!!.data().add(permissionToAdd)
                    addedPermission = true
                }
            })
        } else {
            user = luckPerms.userManager.getUser(offlinePlayer.uniqueId) ?: return
            if (user!!.data().toCollection()
                    .contains(permissionToCheck) || offlinePlayer.player!!.hasPermission(args[2])
            ) {
                user!!.data().add(permissionToAdd)
                addedPermission = true
            }
        }
        if (user != null && addedPermission) {
            luckPerms.userManager.saveUser(user!!)
            sender.sendMessage(Util.colorcode("${Util.prefix} Successfully upgraded ${offlinePlayer.name} to ${args[3]}"))
        } else {
            sender.sendMessage(Util.colorcode("${Util.prefix} Failed to upgrade ${offlinePlayer.name}. Player does not have permission: ${args[2]}"))
            JDAMethods.sendMessageDiscordChannel(
                "1143270088510804068",
                "Failed to upgrade ${offlinePlayer.name}. Player does not have permission: ${args[2]}",
                false
            )
        }
    }

    override fun tabComplete(
        plugin: SolUtilities,
        sender: CommandSender,
        args: Array<out String>
    ): MutableList<String>? {
        when (args.size) {
            3 -> {
                return mutableListOf("checking <permission.node>")
            }

            4 -> {
                return mutableListOf("adding <permission.node>")
            }
        }
        return null
    }

    override fun permission(): String {
        return "solutilities.command.checkpermission"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}