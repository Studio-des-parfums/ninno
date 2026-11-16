package com.ninno.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Genre de l'enfant, tel qu'échangé avec le backend ("girl" / "boy"). */
enum class ChildGender(val apiValue: String, val label: String, val color: Color, val icon: ImageVector) {
    GIRL("girl", "Fille", Color(0xFFEC4899), Icons.Filled.Female),
    BOY("boy", "Garçon", Color(0xFF3B82F6), Icons.Filled.Male);

    companion object {
        fun fromApi(value: String?): ChildGender? = entries.firstOrNull { it.apiValue == value }
    }
}

const val MIN_CHILD_AGE = 3
const val MAX_CHILD_AGE = 17

/** Genre de l'enfant qui joue, pour accorder les messages de Zoé (null : enfant inscrit avant). */
val LocalChildGender = compositionLocalOf<ChildGender?> { null }

/**
 * Accorde un texte au genre de l'enfant : `agree("prêt", "prête")`.
 * Genre inconnu (enfant inscrit avant l'ajout du genre) : forme épicène « prêt·e ».
 */
fun ChildGender?.agree(masculine: String, feminine: String): String = when (this) {
    ChildGender.BOY -> masculine
    ChildGender.GIRL -> feminine
    null -> if (feminine.startsWith(masculine)) "$masculine·${feminine.removePrefix(masculine)}" else "$masculine / $feminine"
}

/** Choix Fille / Garçon : rose ou bleu, avec icône. */
@Composable
fun GenderPicker(selected: ChildGender?, onSelect: (ChildGender) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ChildGender.entries.forEach { gender ->
            val active = gender == selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (active) gender.color else gender.color.copy(alpha = 0.1f))
                    .border(2.dp, gender.color, RoundedCornerShape(16.dp))
                    .clickable { onSelect(gender) }
                    .padding(vertical = 12.dp)
            ) {
                Icon(gender.icon, contentDescription = null, tint = if (active) Color.White else gender.color, modifier = Modifier.size(32.dp))
                Text(gender.label, color = if (active) Color.White else gender.color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** Choix de l'âge : boutons − / + (3 à 17 ans). */
@Composable
fun AgePicker(age: Int?, color: Color, onChange: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Âge", color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp))
        StepButton("−", color, enabled = age != null && age > MIN_CHILD_AGE) { onChange((age ?: MIN_CHILD_AGE) - 1) }
        Text(
            if (age == null) "?" else "$age ans",
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        StepButton("+", color, enabled = age == null || age < MAX_CHILD_AGE) {
            onChange(if (age == null) 8 else age + 1)
        }
    }
}

@Composable
private fun StepButton(text: String, color: Color, enabled: Boolean, onClick: () -> Unit) {
    Text(
        text,
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (enabled) color else color.copy(alpha = 0.3f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(top = 6.dp)
            .fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}
