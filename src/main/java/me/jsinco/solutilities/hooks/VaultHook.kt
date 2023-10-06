package me.jsinco.solutilities.hooks

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit

object VaultHook {
    @JvmStatic val economy: Economy = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)!!.provider
}