package com.ninno.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninno.app.network.ApiException
import com.ninno.app.network.AuthApiClient
import com.ninno.app.network.ChildDto
import com.ninno.app.network.ParentDto
import com.ninno.app.network.SerialStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Étapes du parcours d'accès : numéro de série → inscription, ou connexion. */
enum class AuthStep { SERIAL, REGISTER, LOGIN }

data class AccountState(
    val restoring: Boolean = true,
    val parent: ParentDto? = null,
    val selectedChild: ChildDto? = null,
    val step: AuthStep = AuthStep.SERIAL,
    /** Numéro validé à l'étape SERIAL, réutilisé à l'inscription. */
    val serial: String = "",
    val busy: Boolean = false,
    val error: String? = null,
    val info: String? = null,
)

class AccountViewModel : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private var token: String? = TokenStorage.load()

    init {
        val saved = token
        if (saved == null) {
            _state.value = AccountState(restoring = false)
        } else {
            viewModelScope.launch {
                val parent = runCatching { AuthApiClient.me(saved) }.getOrNull()
                if (parent == null) logout()
                _state.value = AccountState(restoring = false, parent = parent)
            }
        }
    }

    fun goTo(step: AuthStep) {
        _state.value = _state.value.copy(step = step, error = null, info = null)
    }

    fun submitSerial(serial: String) = run {
        when (AuthApiClient.checkSerial(serial)) {
            SerialStatus.AVAILABLE -> _state.value = _state.value.copy(step = AuthStep.REGISTER, serial = serial)
            SerialStatus.USED -> _state.value = _state.value.copy(
                step = AuthStep.LOGIN,
                info = "Ce coffret a déjà été activé. Connecte-toi avec le compte parent."
            )
            SerialStatus.UNKNOWN -> fail("Numéro de série inconnu. Vérifie la saisie.")
        }
    }

    fun register(email: String, password: String, childFirstName: String) = run {
        val res = AuthApiClient.register(_state.value.serial, email, password, childFirstName)
        signedIn(res.token, res.parent)
    }

    fun login(email: String, password: String) = run {
        val res = AuthApiClient.login(email, password)
        signedIn(res.token, res.parent)
    }

    fun addChild(serial: String, childFirstName: String, onDone: () -> Unit) = run {
        val t = token ?: return@run
        val child = AuthApiClient.addChild(t, serial, childFirstName)
        val parent = _state.value.parent ?: return@run
        _state.value = _state.value.copy(parent = parent.copy(children = parent.children + child))
        onDone()
    }

    /** Jeton de session du parent, pour les appels qui identifient l'enfant (formule). */
    fun currentToken(): String? = token

    fun selectChild(child: ChildDto) {
        _state.value = _state.value.copy(selectedChild = child)
    }

    /** Retour à l'écran « Qui joue ? » pour changer d'enfant. */
    fun clearSelectedChild() {
        _state.value = _state.value.copy(selectedChild = null)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun logout() {
        token = null
        TokenStorage.clear()
        _state.value = AccountState(restoring = false)
    }

    private fun signedIn(newToken: String, parent: ParentDto) {
        token = newToken
        TokenStorage.save(newToken)
        _state.value = AccountState(restoring = false, parent = parent)
    }

    private fun fail(message: String) {
        _state.value = _state.value.copy(error = message)
    }

    private fun run(block: suspend () -> Unit) {
        if (_state.value.busy) return
        _state.value = _state.value.copy(busy = true, error = null, info = null)
        viewModelScope.launch {
            try {
                block()
            } catch (e: ApiException) {
                fail(e.message ?: "Erreur")
            } catch (e: Exception) {
                fail("Impossible de joindre le serveur. Réessaie dans un instant.")
            } finally {
                _state.value = _state.value.copy(busy = false)
            }
        }
    }
}
