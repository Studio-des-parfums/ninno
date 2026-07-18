package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.ui.CardScreen

@Composable
fun SeventhScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = R.drawable.bg_home2,
        text = "Enfin, la note de fond : la puissance. Sens, et choisi les 2 meilleures puis  celle que tu n'aimes pas. C'est la ligne du bas.",
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
