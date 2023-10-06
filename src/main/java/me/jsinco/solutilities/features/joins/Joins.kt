package me.jsinco.solutilities.features.joins

import me.jsinco.solutilities.SolUtilities

class Joins {
    companion object {
        private val plugin: SolUtilities = SolUtilities.getPlugin(SolUtilities::class.java)
        val joinMessages: List<String> = plugin.config.getStringList("joins.join-messages")
        val quitMessages: List<String> = plugin.config.getStringList("joins.quit-messages")
    }
}