package com.ninno.app

import com.ninno.app.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

@OptIn(ExperimentalResourceApi::class)
actual class SortSoundPlayer actual constructor() {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var happyUrl: String? = null
    private var sadUrl: String? = null

    init {
        scope.launch {
            happyUrl = loadAsBlobUrl("files/sound_yes.wav")
            sadUrl = loadAsBlobUrl("files/sound_no.wav")
        }
    }

    private suspend fun loadAsBlobUrl(path: String): String? {
        return try {
            val bytes = Res.readBytes(path)
            bytesToBlobUrl(bytes)
        } catch (_: Throwable) {
            null
        }
    }

    actual fun playHappy() {
        happyUrl?.let { playAudioUrl(it) }
    }

    actual fun playSad() {
        sadUrl?.let { playAudioUrl(it) }
    }

    actual fun release() {
        happyUrl?.let { revokeBlobUrl(it) }
        sadUrl?.let { revokeBlobUrl(it) }
    }
}

private fun bytesToBlobUrl(bytes: ByteArray): String {
    val array = Int8Array(bytes.size)
    for (i in bytes.indices) {
        array[i] = bytes[i]
    }
    return createBlobUrl(array)
}

private fun createBlobUrl(bytes: Int8Array): String = js(
    """
    (function(bytes) {
        var blob = new Blob([bytes], { type: "audio/wav" });
        return URL.createObjectURL(blob);
    })(bytes)
    """
)

private fun revokeBlobUrl(url: String): Unit = js(
    """
    (function(url) {
        try { URL.revokeObjectURL(url); } catch (e) {}
    })(url)
    """
)

private fun playAudioUrl(url: String): Unit = js(
    """
    (function(url) {
        try {
            var audio = new Audio(url);
            audio.play().catch(function() {});
        } catch (e) {}
    })(url)
    """
)
