package com.serson.my_pet.ui.screens.medcard.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serson.my_pet.data.db.Repository
import com.serson.my_pet.data.db.entities.MedRecord
import com.serson.my_pet.data.db.entities.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ListMedRecordsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _medRecordsUiState = MutableStateFlow(emptyList<MedRecord>())
    val medRecordsUiState = _medRecordsUiState.asStateFlow()
    private val _msg = MutableStateFlow("")
    val msg = _msg.asStateFlow()

    private val _pet = MutableStateFlow<Pet?>(null)
    val pet = _pet.asStateFlow()

    fun getPetsMedRecords(petId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _pet.value = repository.getPetForCU(petId)
            repository.getMedRecordsForPet(petId).collect { medRecords ->
                _medRecordsUiState.value = medRecords
            }
        }
    }

}
