package me.jsinco.solutilities.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion

class PAPIManager {

    private val placeholders: MutableList<PlaceholderExpansion> = mutableListOf()

    fun addPlaceholder(placeholder: PlaceholderExpansion) {
        placeholders.add(placeholder)
    }

    fun removePlaceholder(placeholder: PlaceholderExpansion) {
        placeholders.remove(placeholder)
    }

    fun registerPlaceholders() {
        for (placeholder in placeholders) {
            placeholder.register()
        }
    }

    fun unregisterPlaceholders() {
        for (placeholder in placeholders) {
            placeholder.unregister()
        }
    }
}