package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.ui.CardScreen

@Composable
fun FifthScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = R.drawable.bg_home2,
        text = "Ensuite la note de cœur :\nle caractère. Sens, et choisi les 2 meilleurs puis  celle que tu n'aimes pas. C'est la ligne du milieu.",
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
