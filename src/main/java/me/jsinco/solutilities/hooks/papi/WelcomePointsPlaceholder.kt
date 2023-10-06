package me.jsinco.solutilities.hooks.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.jsinco.solutilities.Saves.get
import me.jsinco.solutilities.SolUtilities
import org.bukkit.OfflinePlayer

class WelcomePointsPlaceholder : PlaceholderExpansion() {
    private val plugin: SolUtilities = SolUtilities.getPlugin()

    override fun getIdentifier(): String {
        return "solutilities"
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
        return get().getInt("Welcomes.$player").toString()
    }
}