package com.ninno.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ninno.app.Language

/**
 * Drapeau associé à une langue, dessiné en Compose : le rendu web (Skia) n'a pas de police emoji,
 * les drapeaux 🇫🇷 s'afficheraient en carrés. Dessins simplifiés (pas d'armoiries).
 */
@Composable
fun Flag(language: Language, width: Dp = 28.dp, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(3.dp)
    Canvas(
        modifier
            .size(width, width * 2 / 3)
            .clip(shape)
            .border(0.5.dp, Color.Black.copy(alpha = 0.15f), shape)
    ) {
        when (language) {
            Language.FR -> verticalStripes(listOf(Color(0xFF002395), Color.White, Color(0xFFED2939)))
            Language.EN -> unionJack()
            Language.ES -> horizontalStripes(listOf(Color(0xFFAA151B), Color(0xFFF1BF00), Color(0xFFF1BF00), Color(0xFFAA151B)))
            Language.PT -> {
                drawRect(Color(0xFFFF0000))
                drawRect(Color(0xFF006600), size = Size(size.width * 0.4f, size.height))
                drawCircle(Color(0xFFFFCC00), radius = size.height * 0.2f, center = Offset(size.width * 0.4f, size.height / 2))
            }
            Language.DE -> horizontalStripes(listOf(Color.Black, Color(0xFFDD0000), Color(0xFFFFCE00)))
            // Arabe : drapeau des Émirats arabes unis (lisible en petit, sans inscription)
            Language.AR -> {
                horizontalStripes(listOf(Color(0xFF00732F), Color.White, Color.Black))
                drawRect(Color(0xFFFF0000), size = Size(size.width * 0.25f, size.height))
            }
            Language.RU -> horizontalStripes(listOf(Color.White, Color(0xFF0039A6), Color(0xFFD52B1E)))
        }
    }
}

private fun DrawScope.verticalStripes(colors: List<Color>) {
    val w = size.width / colors.size
    colors.forEachIndexed { i, c -> drawRect(c, topLeft = Offset(i * w, 0f), size = Size(w + 0.5f, size.height)) }
}

private fun DrawScope.horizontalStripes(colors: List<Color>) {
    val h = size.height / colors.size
    colors.forEachIndexed { i, c -> drawRect(c, topLeft = Offset(0f, i * h), size = Size(size.width, h + 0.5f)) }
}

private fun DrawScope.unionJack() {
    val blue = Color(0xFF012169)
    val red = Color(0xFFC8102E)
    val w = size.width
    val h = size.height
    drawRect(blue)
    // Diagonales blanches puis rouges
    drawLine(Color.White, Offset(0f, 0f), Offset(w, h), strokeWidth = h * 0.2f)
    drawLine(Color.White, Offset(w, 0f), Offset(0f, h), strokeWidth = h * 0.2f)
    drawLine(red, Offset(0f, 0f), Offset(w, h), strokeWidth = h * 0.07f)
    drawLine(red, Offset(w, 0f), Offset(0f, h), strokeWidth = h * 0.07f)
    // Croix blanche puis rouge
    drawRect(Color.White, topLeft = Offset(w / 2 - h * 0.17f, 0f), size = Size(h * 0.34f, h))
    drawRect(Color.White, topLeft = Offset(0f, h / 2 - h * 0.17f), size = Size(w, h * 0.34f))
    drawRect(red, topLeft = Offset(w / 2 - h * 0.1f, 0f), size = Size(h * 0.2f, h))
    drawRect(red, topLeft = Offset(0f, h / 2 - h * 0.1f), size = Size(w, h * 0.2f))
}
