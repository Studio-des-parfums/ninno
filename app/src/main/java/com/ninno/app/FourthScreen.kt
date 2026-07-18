package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.ui.CardScreen

@Composable
fun FourthScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = R.drawable.bg_home2,
        text = "On va commencer par\nla note de tête : c'est\nla plus légère. Sens, et\n\nchoisi la meilleure puis\n\ncelle que tu n'aimes pas.\n\nC'est la ligne du haut.",
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
