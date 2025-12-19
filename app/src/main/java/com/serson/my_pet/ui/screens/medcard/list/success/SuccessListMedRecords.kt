package com.serson.my_pet.ui.screens.medcard.list.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.serson.my_pet.R
import com.serson.my_pet.navigation.Routes
import com.serson.my_pet.ui.components.BottomBarData
import com.serson.my_pet.ui.components.ButtonComponent
import com.serson.my_pet.ui.components.MyPetBottomBar
import com.serson.my_pet.ui.components.MyPetTopBar
import com.serson.my_pet.ui.components.PetCardHeader
import com.serson.my_pet.ui.screens.medcard.list.ListMedRecordsViewModel
import com.serson.my_pet.ui.theme.GreenButton
import com.serson.my_pet.ui.theme.LightBlueBackground

@Composable
fun SuccessListMedRecords(
    canNavigateBack: Boolean,
    profileId: Int,
    getNavController: () -> NavHostController,
    viewModel: ListMedRecordsViewModel = hiltViewModel()
) {
    val navController = getNavController()

    val medRecords by viewModel.medRecordsUiState.collectAsState()
    val pet = viewModel.pet



    Scaffold(
        topBar = {
            MyPetTopBar(
                text = stringResource(id = R.string.medcard_screen_title),
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() },
                actions = { }
            )
        },
        bottomBar = {
            MyPetBottomBar(
                profileId = profileId,
                canNavigateBack = canNavigateBack,
                items = BottomBarData.items,
                getNavController = { navController }
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            ) {
                PetCardHeader(petName = pet.name, backgroundColor = LightBlueBackground)

                // список медзаписей
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    medRecords.forEach { medRecord ->
                        MedRecordItem(
                            medRecord = medRecord,
                            navController = navController
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp)) // для нормального скролла
                }
            }

            // кнопка ADD
            ButtonComponent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(innerPadding.calculateBottomPadding()),
                onClick = {
                    navController.navigate("${Routes.CreateMedRecord.route}/$profileId") {
                        launchSingleTop = true
                    }
                },
                text = stringResource(id = R.string.add_button_description),
                color = ButtonDefaults.buttonColors(containerColor = GreenButton),
                icon = Icons.Default.Add,
                textColor = Color.White,
                borderColor = GreenButton
            )
        }
    }
}
