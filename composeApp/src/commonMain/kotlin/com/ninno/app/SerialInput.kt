package com.ninno.app

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/** Longueur d'un numéro de série : 9 chiffres, affichés 123-456-789. */
const val SERIAL_DIGITS = 9

/** Ne garde que les chiffres saisis, 9 au maximum (le collage de « 123-456-789 » fonctionne). */
fun sanitizeSerialInput(input: String): String = input.filter { it.isDigit() }.take(SERIAL_DIGITS)

/** Affiche les chiffres bruts avec les tirets ajoutés automatiquement : 123456789 -> 123-456-789. */
object SerialVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = raw.chunked(3).joinToString("-")
        val mapping = object : OffsetMapping {
            // Un tiret est inséré après le 3e et le 6e chiffre (seulement si la suite existe).
            override fun originalToTransformed(offset: Int): Int = offset + dashesBefore(offset, raw.length)
            override fun transformedToOriginal(offset: Int): Int {
                var original = 0
                while (original < raw.length && originalToTransformed(original + 1) <= offset) original++
                return original
            }
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }

    private fun dashesBefore(offset: Int, length: Int): Int =
        listOf(3, 6).count { offset > it || (offset == it && length > it) }
}
