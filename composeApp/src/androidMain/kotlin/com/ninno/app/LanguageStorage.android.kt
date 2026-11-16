package com.ninno.app

// Pas de persistance hors web pour l'instant.
actual object LanguageStorage {
    private var code: String? = null
    actual fun load(): String? = code
    actual fun save(code: String) { this.code = code }
}
