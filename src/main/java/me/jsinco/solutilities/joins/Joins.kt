package me.jsinco.solutilities.joins

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.bukkitcommands.CommandMapper

class Joins(val plugin: SolUtilities) {

    init {
        CommandMapper.register("joins", JoinsCommand(plugin))
        plugin.server.pluginManager.registerEvents(Listeners(plugin), plugin)
    }

    companion object {
        private val plugin: SolUtilities = SolUtilities.getPlugin(SolUtilities::class.java)
        val joinMessages: List<String> = plugin.config.getStringList("joins.join-messages")
        val quitMessages: List<String> = plugin.config.getStringList("joins.quit-messages")
    }
}