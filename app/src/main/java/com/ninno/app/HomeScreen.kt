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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.ui.DeviceSize
import com.ninno.app.ui.NavForwardButton
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberUseStackedLayout
import com.ninno.app.ui.screenPaddingFor

@Composable
fun HomeScreen(onNextClick: () -> Unit, backgroundUrl: String? = null, isLoading: Boolean = false, versionName: String = "") {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
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

    if (isLoading) {
        // Loading screen while backend content is being fetched
        ScreenBackground(backgroundRes = R.drawable.bg_home, backgroundUrl = null) {
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
                        color = Color.White,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "avec",
                        color = Color.White,
                        fontSize = withSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Inno, le robot",
                        color = Color.White,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFFFF4FA0),
                                offset = Offset(0f, 4f),
                                blurRadius = 12f
                            )
                        )
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color.White,
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    } else {
        ScreenBackground(backgroundRes = R.drawable.bg_home, backgroundUrl = backgroundUrl) {
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
                        color = Color.White,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "avec",
                        color = Color.White,
                        fontSize = withSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Inno, le robot",
                        color = Color.White,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFFFF4FA0),
                                offset = Offset(0f, 4f),
                                blurRadius = 12f
                            )
                        )
                    )
                }
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
                    .align(Alignment.BottomStart)
                    .padding(screenPadding)
            )
        }
    }
}
