package com.serson.my_pet.ui.screens.profile.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ListProfileScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    globalScope: () -> CoroutineScope,
    viewModel: ListProfileViewModel = hiltViewModel()
) {
    val localScope = rememberCoroutineScope()
    //val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        localScope.launch {
            viewModel.getPetsProfiles()
        }
    }


}
