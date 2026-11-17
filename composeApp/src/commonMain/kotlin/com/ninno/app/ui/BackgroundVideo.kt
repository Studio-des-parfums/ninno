package com.ninno.app.ui

import androidx.compose.runtime.Composable

/** Extensions de fond reconnues comme vidéo (lues en boucle, sans son). */
private val VIDEO_EXTENSIONS = listOf(".mp4", ".webm")

/** Vrai si l'URL de fond configurée dans le backoffice pointe vers une vidéo. */
fun isVideoUrl(url: String?): Boolean {
    val path = url?.substringBefore('?')?.substringBefore('#')?.lowercase() ?: return false
    return VIDEO_EXTENSIONS.any { path.endsWith(it) }
}

/**
 * Vidéo de fond plein écran, en boucle et muette.
 * Renvoie false si la plateforme ne sait pas l'afficher : l'appelant affiche alors l'image par défaut.
 */
@Composable
expect fun BackgroundVideo(url: String): Boolean
