package com.ninno.app

import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioSystem

actual class SortSoundPlayer actual constructor() {

    actual fun playHappy() {
        playResource("files/sound_yes.wav")
    }

    actual fun playSad() {
        playResource("files/sound_no.wav")
    }

    actual fun release() {
        // Rien à libérer explicitement : chaque lecture ouvre son propre Clip.
    }

    private fun playResource(path: String) {
        try {
            val stream = javaClass.classLoader.getResourceAsStream("composeResources/com.ninno.app.resources/$path")
                ?: return
            val bytes = stream.use { it.readBytes() }
            val audioStream = AudioSystem.getAudioInputStream(ByteArrayInputStream(bytes))
            val clip = AudioSystem.getClip()
            clip.open(audioStream)
            clip.start()
        } catch (_: Exception) {
            // Ignore les erreurs de lecture audio.
        }
    }
}
