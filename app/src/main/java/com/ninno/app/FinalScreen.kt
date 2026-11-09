package com.ninno.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.ui.DeviceSize
import com.ninno.app.ui.FamilyDayLogo
import com.ninno.app.ui.NavHomeButton
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.logoWidthFor
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberIsLandscape
import com.ninno.app.ui.rememberUseStackedLayout
import com.ninno.app.ui.screenPaddingFor

@Composable
fun FinalScreen(
    formulaViewModel: PerfumeFormulaViewModel,
    onHomeClick: () -> Unit,
    backgroundUrl: String? = null,
    logoUrl: String? = null
) {
    val uiState by formulaViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState is FormulaUiState.Idle) {
            formulaViewModel.generateFormulaDescription()
        }
    }

    val deviceSize = rememberDeviceSize()
    val isLandscape = rememberIsLandscape()
    val useStackedLayout = rememberUseStackedLayout()
    val buttonSize = navButtonSizeFor(deviceSize)
    val screenPadding = screenPaddingFor(deviceSize)
    val logoWidth = logoWidthFor(deviceSize, isLandscape)

    val cardMaxWidth = when (deviceSize) {
        DeviceSize.COMPACT -> 360.dp
        DeviceSize.MEDIUM -> 760.dp
        DeviceSize.EXPANDED -> 900.dp
    }
    val textSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 16.sp else 20.sp
        DeviceSize.MEDIUM -> 24.sp
        DeviceSize.EXPANDED -> 28.sp
    }
    val bravoSize = when (deviceSize) {
        DeviceSize.COMPACT -> if (useStackedLayout) 24.sp else 30.sp
        DeviceSize.MEDIUM -> 36.sp
        DeviceSize.EXPANDED -> 44.sp
    }
    val cardPadding = when (deviceSize) {
        DeviceSize.COMPACT -> 20.dp
        DeviceSize.MEDIUM -> 32.dp
        DeviceSize.EXPANDED -> 40.dp
    }

    ScreenBackground(backgroundRes = R.drawable.bg_home2, backgroundUrl = backgroundUrl, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FamilyDayLogo(
                width = logoWidth,
                logoUrl = logoUrl,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
            Column(
                modifier = Modifier
                    .widthIn(max = cardMaxWidth)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .padding(cardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BRAVO !",
                    color = Color(0xFFFF4FA0),
                    fontSize = bravoSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = cardPadding)
                )

                when (val state = uiState) {
                    FormulaUiState.Loading, FormulaUiState.Idle -> {
                        CircularProgressIndicator(color = Color(0xFFFF4FA0))
                        Text(
                            text = "Création de ton parfum...",
                            color = Color(0xFF2B2B2B),
                            fontSize = textSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = cardPadding)
                        )
                    }
                    is FormulaUiState.Success -> {
                        if (useStackedLayout) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(cardPadding)
                            ) {
                                val notesSummary = listOf(
                                    "Tête : " + state.formula.topNotes.joinToString(", "),
                                    "Cœur : " + state.formula.heartNotes.joinToString(", "),
                                    "Fond : " + state.formula.baseNotes.joinToString(", ")
                                ).joinToString("\n")
                                Text(
                                    text = notesSummary,
                                    color = Color(0xFF2B2B2B),
                                    fontSize = (textSize.value * 0.85f).sp,
                                    fontFamily = FontFamily.SansSerif,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = state.formula.description,
                                    color = Color(0xFF2B2B2B),
                                    fontSize = (textSize.value * 0.85f).sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(cardPadding)
                            ) {
                                val notesSummary = listOf(
                                    "Tête : " + state.formula.topNotes.joinToString(", "),
                                    "Cœur : " + state.formula.heartNotes.joinToString(", "),
                                    "Fond : " + state.formula.baseNotes.joinToString(", ")
                                ).joinToString("\n")
                                Text(
                                    text = notesSummary,
                                    color = Color(0xFF2B2B2B),
                                    fontSize = (textSize.value * 0.7f).sp,
                                    fontFamily = FontFamily.SansSerif,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = state.formula.description,
                                    color = Color(0xFF2B2B2B),
                                    fontSize = (textSize.value * 0.7f).sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    is FormulaUiState.Error -> {
                        Text(
                            text = state.message,
                            color = Color(0xFF2B2B2B),
                            fontSize = textSize,
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

        NavHomeButton(
            onClick = onHomeClick,
            size = buttonSize,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(screenPadding)
        )
    }
}
