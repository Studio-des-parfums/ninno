package com.ninno.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninno.app.network.ChildDto
import com.ninno.app.resources.Res
import com.ninno.app.resources.bg_home
import com.ninno.app.ui.ScreenBackground

private val Ink = Color(0xFF3B1E5C)
private val ErrorRed = Color(0xFFD32F2F)
// Même rouge que « Zoé la taupe » sur l'accueil
private val Accent = Color(0xFFD32F2F)

/** Carte blanche centrée, dans le style du bandeau d'accueil. */
@Composable
private fun AccountCard(backgroundUrl: String?, content: @Composable ColumnScope.() -> Unit) {
    ScreenBackground(backgroundRes = Res.drawable.bg_home, backgroundUrl = backgroundUrl) {
        Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.94f))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Composable
private fun CardTitle(text: String) {
    Text(text, color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
}

@Composable
private fun Messages(error: String?, info: String?) {
    info?.let { Text(it, color = Ink, fontSize = 14.sp, textAlign = TextAlign.Center) }
    error?.let { Text(it, color = ErrorRed, fontSize = 14.sp, textAlign = TextAlign.Center) }
}

@Composable
private fun PrimaryButton(text: String, busy: Boolean, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled && !busy,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Accent,
            contentColor = Color.White,
            // Désactivé : reste visible (on comprend qu'il faut compléter le champ)
            disabledContainerColor = Accent.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.9f),
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp, disabledElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        if (busy) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 3.dp)
        else Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

/** Couleurs des champs : texte violet foncé bien lisible sur la carte blanche, quel que soit le thème. */
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Ink,
    unfocusedTextColor = Ink,
    cursorColor = Ink,
    focusedBorderColor = Ink,
    unfocusedBorderColor = Ink.copy(alpha = 0.5f),
    focusedLabelColor = Ink,
    unfocusedLabelColor = Ink.copy(alpha = 0.7f),
    focusedPlaceholderColor = Ink.copy(alpha = 0.35f),
    unfocusedPlaceholderColor = Ink.copy(alpha = 0.35f),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
)

/** Champ du numéro de série : chiffres uniquement, tirets automatiques (123-456-789). */
@Composable
private fun SerialField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(sanitizeSerialInput(it)) },
        label = { Text("Numéro de série") },
        placeholder = { Text("123-456-789") },
        singleLine = true,
        visualTransformation = SerialVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle = LocalTextStyle.current.copy(fontSize = 22.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp, textAlign = TextAlign.Center),
        colors = fieldColors(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Field(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, capitalization = capitalization),
        textStyle = LocalTextStyle.current.copy(fontSize = 17.sp, fontWeight = FontWeight.Medium),
        colors = fieldColors(),
        modifier = Modifier.fillMaxWidth()
    )
}

/** Parcours d'accès : numéro de série du coffret → inscription parent, ou connexion. */
@Composable
fun AuthScreen(viewModel: AccountViewModel, backgroundUrl: String?) {
    val state by viewModel.state.collectAsState()
    AccountCard(backgroundUrl) {
        when (state.step) {
            AuthStep.SERIAL -> SerialStep(state, viewModel)
            AuthStep.REGISTER -> RegisterStep(state, viewModel)
            AuthStep.LOGIN -> LoginStep(state, viewModel)
        }
    }
}

@Composable
private fun SerialStep(state: AccountState, viewModel: AccountViewModel) {
    var serial by remember { mutableStateOf("") }
    CardTitle("Bienvenue !")
    Text(
        "Saisis le numéro de série qui se trouve dans ton coffret.",
        color = Ink, textAlign = TextAlign.Center
    )
    SerialField(serial) { serial = it }
    Messages(state.error, state.info)
    PrimaryButton("Valider", state.busy, enabled = serial.length == SERIAL_DIGITS) { viewModel.submitSerial(serial) }
    TextButton(onClick = { viewModel.goTo(AuthStep.LOGIN) }) { Text("J'ai déjà un compte", color = Ink) }
}

@Composable
private fun RegisterStep(state: AccountState, viewModel: AccountViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }
    // Erreur affichée dès que la confirmation est saisie et diffère
    val mismatch = confirmation.isNotEmpty() && confirmation != password
    CardTitle("Créer le compte parent")
    Text("Coffret ${state.serial.chunked(3).joinToString("-")}", color = Ink, fontSize = 13.sp)
    Field(email, { email = it }, "E-mail du parent", KeyboardType.Email)
    Field(password, { password = it }, "Mot de passe (8 caractères min.)", KeyboardType.Password, password = true)
    Field(confirmation, { confirmation = it }, "Confirmer le mot de passe", KeyboardType.Password, password = true)
    if (mismatch) Text("Les mots de passe ne correspondent pas.", color = ErrorRed, fontSize = 14.sp)
    Field(childName, { childName = it }, "Prénom de l'enfant", capitalization = KeyboardCapitalization.Words)
    Messages(state.error, state.info)
    PrimaryButton(
        "Créer le compte", state.busy,
        enabled = email.isNotBlank() && password.length >= 8 && confirmation == password && childName.isNotBlank()
    ) { viewModel.register(email, password, childName) }
    TextButton(onClick = { viewModel.goTo(AuthStep.SERIAL) }) { Text("Retour", color = Ink) }
}

@Composable
private fun LoginStep(state: AccountState, viewModel: AccountViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    CardTitle("Connexion parent")
    Field(email, { email = it }, "E-mail", KeyboardType.Email)
    Field(password, { password = it }, "Mot de passe", KeyboardType.Password, password = true)
    Messages(state.error, state.info)
    PrimaryButton("Se connecter", state.busy, enabled = email.isNotBlank() && password.isNotBlank()) {
        viewModel.login(email, password)
    }
    TextButton(onClick = { viewModel.goTo(AuthStep.SERIAL) }) { Text("J'ai un nouveau coffret", color = Ink) }
}

/** Espace parent : choix de l'enfant qui joue, ajout d'un enfant avec un nouveau coffret. */
@Composable
fun ChildrenScreen(viewModel: AccountViewModel, backgroundUrl: String?, onChildSelected: (ChildDto) -> Unit) {
    val state by viewModel.state.collectAsState()
    val parent = state.parent ?: return
    var adding by remember { mutableStateOf(false) }
    var serial by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }

    AccountCard(backgroundUrl) {
        CardTitle(if (adding) "Ajouter un enfant" else "Qui joue ?")
        if (!adding) {
            parent.children.forEach { child ->
                Text(
                    child.firstName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Ink)
                        .clickable { onChildSelected(child) }
                        .padding(vertical = 14.dp)
                )
            }
            TextButton(onClick = { adding = true; viewModel.clearError() }) {
                Text("+ Ajouter un enfant (nouveau coffret)", color = Ink)
            }
            TextButton(onClick = { viewModel.logout() }) { Text("Se déconnecter (${parent.email})", color = Ink, fontSize = 13.sp) }
        } else {
            Text("Chaque enfant a besoin du numéro de série de son propre coffret.", color = Ink, textAlign = TextAlign.Center)
            SerialField(serial) { serial = it }
            Field(childName, { childName = it }, "Prénom de l'enfant", capitalization = KeyboardCapitalization.Words)
            Messages(state.error, null)
            PrimaryButton("Ajouter", state.busy, enabled = serial.length == SERIAL_DIGITS && childName.isNotBlank()) {
                viewModel.addChild(serial, childName) { adding = false; serial = ""; childName = "" }
            }
            TextButton(onClick = { adding = false; viewModel.clearError() }) { Text("Annuler", color = Ink) }
        }
    }
}
