package com.serson.my_pet.ui.screens.procedure.createUpdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serson.my_pet.data.db.Repository
import com.serson.my_pet.data.db.entities.Frequency
import com.serson.my_pet.data.db.entities.Procedure
import com.serson.my_pet.data.db.entities.ProcedureTitle
import com.serson.my_pet.data.db.entities.ProcedureType
import com.serson.my_pet.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateUpdateProcedureViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _msg = MutableStateFlow<String?>("")
    val msg = _msg.asStateFlow()

    private val _procedureUiState = MutableStateFlow(
        Procedure(
            title = 0, isDone = 0, frequency = "", frequencyOption = 0,
            dateDone = LocalDateTime.now().withMinute(0),
            notes = "", reminder = null,
            pet = 0, inMedCard = 0
        )
    )
    val procedureUiState = _procedureUiState.asStateFlow()

    private var titles = emptyList<ProcedureTitle>() // список всех заголовков
    var types = emptyList<ProcedureType>() // список всех типов
    var frequencyOptions = emptyList<Frequency>() // список вариантов частоты

    var frequencyOptionsTitles = mutableListOf<String>() // список вариантов частоты

    var title = ProcedureTitle(
        // заголовок создаваемой (изменяемой) процедуры
        name = "",
        type = 0,
    )
    var type = ProcedureType( // тип создаваемой (изменяемой) процедуры
        name = "",
        id = title.id
    )

    var frequency = Frequency( // частота создаваемой (изменяемой) процедуры
        option = "никогда",
        frequency = "0"
    )

    init {
        _uiState.update { UIState.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            titles = repository.getProcedureTitlesForCU()
            types = repository.getProcedureTypes()
            frequencyOptions = repository.getFrequencyOptions()
            frequencyOptions.forEach {
                frequencyOptionsTitles.add(it.id, it.option)
            }
            _uiState.update { UIState.Success }
        }
    }

    fun getPetProcedure(procedureId: Int) {
        if (procedureId != -1) {
            _uiState.update { UIState.Loading }
            viewModelScope.launch(Dispatchers.IO) {
                _procedureUiState.value = repository.getProcedure(procedureId)
                title = titles.find { title -> title.id == _procedureUiState.value.title }
                    ?: title
                type = types.find { type -> type.id == title.type }
                    ?: type
                frequency = repository.getFrequency(_procedureUiState.value.frequencyOption)

                _uiState.update { UIState.Success }
            }
        }
    }

    fun createProcedure(procedure: Procedure, title: ProcedureTitle) {
        viewModelScope.launch(Dispatchers.IO) {
            val titleId = repository.insertTitle(title)
            val procedureWithTitleFreq = procedure.copy(title = titleId)
            repository.insertProcedure(procedureWithTitleFreq)
            _msg.value = null
        }
    }

    fun updateProcedure(procedure: Procedure, title: ProcedureTitle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTitle(title)
            repository.updateProcedure(procedure)
            _msg.value = null

        }
    }

    fun resetMsg() {
        _msg.value = ""
    }
}
