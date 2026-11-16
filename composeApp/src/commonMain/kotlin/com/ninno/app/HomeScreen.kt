package com.ninno.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home
import com.ninno.app.ui.DeviceSize
import com.ninno.app.ui.MascotWithBubble
import com.ninno.app.ui.NavBackButton
import com.ninno.app.ui.NavForwardButton
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberIsLandscape
import com.ninno.app.ui.rememberUseStackedLayout
import com.ninno.app.ui.screenPaddingFor

// Titre posé sur un bandeau blanc translucide cerclé de violet : lisible quel que soit le fond configuré
private val TitleColor = Color(0xFF3B1E5C)
private val SubtitleColor = Color(0xFFD32F2F)
private val BannerBorder = Color(0xFF3B1E5C)

@Composable
private fun TitleBanner(titleSize: TextUnit, withSize: TextUnit, subtitleSize: TextUnit) {
    val strings = LocalStrings.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.88f))
            .border(2.dp, BannerBorder.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        Text(
            text = strings.homeTitle,
            color = TitleColor,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
        Text(
            text = strings.homeWith,
            color = TitleColor,
            fontSize = withSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
        Text(
            text = strings.zoeName,
            color = SubtitleColor,
            fontSize = subtitleSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HomeScreen(
    onNextClick: () -> Unit,
    backgroundUrl: String? = null,
    isLoading: Boolean = false,
    versionName: String = "",
    childName: String? = null,
    onChangeChild: (() -> Unit)? = null,
) {
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
                    TitleBanner(titleSize = titleSize, withSize = withSize, subtitleSize = subtitleSize)
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
                    TitleBanner(titleSize = titleSize, withSize = withSize, subtitleSize = subtitleSize)
                }
            }

            val strings = LocalStrings.current
            MascotWithBubble(
                messages = listOf(strings.greeting(childName), strings.welcome, strings.readyToStart),
                // Plus petite qu'ailleurs : laisse respirer le titre d'accueil
                heightFraction = 0.45f,
                modifier = Modifier.fillMaxSize()
            )

            // Retour à « Qui joue ? » pour changer d'enfant
            if (onChangeChild != null) {
                NavBackButton(
                    onClick = onChangeChild,
                    size = buttonSize,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(screenPadding)
                )
            }

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
                    .align(Alignment.BottomCenter)
                    .padding(screenPadding)
            )
        }
    }
}
