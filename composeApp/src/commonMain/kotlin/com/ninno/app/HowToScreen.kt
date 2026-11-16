package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home2
import com.ninno.app.ui.CardScreen

/** Juste après l'accueil : Zoé annonce les règles, affichées dans le panneau à droite. */
@Composable
fun HowToScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = Res.drawable.bg_home2,
        messages = listOf(LocalStrings.current.howToIntro),
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
