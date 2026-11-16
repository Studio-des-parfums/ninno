package com.ninno.app

import kotlinx.browser.localStorage

private const val KEY = "ninno_parent_token"

actual object TokenStorage {
    actual fun load(): String? = runCatching { localStorage.getItem(KEY) }.getOrNull()
    actual fun save(token: String) { runCatching { localStorage.setItem(KEY, token) } }
    actual fun clear() { runCatching { localStorage.removeItem(KEY) } }
}
