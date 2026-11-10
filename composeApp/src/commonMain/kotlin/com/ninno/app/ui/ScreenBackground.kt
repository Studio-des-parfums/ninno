package com.ninno.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ninno.app.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class DeviceSize { COMPACT, MEDIUM, EXPANDED }

/** Largeur de la fenêtre en dp, quelle que soit la plateforme (Android, desktop, web). */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberWindowWidthDp(): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return with(density) { windowInfo.containerSize.width.toDp() }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberWindowHeightDp(): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return with(density) { windowInfo.containerSize.height.toDp() }
}

@Composable
fun rememberDeviceSize(): DeviceSize {
    val widthDp = rememberWindowWidthDp().value
    return when {
        widthDp < 600 -> DeviceSize.COMPACT   // téléphone
        widthDp < 840 -> DeviceSize.MEDIUM    // tablette petite / téléphone paysage large
        else -> DeviceSize.EXPANDED           // tablette grande / paysage
    }
}

@Composable
fun rememberIsLandscape(): Boolean {
    return rememberWindowWidthDp() > rememberWindowHeightDp()
}

@Composable
fun rememberUseStackedLayout(): Boolean {
    return rememberDeviceSize() == DeviceSize.COMPACT && !rememberIsLandscape()
}

fun logoWidthFor(deviceSize: DeviceSize, isLandscape: Boolean): Dp = when (deviceSize) {
    DeviceSize.COMPACT -> if (isLandscape) 160.dp else 120.dp
    DeviceSize.MEDIUM -> if (isLandscape) 240.dp else 200.dp
    DeviceSize.EXPANDED -> if (isLandscape) 340.dp else 280.dp
}

fun screenPaddingFor(deviceSize: DeviceSize): Dp = when (deviceSize) {
    DeviceSize.COMPACT -> 16.dp
    DeviceSize.MEDIUM -> 20.dp
    DeviceSize.EXPANDED -> 24.dp
}

/** URL du logo partenaire (configuré dans sdp-dashboard). null = aucun logo affiché. */
val LocalPartnerLogoUrl = staticCompositionLocalOf<String?> { null }

@Composable
fun ScreenBackground(
    backgroundRes: DrawableResource,
    backgroundUrl: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (backgroundUrl != null) {
            AsyncImage(
                model = backgroundUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(backgroundRes),
                error = painterResource(backgroundRes),
                fallback = painterResource(backgroundRes),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        val partnerLogoUrl = LocalPartnerLogoUrl.current
        if (partnerLogoUrl != null) {
            AsyncImage(
                model = partnerLogoUrl,
                contentDescription = "Logo partenaire",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp)
                    .width(130.dp)
            )
        }
        content()
    }
}
