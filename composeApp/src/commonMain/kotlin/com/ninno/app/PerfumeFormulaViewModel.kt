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

data class FormulaNote(
    val name: String,
    val category: NoteCategory,
    /** Quantité en mL pour chaque volume de flacon (10, 30, 50). */
    val mlByVolume: Map<Int, Double>
)

data class GeneratedFormula(
    val volumes: List<Int>,
    val notes: List<FormulaNote>,
    val description: String
) {
    fun notesOf(category: NoteCategory) = notes.filter { it.category == category }
}

/** Volume de flacon sélectionné par défaut. */
const val DEFAULT_VOLUME_ML = 30

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
        if (top.liked.isEmpty() && heart.liked.isEmpty() && base.liked.isEmpty()) {
            _uiState.value = FormulaUiState.Error("Tu n'as aimé aucune note : recommence pour créer ton parfum !")
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
                        volumes = response.volumes,
                        notes = response.notes.mapNotNull { note ->
                            val category = when (note.type) {
                                "top" -> NoteCategory.TOP
                                "heart" -> NoteCategory.HEART
                                "base" -> NoteCategory.BASE
                                else -> return@mapNotNull null
                            }
                            FormulaNote(
                                name = note.name,
                                category = category,
                                mlByVolume = note.ml.mapNotNull { (k, v) -> k.toIntOrNull()?.let { it to v } }.toMap()
                            )
                        },
                        description = response.description
                    )
                )
            } catch (e: Throwable) {
                _uiState.value = FormulaUiState.Error("Impossible de générer ta formule pour le moment.")
            }
        }
    }
}
