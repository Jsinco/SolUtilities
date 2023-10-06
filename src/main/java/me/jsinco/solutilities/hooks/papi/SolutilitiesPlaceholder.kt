package me.jsinco.solutilities.hooks.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.jsinco.solutilities.Saves
import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.utility.Util
import org.bukkit.entity.Player

class SolutilitiesPlaceholder : PlaceholderExpansion() {

    private val plugin: SolUtilities = SolUtilities.getPlugin()
    private val colors: List<String>? = plugin.config.getConfigurationSection("prefixes.colors")?.getKeys(false)?.stream()?.toList()


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

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        when (params) {
            "welcomes" -> {
                return Saves.get().getInt("Welcomes.$player").toString()
            }

            "prefix" -> {
                if (player == null || !player.hasPermission("solutilities.prefix.staff") || colors == null) return ""
                val string = plugin.config.getString("prefixes.staff")

                for (color in colors) {
                    if (player.hasPermission("solutilities.color.$color")) {
                        return Util.colorcode(plugin.config.getString("prefixes.colors.$color") + string)
                    }
                }

                return string?.let { Util.colorcode(it) }
            }
        }
        return null
    }
}