package com.serson.my_pet.ui.screens.procedure.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serson.my_pet.data.db.Repository
import com.serson.my_pet.data.db.entities.Pet
import com.serson.my_pet.data.db.entities.Procedure
import com.serson.my_pet.data.db.entities.ProcedureTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ListProcedureViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _proceduresUiState = MutableStateFlow(emptyList<Procedure>())
    val proceduresUiState = _proceduresUiState.asStateFlow()

    private val _titlesUiState = MutableStateFlow(emptyList<ProcedureTitle>())
    val titlesUiState = _titlesUiState.asStateFlow()

    private val _pet = MutableStateFlow<Pet?>(null)
    val pet = _pet.asStateFlow()

    fun getPetProcedures(petId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _pet.value = repository.getPetForCU(petId)
            repository.getProceduresForPet(petId).collect { procedures ->
                _proceduresUiState.value = procedures
            }
        }
    }

    fun getTitles() {
        viewModelScope.launch {
            repository.getProcedureTitles().collect { titles ->
                _titlesUiState.value = titles
            }
        }
    }

}
