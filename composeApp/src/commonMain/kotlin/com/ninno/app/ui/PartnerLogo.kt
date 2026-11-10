package com.ninno.app.ui

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage

/**
 * Logo partenaire, affiché en haut au centre des écrans (configuré dans sdp-dashboard).
 * N'affiche rien (et ne prend pas de place) si aucun logo n'est configuré.
 */
@Composable
fun PartnerLogo(width: Dp, modifier: Modifier = Modifier) {
    val url = LocalPartnerLogoUrl.current ?: return
    AsyncImage(
        model = url,
        contentDescription = "Logo partenaire",
        contentScale = ContentScale.FillWidth,
        modifier = modifier.width(width)
    )
}
