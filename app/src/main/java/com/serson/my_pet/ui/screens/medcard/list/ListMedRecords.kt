package com.serson.my_pet.ui.screens.medcard.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.serson.my_pet.ui.components.StatusDialog
import com.serson.my_pet.ui.screens.LoadingScreen
import com.serson.my_pet.ui.screens.medcard.list.success.SuccessListMedRecords
import kotlinx.coroutines.launch

@Composable
fun ListMedRecords(
    navController: NavHostController,
    profileId: Int,
    canNavigateBack: Boolean,
    viewModel: ListMedRecordsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val pet = viewModel.pet

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getPetsMedRecords(profileId)
        }
    }

    if (pet.id == -1) {
        LoadingScreen()
    } else {
        SuccessListMedRecords(
            canNavigateBack,
            profileId,
            { navController }
        )
    }
}
