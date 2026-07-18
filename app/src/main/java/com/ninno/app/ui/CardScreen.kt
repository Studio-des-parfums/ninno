package com.ninno.app.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardScreen(
    @DrawableRes backgroundRes: Int,
    text: String,
    onNextClick: (() -> Unit)?,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    val deviceSize = rememberDeviceSize()
    val isLandscape = rememberIsLandscape()
    val buttonSize = navButtonSizeFor(deviceSize)
    val screenPadding = screenPaddingFor(deviceSize)

    val cardMaxWidth = when (deviceSize) {
        DeviceSize.COMPACT -> 360.dp
        DeviceSize.MEDIUM -> 560.dp
        DeviceSize.EXPANDED -> 680.dp
    }
    val cardTextSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (!isLandscape) 18.sp else 20.sp
        DeviceSize.MEDIUM -> 24.sp
        DeviceSize.EXPANDED -> 28.sp
    }
    val cardPadding = when (deviceSize) {
        DeviceSize.COMPACT -> 20.dp
        DeviceSize.MEDIUM -> 32.dp
        DeviceSize.EXPANDED -> 40.dp
    }
    val logoWidth = logoWidthFor(deviceSize, isLandscape)

    ScreenBackground(backgroundRes = backgroundRes, backgroundUrl = backgroundUrl, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavBackButton(
                    onClick = onBackClick,
                    size = buttonSize
                )

                FamilyDayLogo(width = logoWidth, logoUrl = logoUrl)

                if (onNextClick != null) {
                    NavForwardButton(
                        onClick = onNextClick,
                        size = buttonSize
                    )
                } else {
                    Spacer(modifier = Modifier.size(buttonSize))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = cardMaxWidth)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White.copy(alpha = 0.92f))
                        .padding(cardPadding)
                ) {
                    Text(
                        text = text,
                        color = Color(0xFF2B2B2B),
                        fontSize = cardTextSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
