package com.ninno.app

import kotlinx.browser.localStorage

private const val KEY = "ninno_language"

actual object LanguageStorage {
    actual fun load(): String? = runCatching { localStorage.getItem(KEY) }.getOrNull()
    actual fun save(code: String) { runCatching { localStorage.setItem(KEY, code) } }
}
