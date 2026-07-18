package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.ui.CardScreen

@Composable
fun SecondaryScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = R.drawable.bg_home2,
        text = "Un parfum est un mélange d'odeurs, d'alcool, et un peu d'eau.",
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
