package com.ninno.app

// Pas de persistance hors web pour l'instant : la session dure le temps de l'exécution.
actual object TokenStorage {
    private var token: String? = null
    actual fun load(): String? = token
    actual fun save(token: String) { this.token = token }
    actual fun clear() { token = null }
}
