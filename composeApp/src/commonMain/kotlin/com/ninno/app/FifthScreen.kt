package com.ninno.app

import androidx.compose.runtime.Composable
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home2
import com.ninno.app.ui.CardScreen

@Composable
fun FifthScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    CardScreen(
        backgroundRes = Res.drawable.bg_home2,
        messages = LocalStrings.current.fifth,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        backgroundUrl = backgroundUrl,
        logoUrl = logoUrl
    )
}
