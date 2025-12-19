package com.serson.my_pet.ui.screens.profile.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serson.my_pet.data.db.Repository
import com.serson.my_pet.data.db.entities.Pet
import com.serson.my_pet.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _petsUiState = MutableStateFlow(emptyList<Pet>())
    val petsUiState = _petsUiState.asStateFlow()
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()
    fun getPetsProfiles() {
        viewModelScope.launch(IO) {
            _petsUiState.value = repository.getPets()
            _uiState.update { UIState.Success }
        }
    }
}
