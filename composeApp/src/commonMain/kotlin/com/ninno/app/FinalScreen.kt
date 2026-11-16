package com.ninno.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home2
import com.ninno.app.ui.PartnerLogo
import com.ninno.app.ui.MascotWithBubble
import com.ninno.app.ui.NavHomeButton
import com.ninno.app.ui.ScreenBackground
import com.ninno.app.ui.logoWidthFor
import com.ninno.app.ui.navButtonSizeFor
import com.ninno.app.ui.rememberDeviceSize
import com.ninno.app.ui.rememberIsLandscape
import com.ninno.app.ui.screenPaddingFor

private val TextDark = Color(0xFF2B1B3D)
private val TopColor = Color(0xFFFFB300)   // tête : léger, lumineux
private val HeartColor = Color(0xFFE91E63) // cœur : caractère
private val BaseColor = Color(0xFF6D4C41)  // fond : puissance

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
    val buttonSize = navButtonSizeFor(deviceSize)
    val screenPadding = screenPaddingFor(deviceSize)
    val logoWidth = logoWidthFor(deviceSize, isLandscape)

    // Ce que dit Zoé, selon l'état de la génération
    val state = uiState
    val strings = LocalStrings.current
    val mascotMessages = remember(state, strings) {
        when (state) {
            FormulaUiState.Idle, FormulaUiState.Loading ->
                listOf(strings.bravo, strings.preparing)
            is FormulaUiState.Success ->
                listOf(strings.bravo, strings.hereIsYourPerfume, state.formula.description)
            is FormulaUiState.Error ->
                listOf(strings.oops, strings.error(state.reason))
        }
    }

    ScreenBackground(backgroundRes = Res.drawable.bg_home2, backgroundUrl = backgroundUrl, modifier = Modifier.fillMaxSize()) {
        PartnerLogo(
            width = logoWidth,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenPadding)
        )

        // Zoé en bas à gauche, qui annonce le résultat
        MascotWithBubble(
            messages = mascotMessages,
            modifier = Modifier.fillMaxSize()
        )

        // Section dédiée aux notes de la formule, à droite
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding)
        ) {
            val cardWidth = (maxWidth * 0.4f).coerceIn(300.dp, 520.dp)
            FormulaNotesCard(
                state = state,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(cardWidth)
            )
        }

        NavHomeButton(
            onClick = onHomeClick,
            size = buttonSize,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(screenPadding)
        )
    }
}

@Composable
private fun FormulaNotesCard(state: FormulaUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(28.dp))
            .background(Color.White, RoundedCornerShape(28.dp))
            .border(3.dp, Color.White, RoundedCornerShape(28.dp))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val strings = LocalStrings.current
        Text(
            text = strings.yourFormula,
            color = TextDark,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        when (state) {
            FormulaUiState.Idle, FormulaUiState.Loading -> {
                CircularProgressIndicator(color = HeartColor, modifier = Modifier.padding(24.dp))
            }
            is FormulaUiState.Success -> {
                val formula = state.formula
                Text(
                    text = strings.forBottle(formula.volumeMl),
                    color = TextDark.copy(alpha = 0.75f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                NoteGroup(strings.topNotes, formula.notesOf(NoteCategory.TOP), TopColor)
                NoteGroup(strings.heartNotes, formula.notesOf(NoteCategory.HEART), HeartColor)
                NoteGroup(strings.baseNotes, formula.notesOf(NoteCategory.BASE), BaseColor)
            }
            is FormulaUiState.Error -> {
                Text(
                    text = strings.formulaFailed,
                    color = TextDark.copy(alpha = 0.7f),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/** 4.5 -> "4,5" ; 5.0 -> "5" */
private fun formatMl(value: Double): String {
    val tenths = kotlin.math.round(value * 10).toLong()
    return if (tenths % 10 == 0L) "${tenths / 10}" else "${tenths / 10},${tenths % 10}"
}

@Composable
private fun NoteGroup(label: String, notes: List<FormulaNote>, color: Color) {
    if (notes.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(18.dp))
            .border(2.dp, color, RoundedCornerShape(18.dp))
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        notes.forEach { note ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = note.name,
                    color = TextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${formatMl(note.ml)} mL",
                    color = TextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
