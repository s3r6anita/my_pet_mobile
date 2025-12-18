package com.serson.my_pet.util

sealed class UIState {
    data object Loading : UIState()
    data object Error : UIState()
    data object Success : UIState()
}
