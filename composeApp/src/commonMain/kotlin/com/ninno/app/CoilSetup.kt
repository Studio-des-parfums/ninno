package com.ninno.app

import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.ImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.ninno.app.network.NinnoHttpClient

/** Enregistre un ImageLoader singleton avec le moteur réseau Ktor, requis sur toutes les plateformes KMP. */
object CoilSetup {
    private var initialized = false

    @OptIn(DelicateCoilApi::class)
    fun ensureInitialized() {
        if (initialized) return
        initialized = true
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(PlatformContext.INSTANCE)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = { NinnoHttpClient.client }))
                }
                .build()
        )
    }
}
