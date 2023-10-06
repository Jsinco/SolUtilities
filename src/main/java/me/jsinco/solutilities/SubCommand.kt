package me.jsinco.solutilities

import org.bukkit.command.CommandSender

interface SubCommand {

    /**
     * Executed code for the subcommand
     * @param plugin The plugin instance
     * @param sender The command sender
     * @param args The command arguments
     * @return true if the command was executed successfully, false otherwise
     */
    fun execute(plugin: SolUtilities, sender: CommandSender, args: Array<out String>)

    /**
     * Tab completion for the subcommand
     * @param plugin The plugin instance
     * @param sender The command sender
     * @param args The command arguments
     * @return A list of tab completions
     */
    fun tabComplete(plugin: SolUtilities, sender: CommandSender, args: Array<out String>): MutableList<String>?

    /**
     * The permission required to execute the subcommand
     * @return The permission required to execute the subcommand, if not null
     */
    fun permission(): String?

    /**
     * Whether the subcommand can only be executed by a player
     * @return true if the subcommand can only be executed by a player, false otherwise
     */
    fun playerOnly(): Boolean
}