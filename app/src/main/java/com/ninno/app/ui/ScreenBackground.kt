package com.ninno.app.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext

enum class DeviceSize { COMPACT, MEDIUM, EXPANDED }

@Composable
fun rememberDeviceSize(): DeviceSize {
    val widthDp = LocalConfiguration.current.screenWidthDp
    return when {
        widthDp < 600 -> DeviceSize.COMPACT   // téléphone
        widthDp < 840 -> DeviceSize.MEDIUM    // tablette petite / téléphone paysage large
        else -> DeviceSize.EXPANDED           // tablette grande / paysage
    }
}

@Composable
fun rememberIsLandscape(): Boolean {
    val config = LocalConfiguration.current
    return config.screenWidthDp > config.screenHeightDp
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

@Composable
fun ScreenBackground(
    @DrawableRes backgroundRes: Int,
    backgroundUrl: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (backgroundUrl != null) {
            val context = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(backgroundUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = backgroundRes),
                error = painterResource(id = backgroundRes),
                fallback = painterResource(id = backgroundRes),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        content()
    }
}
