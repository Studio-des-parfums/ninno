package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.ui.CardScreen

@Composable
fun ThirdScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = R.drawable.bg_home2,
        text = "Un parfum est composé de :\n\n\nNote de tête\n\nNote de cœur\n\nNote de fond",
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
