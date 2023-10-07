package me.jsinco.solutilities.blackmarket

import me.jsinco.oneannouncer.DiscordSRVUtil
import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import me.jsinco.solutilities.Saves
import me.jsinco.solutilities.SolUtilities
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.plugin.Plugin
import java.util.*

class BlackMarketNotifyCommand : DiscordCommand {

    override fun name(): String {
        return "blackmarket-notify"
    }

    override fun description(): String {
        return "Get notified when the Blackmarket has been restocked"
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val mcUUID: UUID? = DiscordSRVUtil.getUUIDFromDiscordID(event.user.id)
        if (mcUUID == null) {
            event.reply("**Market** » Your discord and minecraft accounts are not linked. Use `/discord link` in game to get started").setEphemeral(true).queue()
            return
        }

        if (Saves.get().get("Blackmarket.NotifyDiscord.$mcUUID") == null) {
            Saves.get().set("Blackmarket.NotifyDiscord.$mcUUID", true)
            event.reply("**Market** » You will now receive Blackmarket discord notifications").setEphemeral(true).queue()
        } else {
            Saves.get().set("Blackmarket.NotifyDiscord.$mcUUID", null)
            event.reply("**Market** » You will no longer receive Blackmarket discord notifications").setEphemeral(true).queue()
        }
        Saves.save()
    }

    override fun options(): List<CommandOption>? {
        return null
    }

    override fun permission(): Permission? {
        return null
    }

    override fun plugin(): Plugin? {
        return SolUtilities.getPlugin()
    }
}