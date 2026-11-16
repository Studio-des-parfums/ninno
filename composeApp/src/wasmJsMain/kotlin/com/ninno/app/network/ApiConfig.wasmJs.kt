package com.ninno.app.network

import kotlinx.browser.window

// Site servi en local (gradle wasmJsBrowserDevelopmentRun) : on parle au backend local.
internal actual fun localApiUrlOverride(): String? =
    if (window.location.hostname in setOf("localhost", "127.0.0.1")) "http://localhost:8000" else null
