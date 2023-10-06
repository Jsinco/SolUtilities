package me.jsinco.solutilities.hooks

import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit

object LuckPermsHook {
    @JvmStatic val luckPermsInstance: LuckPerms? = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider
}