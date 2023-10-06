package me.jsinco.solutilities.features.joins

import me.jsinco.solutilities.SolUtilities

class Joins {
    companion object {
        private val plugin: SolUtilities = SolUtilities.getPlugin()
        val JOIN_MSGS: List<String> = plugin.config.getStringList("joins.join-messages")
        val QUIT_MSGS: List<String> = plugin.config.getStringList("joins.quit-messages")
        val VANISH_CMDS: List<String> = plugin.config.getStringList("joins.vanish-commands")
    }
}