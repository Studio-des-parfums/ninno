package com.ninno.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninno.app.network.FormulaApiClient
import com.ninno.app.network.FormulaRequest
import com.ninno.app.network.NoteChoiceDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NoteCategory { TOP, HEART, BASE }

data class NoteChoice(
    val category: NoteCategory,
    val liked: List<String>,
    val disliked: List<String>
)

data class PerfumeFormula(
    val topChoice: NoteChoice? = null,
    val heartChoice: NoteChoice? = null,
    val baseChoice: NoteChoice? = null
)

data class GeneratedFormula(
    val topNotes: List<String>,
    val heartNotes: List<String>,
    val baseNotes: List<String>,
    val description: String
)

sealed interface FormulaUiState {
    data object Idle : FormulaUiState
    data object Loading : FormulaUiState
    data class Success(val formula: GeneratedFormula) : FormulaUiState
    data class Error(val message: String) : FormulaUiState
}

class PerfumeFormulaViewModel : ViewModel() {

    private val _formula = MutableStateFlow(PerfumeFormula())
    val formula: StateFlow<PerfumeFormula> = _formula.asStateFlow()

    private val _uiState = MutableStateFlow<FormulaUiState>(FormulaUiState.Idle)
    val uiState: StateFlow<FormulaUiState> = _uiState.asStateFlow()

    fun reset() {
        _formula.value = PerfumeFormula()
        _uiState.value = FormulaUiState.Idle
    }

    fun recordChoice(category: NoteCategory, liked: List<String>, disliked: List<String>) {
        val choice = NoteChoice(category, liked, disliked)
        _formula.value = when (category) {
            NoteCategory.TOP -> _formula.value.copy(topChoice = choice)
            NoteCategory.HEART -> _formula.value.copy(heartChoice = choice)
            NoteCategory.BASE -> _formula.value.copy(baseChoice = choice)
        }
    }

    fun generateFormulaDescription() {
        val current = _formula.value
        val top = current.topChoice
        val heart = current.heartChoice
        val base = current.baseChoice
        if (top == null || heart == null || base == null) {
            _uiState.value = FormulaUiState.Error("Réponses incomplètes")
            return
        }

        _uiState.value = FormulaUiState.Loading
        viewModelScope.launch {
            try {
                val response = FormulaApiClient.generateFormula(
                    FormulaRequest(
                        top = NoteChoiceDto(top.liked, top.disliked),
                        heart = NoteChoiceDto(heart.liked, heart.disliked),
                        base = NoteChoiceDto(base.liked, base.disliked)
                    )
                )
                _uiState.value = FormulaUiState.Success(
                    GeneratedFormula(
                        topNotes = response.topNotes,
                        heartNotes = response.heartNotes,
                        baseNotes = response.baseNotes,
                        description = response.description
                    )
                )
            } catch (e: Throwable) {
                _uiState.value = FormulaUiState.Error("Impossible de générer ta formule pour le moment.")
            }
        }
    }
}
