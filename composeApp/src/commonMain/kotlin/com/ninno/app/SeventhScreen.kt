package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home2
import com.ninno.app.ui.CardScreen

@Composable
fun SeventhScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = Res.drawable.bg_home2,
        messages = listOf(
            "Enfin, la note de fond : la puissance.",
            "Sens, et choisis les 2 meilleures puis celle que tu n'aimes pas.",
            "C'est la ligne du bas."
        ),
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
