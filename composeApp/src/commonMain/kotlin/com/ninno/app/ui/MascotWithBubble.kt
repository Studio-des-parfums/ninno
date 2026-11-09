package com.ninno.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.resources.Res
import com.ninno.app.resources.mascot
import org.jetbrains.compose.resources.painterResource

/** Petite pointe de la bulle, orientée vers la mascotte (à gauche). */
private val BubbleTailShape = GenericShape { size, _ ->
    moveTo(size.width, 0f)
    lineTo(0f, size.height / 2f)
    lineTo(size.width, size.height)
    close()
}

/** Pointe de la bulle orientée vers le bas (mascotte sous la bulle). */
private val BubbleTailDownShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width / 2f, size.height)
    close()
}

/**
 * Effet machine à écrire : tous les messages s'accumulent dans la même bulle,
 * une ligne par message, avec une courte pause entre chaque ligne.
 */
@Composable
private fun rememberTypewriterText(
    messages: List<String>,
    charDelayMs: Long,
    pauseBetweenMessagesMs: Long,
    startDelayMs: Long = 1500, // laisse le fond s'afficher avant de démarrer l'animation
): String {
    var displayed by remember(messages) { mutableStateOf("") }
    LaunchedEffect(messages) {
        delay(startDelayMs)
        var text = ""
        messages.forEachIndexed { index, msg ->
            if (index > 0) {
                delay(pauseBetweenMessagesMs)
                text += "\n"
            }
            for (c in msg) {
                text += c
                displayed = text
                delay(charDelayMs)
            }
        }
    }
    return displayed
}

/**
 * Mascotte posée en bas à gauche de l'écran, avec une bulle de dialogue à sa droite.
 * À placer dans un Box plein écran (au-dessus du background).
 */
@Composable
fun MascotWithBubble(
    messages: List<String>,
    modifier: Modifier = Modifier,
    charDelayMs: Long = 40,
    pauseBetweenMessagesMs: Long = 700,
) {
    val displayed = rememberTypewriterText(messages, charDelayMs, pauseBetweenMessagesMs)

    BoxWithConstraints(modifier = modifier) {
        // La mascotte occupe ~60 % de la hauteur de l'écran
        val mascotHeight = maxHeight * 0.6f
        val bubbleFont = if (maxWidth > 1000.dp) 22.sp else 16.sp

        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalAlignment = Alignment.Top,
        ) {
            Image(
                painter = painterResource(Res.drawable.mascot),
                contentDescription = "Mascotte",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(mascotHeight),
            )

            // Bulle de dialogue, alignée vers la tête de la mascotte
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(x = (-mascotHeight * 0.12f), y = mascotHeight * 0.05f),
            ) {
                if (displayed.isNotEmpty()) Box(
                    Modifier
                        .size(width = 18.dp, height = 24.dp)
                        .background(Color.White, BubbleTailShape)
                )
                if (displayed.isNotEmpty()) Box(
                    modifier = Modifier
                        .widthIn(max = 380.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                ) {
                    Text(
                        text = displayed,
                        color = Color(0xFF2B1B3D),
                        fontSize = bubbleFont,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

/**
 * Mascotte centrée à l'écran, avec la bulle de dialogue au-dessus d'elle.
 * Utilisé sur les écrans d'explication entre les étapes.
 */
@Composable
fun CenteredMascotWithBubble(
    messages: List<String>,
    modifier: Modifier = Modifier,
    charDelayMs: Long = 30,
    pauseBetweenMessagesMs: Long = 600,
) {
    val displayed = rememberTypewriterText(messages, charDelayMs, pauseBetweenMessagesMs, startDelayMs = 400)
    val bubbleColor = if (displayed.isEmpty()) Color.Transparent else Color.White

    BoxWithConstraints(modifier = modifier) {
        val mascotHeight = maxHeight * 0.45f
        val bubbleFont = if (maxWidth > 1000.dp) 24.sp else 18.sp

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 620.dp)
                    .shadow(if (displayed.isEmpty()) 0.dp else 8.dp, RoundedCornerShape(28.dp))
                    .background(bubbleColor, RoundedCornerShape(28.dp))
                    .padding(horizontal = 32.dp, vertical = 22.dp),
            ) {
                Text(
                    text = displayed,
                    color = Color(0xFF2B1B3D),
                    fontSize = bubbleFont,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
            Box(
                Modifier
                    .size(width = 28.dp, height = 18.dp)
                    .background(bubbleColor, BubbleTailDownShape)
            )
            Image(
                painter = painterResource(Res.drawable.mascot),
                contentDescription = "Zoé la taupe",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(mascotHeight),
            )
        }
    }
}
