package com.ninno.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
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
import com.ninno.app.ui.Flag
import com.ninno.app.ui.ScreenBackground

private val Ink = Color(0xFF3B1E5C)
private val ErrorRed = Color(0xFFD32F2F)
// Même rouge que « Zoé la taupe » sur l'accueil
private val Accent = Color(0xFFD32F2F)

/** Carte blanche centrée, dans le style du bandeau d'accueil. */
@Composable
private fun AccountCard(
    backgroundUrl: String?,
    /** Affiché au-dessus de la carte (ex. sélecteur de langue). */
    header: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ScreenBackground(backgroundRes = Res.drawable.bg_home, backgroundUrl = backgroundUrl) {
        Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), contentAlignment = Alignment.Center) {
          Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            header?.invoke()
            Column(
                modifier = Modifier
                    .padding(top = if (header != null) 12.dp else 0.dp)
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
        label = { Text(accountStringsCurrent.serialNumber) },
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
    val language by viewModel.language.collectAsState()
    AccountCard(backgroundUrl, header = { LanguageSelect(language) { viewModel.setLanguage(it) } }) {
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
    val t = accountStringsCurrent
    CardTitle(t.welcomeTitle)
    Text(
        t.serialPrompt,
        color = Ink, textAlign = TextAlign.Center
    )
    SerialField(serial) { serial = it }
    Messages(state.error, state.info)
    PrimaryButton(t.validate, state.busy, enabled = serial.length == SERIAL_DIGITS) { viewModel.submitSerial(serial) }
    TextButton(onClick = { viewModel.goTo(AuthStep.LOGIN) }) { Text(t.haveAccount, color = Ink) }
}

@Composable
private fun RegisterStep(state: AccountState, viewModel: AccountViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<ChildGender?>(null) }
    var age by remember { mutableStateOf<Int?>(null) }
    // Erreur affichée dès que la confirmation est saisie et diffère
    val mismatch = confirmation.isNotEmpty() && confirmation != password
    val t = accountStringsCurrent
    CardTitle(t.registerTitle)
    Text(t.box(state.serial.chunked(3).joinToString("-")), color = Ink, fontSize = 13.sp)
    Field(email, { email = it }, t.parentEmail, KeyboardType.Email)
    Field(password, { password = it }, t.passwordMin, KeyboardType.Password, password = true)
    Field(confirmation, { confirmation = it }, t.confirmPassword, KeyboardType.Password, password = true)
    if (mismatch) Text(t.passwordsMismatch, color = ErrorRed, fontSize = 14.sp)
    Field(childName, { childName = it }, t.childFirstName, capitalization = KeyboardCapitalization.Words)
    GenderPicker(gender) { gender = it }
    AgePicker(age, gender?.color ?: Ink) { age = it }
    Messages(state.error, state.info)
    PrimaryButton(
        t.createAccount, state.busy,
        enabled = email.isNotBlank() && password.length >= 8 && confirmation == password && childName.isNotBlank() &&
            gender != null && age != null
    ) { viewModel.register(email, password, childName, gender!!, age!!) }
    TextButton(onClick = { viewModel.goTo(AuthStep.SERIAL) }) { Text(t.back, color = Ink) }
}

@Composable
private fun LoginStep(state: AccountState, viewModel: AccountViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val t = accountStringsCurrent
    CardTitle(t.loginTitle)
    Field(email, { email = it }, t.email, KeyboardType.Email)
    Field(password, { password = it }, t.password, KeyboardType.Password, password = true)
    Messages(state.error, state.info)
    PrimaryButton(t.signIn, state.busy, enabled = email.isNotBlank() && password.isNotBlank()) {
        viewModel.login(email, password)
    }
    TextButton(onClick = { viewModel.goTo(AuthStep.SERIAL) }) { Text(t.newBox, color = Ink) }
}

/** Espace parent : choix de l'enfant qui joue, ajout d'un enfant avec un nouveau coffret. */
@Composable
fun ChildrenScreen(viewModel: AccountViewModel, backgroundUrl: String?, onChildSelected: (ChildDto) -> Unit) {
    val state by viewModel.state.collectAsState()
    val parent = state.parent ?: return
    var adding by remember { mutableStateOf(false) }
    var serial by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<ChildGender?>(null) }
    var age by remember { mutableStateOf<Int?>(null) }

    val language by viewModel.language.collectAsState()
    val t = accountStringsCurrent
    AccountCard(
        backgroundUrl,
        header = if (adding) null else ({ LanguageSelect(language) { viewModel.setLanguage(it) } })
    ) {
        CardTitle(if (adding) t.addChildTitle else t.whoPlays)
        if (!adding) {
            parent.children.forEach { child ->
                // Couleur et icône du genre (bleu garçon, rose fille), violet si inconnu
                val childGender = ChildGender.fromApi(child.gender)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(childGender?.color ?: Ink)
                        .clickable { onChildSelected(child) }
                        .padding(vertical = 14.dp)
                ) {
                    childGender?.let {
                        Icon(it.icon, contentDescription = it.label(t), tint = Color.White, modifier = Modifier.size(24.dp).padding(end = 4.dp))
                    }
                    Text(child.firstName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    child.age?.let { Text("  · ${t.yearsOld(it)}", color = Color.White.copy(alpha = 0.85f), fontSize = 16.sp) }
                }
            }
            TextButton(onClick = { adding = true; viewModel.clearError() }) {
                Text(t.addChildLink, color = Ink)
            }
            TextButton(onClick = { viewModel.logout() }) { Text(t.logout(parent.email), color = Ink, fontSize = 13.sp) }
        } else {
            Text(t.eachChildNeedsSerial, color = Ink, textAlign = TextAlign.Center)
            SerialField(serial) { serial = it }
            Field(childName, { childName = it }, t.childFirstName, capitalization = KeyboardCapitalization.Words)
            GenderPicker(gender) { gender = it }
            AgePicker(age, gender?.color ?: Ink) { age = it }
            Messages(state.error, null)
            PrimaryButton(
                t.add, state.busy,
                enabled = serial.length == SERIAL_DIGITS && childName.isNotBlank() && gender != null && age != null
            ) {
                viewModel.addChild(serial, childName, gender!!, age!!) {
                    adding = false; serial = ""; childName = ""; gender = null; age = null
                }
            }
            TextButton(onClick = { adding = false; viewModel.clearError() }) { Text(t.cancel, color = Ink) }
        }
    }
}

/** Langue de Zoé : bouton drapeau + langue, qui déroule la liste de toutes les langues. */
@Composable
private fun LanguageSelect(selected: Language, onSelect: (Language) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .shadow(6.dp, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable { open = true }
                .padding(start = 16.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Flag(selected)
            Text(selected.nativeName, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = accountStringsCurrent.chooseLanguage, tint = Ink)
        }
        DropdownMenu(
            expanded = open,
            onDismissRequest = { open = false },
            modifier = Modifier.background(Color.White)
        ) {
            Language.entries.forEach { language ->
                val active = language == selected
                DropdownMenuItem(
                    text = {
                        Text(
                            language.nativeName,
                            color = Ink,
                            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = { Flag(language) },
                    trailingIcon = if (active) ({ Icon(Icons.Filled.Check, contentDescription = null, tint = Ink) }) else null,
                    onClick = {
                        onSelect(language)
                        open = false
                    }
                )
            }
        }
    }
}
