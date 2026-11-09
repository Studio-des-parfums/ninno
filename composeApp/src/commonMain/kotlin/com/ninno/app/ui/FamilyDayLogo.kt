package com.ninno.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.ninno.app.resources.Res
import com.ninno.app.resources.logo_family_day
import org.jetbrains.compose.resources.painterResource

@Composable
fun FamilyDayLogo(width: Dp, logoUrl: String? = null, modifier: Modifier = Modifier) {
    if (logoUrl != null) {
        AsyncImage(
            model = logoUrl,
            contentDescription = "Logo",
            contentScale = ContentScale.FillWidth,
            placeholder = painterResource(Res.drawable.logo_family_day),
            error = painterResource(Res.drawable.logo_family_day),
            fallback = painterResource(Res.drawable.logo_family_day),
            modifier = modifier.width(width)
        )
    } else {
        Image(
            painter = painterResource(Res.drawable.logo_family_day),
            contentDescription = "Family Day 2026",
            contentScale = ContentScale.FillWidth,
            modifier = modifier.width(width)
        )
    }
}
