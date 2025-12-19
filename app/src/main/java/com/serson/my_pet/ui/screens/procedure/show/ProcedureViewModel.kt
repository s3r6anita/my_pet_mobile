package com.serson.my_pet.ui.screens.procedure.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serson.my_pet.data.db.Repository
import com.serson.my_pet.data.db.entities.Frequency
import com.serson.my_pet.data.db.entities.Procedure
import com.serson.my_pet.data.db.entities.ProcedureTitle
import com.serson.my_pet.data.db.entities.ProcedureType
import com.serson.my_pet.ui.screens.procedure.FrequencyOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProcedureViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _procedureUiState = MutableStateFlow(
        Procedure(
            0, 0, "", 0,
            LocalDateTime.now().withMinute(0),
            "", LocalDateTime.now().withMinute(0),
            0, 0, -1
        )
    )
    val procedureUiState = _procedureUiState.asStateFlow()
    // если null, то ошибок при удалении не было
    private val _msg = MutableStateFlow<String?>("")
    val msg = _msg.asStateFlow()

    private val _title = MutableStateFlow(ProcedureTitle(name = "", type = -1, id = -1))
    val title = _title.asStateFlow()
    private val _type = MutableStateFlow(ProcedureType(name = "", id = _title.value.type))
    val type = _type.asStateFlow()
    private val _frequency = MutableStateFlow(Frequency(option = "Никогда", frequency = "0"))
    val frequency = _frequency.asStateFlow()

    fun getProcedure(procedureId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _procedureUiState.value = repository.getProcedure(procedureId)
            _frequency.value = repository.getFrequency(_procedureUiState.value.frequencyOption)
            _title.value = repository.getProcedureTitlesForCU()
                .find { it.id == _procedureUiState.value.title } ?: title.value
            _type.value = repository.getProcedureTypes()
                .find { it.id == _title.value.type } ?: type.value

            when (frequency.value.option) {
                FrequencyOptions.Minutes.period -> frequency.value.frequency =
                    _procedureUiState.value.frequency + FrequencyOptions.Minutes.abbreviation

                FrequencyOptions.Hours.period -> frequency.value.frequency =
                    _procedureUiState.value.frequency + FrequencyOptions.Hours.abbreviation

                FrequencyOptions.Days.period -> frequency.value.frequency =
                    _procedureUiState.value.frequency + FrequencyOptions.Days.abbreviation

                FrequencyOptions.Weeks.period -> frequency.value.frequency =
                    _procedureUiState.value.frequency + FrequencyOptions.Weeks.abbreviation

                else -> frequency.value.frequency = FrequencyOptions.Never.abbreviation
            }
        }
    }

    fun deleteProcedure(procedure: Procedure) {
        viewModelScope.launch(Dispatchers.IO) {
            _msg.value = null
            repository.deleteProcedure(procedure)
        }
    }

    fun resetMsg() {
        _msg.value = ""
    }
}
