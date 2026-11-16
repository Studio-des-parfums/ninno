package com.ninno.app

/** Langue choisie pour Zoé (localStorage sur le web). */
expect object LanguageStorage {
    fun load(): String?
    fun save(code: String)
}
