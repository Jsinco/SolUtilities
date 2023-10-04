package me.jsinco.solutilities.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player


class PermissionCheckPlaceholder : PlaceholderExpansion() {

    private val luckPerms: LuckPerms? = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider

    override fun getIdentifier(): String {
        return "permission"
    }

    override fun getAuthor(): String {
        return "Jsinco"
    }

    override fun getVersion(): String {
        return "1.2.0"
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
            "true"
        } else {
            "false"
        }
    }
}