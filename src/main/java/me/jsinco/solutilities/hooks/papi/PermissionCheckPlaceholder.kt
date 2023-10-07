package me.jsinco.solutilities.hooks.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.hooks.LuckPermsHook
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player


class PermissionCheckPlaceholder : PlaceholderExpansion() {
    private val plugin: SolUtilities = SolUtilities.getPlugin()
    private val luckPerms: LuckPerms? = LuckPermsHook.luckPermsInstance

    override fun getIdentifier(): String {
        return "permission"
    }

    override fun getAuthor(): String {
        return plugin.description.authors[0]
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        if (luckPerms == null || player == null) return null
        val user: User = luckPerms.userManager.loadUser(player.uniqueId).join()
        val node: Node = Node.builder(params).build()

        val onlinePlayer: Player? = player.player
        return if (user.data().toCollection().contains(node) || (onlinePlayer != null && onlinePlayer.hasPermission(params))
        ) {
            "yes"
        } else {
            "no"
        }
    }
}