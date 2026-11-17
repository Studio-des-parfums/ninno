package com.ninno.app.ui

import androidx.compose.runtime.Composable

/** Pas de lecteur vidéo sur cette plateforme : l'image de fond par défaut est affichée. */
@Composable
actual fun BackgroundVideo(url: String): Boolean = false
