package com.serson.my_pet.ui.screens.medcard.createUpdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serson.my_pet.data.db.Repository
import com.serson.my_pet.data.db.entities.MedRecord
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
class CreateUpdateMedRecordViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _msg = MutableStateFlow<String?>("")
    val msg = _msg.asStateFlow()

    private val _medRecordUiState = MutableStateFlow(
        MedRecord("", LocalDateTime.now(), "", -1)
    )
    val medRecordUiState = _medRecordUiState.asStateFlow()

    fun getPetMedRecord(medRecordId: Int) {
        if (medRecordId != -1) {
            _uiState.update { UIState.Loading }
            viewModelScope.launch(Dispatchers.IO) {
                _medRecordUiState.value = repository.getMedRecord(medRecordId)
                _uiState.update { UIState.Success }
            }
        }
        _uiState.update { UIState.Success }
    }

    fun createMedRecord(medRecord: MedRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            _msg.value = null
            repository.insertMedRecord(medRecord)
        }
    }

    fun updateMedRecord(medRecord: MedRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMedRecord(medRecord)
            _msg.value = null
        }
    }
}
