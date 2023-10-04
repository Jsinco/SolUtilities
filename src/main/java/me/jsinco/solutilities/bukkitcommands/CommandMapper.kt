package me.jsinco.solutilities.bukkitcommands

import me.jsinco.solutilities.SolUtilities
import me.jsinco.solutilities.bukkitcommands.commands.PingCommand
import me.jsinco.solutilities.bukkitcommands.commands.RanksMenu
import me.jsinco.solutilities.bukkitcommands.commands.SearchShopAlias
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import java.lang.reflect.Field

class CommandMapper(val plugin: SolUtilities) {


    companion object {
        private val commands: MutableMap<String, CommandExecutor> = mutableMapOf()

        @JvmStatic
        fun register(name: String, commandExecutor: CommandExecutor) {
            commands[name] = commandExecutor
        }
    }


    init {
        // Commands incase they are not registered with method above
        commands["ls"] = SearchShopAlias(plugin)
        commands["lb"] = SearchShopAlias(plugin)
        commands["ranks"] = RanksMenu(plugin)
        commands["ping"] = PingCommand(plugin)

        // Mapper
        mapCommands()
    }

    private fun mapCommands() {
        try {
            val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
            bukkitCommandMap.isAccessible = true
            val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap

            for (command in commands) {
                val cmd = command.value

                val pluginCommand: PluginCommand = plugin.getCommand(command.key)!!
                pluginCommand.setExecutor(cmd)
                commandMap.register(command.key, "solutilities", pluginCommand)
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}