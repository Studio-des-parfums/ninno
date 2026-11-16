package com.ninno.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.LocalStrings
import org.jetbrains.compose.resources.DrawableResource

private val PanelInk = Color(0xFF3B1E5C)

/**
 * Écran d'explication : Zoé à gauche (bulle au-dessus d'elle), et le panneau « Comment ça marche ? »
 * toujours visible à droite (en haut sur téléphone en portrait).
 */
@Composable
fun CardScreen(
    backgroundRes: DrawableResource,
    messages: List<String>,
    onNextClick: (() -> Unit)?,
    onBackClick: () -> Unit,
    backgroundUrl: String? = null,
    @Suppress("UNUSED_PARAMETER") logoUrl: String? = null
) {
    val deviceSize = rememberDeviceSize()
    val stacked = rememberUseStackedLayout()
    val buttonSize = navButtonSizeFor(deviceSize)
    val screenPadding = screenPaddingFor(deviceSize)
    val (titleSize, textSize) = when (deviceSize) {
        DeviceSize.COMPACT -> 20.sp to 15.sp
        DeviceSize.MEDIUM -> 26.sp to 18.sp
        DeviceSize.EXPANDED -> 32.sp to 21.sp
    }

    ScreenBackground(backgroundRes = backgroundRes, backgroundUrl = backgroundUrl, modifier = Modifier.fillMaxSize()) {
        if (stacked) {
            // Téléphone en portrait : panneau en haut, mascotte en bas
            RulesPanel(
                titleSize = titleSize,
                textSize = textSize,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = screenPadding)
                    .padding(top = MainLogoBottom)
            )
            MascotWithBubbleAbove(
                messages = messages,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = screenPadding, bottom = buttonSize + screenPadding * 2)
            )
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                MascotWithBubbleAbove(
                    messages = messages,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = screenPadding * 2, top = MainLogoBottom, bottom = buttonSize + screenPadding * 2)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = screenPadding * 2, top = MainLogoBottom, bottom = buttonSize + screenPadding),
                    contentAlignment = Alignment.Center
                ) {
                    RulesPanel(titleSize = titleSize, textSize = textSize)
                }
            }
        }

        NavBackButton(
            onClick = onBackClick,
            size = buttonSize,
            modifier = Modifier.align(Alignment.BottomStart).padding(screenPadding)
        )
        if (onNextClick != null) {
            NavForwardButton(
                onClick = onNextClick,
                size = buttonSize,
                modifier = Modifier.align(Alignment.BottomEnd).padding(screenPadding)
            )
        }
    }
}

@Composable
private fun RulesPanel(titleSize: TextUnit, textSize: TextUnit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .widthIn(max = 560.dp)
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(28.dp))
            .border(2.dp, PanelInk.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
            .padding(horizontal = 28.dp, vertical = 24.dp)
    ) {
        val strings = LocalStrings.current
        Text(
            text = strings.rulesTitle,
            color = PanelInk,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold,
        )
        strings.rules.forEachIndexed { index, (lead, rest) ->
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(32.dp).background(PanelInk, CircleShape)
                ) {
                    Text("${index + 1}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = androidx.compose.ui.text.buildAnnotatedString {
                        pushStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, color = PanelInk))
                        append(lead)
                        pop()
                        append(" ")
                        append(rest)
                    },
                    color = Color(0xFF2B1B3D),
                    fontSize = textSize,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}
