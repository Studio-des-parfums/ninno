package com.ninno.app

import android.media.AudioAttributes
import android.media.SoundPool

actual class SortSoundPlayer actual constructor() {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val context get() = AndroidAppContext.instance
    private val happySoundId = soundPool.load(context, R.raw.sound_happy, 1)
    private val sadSoundId = soundPool.load(context, R.raw.sound_sad, 1)

    actual fun playHappy() {
        soundPool.play(happySoundId, 1f, 1f, 1, 0, 1f)
    }

    actual fun playSad() {
        soundPool.play(sadSoundId, 1f, 1f, 1, 0, 1f)
    }

    actual fun release() {
        soundPool.release()
    }
}
