package com.ninno.app

/** Petit lecteur de sons courts (happy/sad) utilisé pendant le tri des notes. */
expect class SortSoundPlayer() {
    fun playHappy()
    fun playSad()
    fun release()
}
