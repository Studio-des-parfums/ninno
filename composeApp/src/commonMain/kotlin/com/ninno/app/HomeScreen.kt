package com.ninno.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home
import com.ninno.app.ui.DeviceSize
import com.ninno.app.ui.MascotWithBubble
import com.ninno.app.ui.NavForwardButton
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberIsLandscape
import com.ninno.app.ui.rememberUseStackedLayout
import com.ninno.app.ui.screenPaddingFor

// Couleurs du titre : foncées avec un halo blanc pour rester lisibles sur un fond clair
private val TitleColor = Color(0xFF3B1E5C)
private val SubtitleColor = Color(0xFF00897B)
private val TitleGlow = Shadow(color = Color.White, offset = Offset(0f, 0f), blurRadius = 16f)

@Composable
fun HomeScreen(onNextClick: () -> Unit, backgroundUrl: String? = null, isLoading: Boolean = false, versionName: String = "") {
    val isLandscape = rememberIsLandscape()
    val useStackedLayout = rememberUseStackedLayout()
    val deviceSize = rememberDeviceSize()
    val screenPadding = screenPaddingFor(deviceSize)

    val titleSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 32.sp else 44.sp
        DeviceSize.MEDIUM -> 56.sp
        DeviceSize.EXPANDED -> 70.sp
    }
    val withSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 14.sp else 16.sp
        DeviceSize.MEDIUM -> 18.sp
        DeviceSize.EXPANDED -> 22.sp
    }
    val subtitleSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 24.sp else 32.sp
        DeviceSize.MEDIUM -> 40.sp
        DeviceSize.EXPANDED -> 50.sp
    }
    val buttonSize = navButtonSizeFor(deviceSize)
    val topPadding = when {
        useStackedLayout -> 32.dp
        isLandscape -> 16.dp
        else -> 48.dp
    }

    // Un seul ScreenBackground pour les deux états : le fond n'est pas rechargé (ni interrompu)
    // quand on passe du chargement à l'écran normal.
    ScreenBackground(backgroundRes = Res.drawable.bg_home, backgroundUrl = if (isLoading) null else backgroundUrl) {
    if (isLoading) {
        // Loading screen while backend content is being fetched
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "Crée ton parfum",
                        color = TitleColor,
                        fontSize = titleSize,
                        style = TextStyle(shadow = TitleGlow),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "avec",
                        color = TitleColor,
                        fontSize = withSize,
                        style = TextStyle(shadow = TitleGlow),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Ninno, le robot",
                        color = SubtitleColor,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = TitleGlow
                        )
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color.White,
                        strokeWidth = 4.dp
                    )
                }
            }
    } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = screenPadding)
                    .padding(top = topPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crée ton parfum",
                        color = TitleColor,
                        fontSize = titleSize,
                        style = TextStyle(shadow = TitleGlow),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "avec",
                        color = TitleColor,
                        fontSize = withSize,
                        style = TextStyle(shadow = TitleGlow),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Ninno, le robot",
                        color = SubtitleColor,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = TitleGlow
                        )
                    )
                }
            }

            MascotWithBubble(
                messages = listOf(
                    "Coucou ! Moi, c'est Zoé la taupe.",
                    "Bienvenue au Studio des Parfums !",
                    "Es-tu prêt à commencer ?"
                ),
                modifier = Modifier.fillMaxSize()
            )

            NavForwardButton(
                onClick = onNextClick,
                size = buttonSize,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(screenPadding)
            )

            Text(
                text = "v$versionName",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(screenPadding)
            )
        }
    }
}
