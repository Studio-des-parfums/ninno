package com.ninno.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ninno.app.R

@Composable
fun FamilyDayLogo(width: Dp, logoUrl: String? = null, modifier: Modifier = Modifier) {
    if (logoUrl != null) {
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(logoUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Logo",
            contentScale = ContentScale.FillWidth,
            placeholder = painterResource(id = R.drawable.logo_family_day),
            error = painterResource(id = R.drawable.logo_family_day),
            fallback = painterResource(id = R.drawable.logo_family_day),
            modifier = modifier.width(width)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.logo_family_day),
            contentDescription = "Family Day 2026",
            contentScale = ContentScale.FillWidth,
            modifier = modifier.width(width)
        )
    }
}
