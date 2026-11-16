package com.ninno.app

/** Stockage du jeton de session parent (localStorage sur le web). */
expect object TokenStorage {
    fun load(): String?
    fun save(token: String)
    fun clear()
}
