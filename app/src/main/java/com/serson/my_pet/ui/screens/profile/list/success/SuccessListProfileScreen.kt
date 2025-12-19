package com.serson.my_pet.ui.screens.profile.list.success

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.serson.my_pet.R
import com.serson.my_pet.navigation.Routes
import com.serson.my_pet.navigation.START
import com.serson.my_pet.ui.components.ButtonComponent
import com.serson.my_pet.ui.components.MyPetSnackBar
import com.serson.my_pet.ui.components.MyPetTopBar
import com.serson.my_pet.ui.screens.profile.list.ListProfileViewModel
import com.serson.my_pet.ui.screens.profile.list.PetItem
import com.serson.my_pet.ui.theme.BlueCheckbox
import com.serson.my_pet.ui.theme.GreenButton
import com.serson.my_pet.ui.theme.LightGrayTint
import com.serson.my_pet.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren


@Composable
fun SuccessListProfileScreen(
    snackbarHostState: SnackbarHostState,
    globalScope: CoroutineScope,
    getNavController: () -> NavHostController,
    viewModel: ListProfileViewModel = hiltViewModel()
) {
    val navController = getNavController()
    val pets by viewModel.petsUiState.collectAsState()

    val preferences = LocalContext.current.getSharedPreferences("pref", Context.MODE_PRIVATE)
    val value = preferences.getBoolean("rememberUserChoice", true)
    val (rememberUserChoice, onStateChange) = remember { mutableStateOf(value) }

    preferences.edit {
        putBoolean("rememberUserChoice", rememberUserChoice)
    }

    Scaffold(
        topBar = {
            MyPetTopBar(
                text = stringResource(id = Routes.ListProfile.title),
                canNavigateBack = false,
                navigateUp = { },
                actions = {
                    // кнопка входа
                    IconButton(onClick = {
                        navController.navigate(Routes.BugReport.route) {
                            launchSingleTop = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ContactSupport,
                            contentDescription = stringResource(id = R.string.feedback_screen)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                MyPetSnackBar(it.visuals.message)
            }
        },
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    чек бокс "Запомнить мой выбор"
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp)
                            .toggleable(
                                value = rememberUserChoice,
                                onValueChange = { onStateChange(!rememberUserChoice) },
                                role = Role.Checkbox
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Checkbox(
                            checked = rememberUserChoice,
                            onCheckedChange = null,
                            modifier = Modifier.padding(15.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = BlueCheckbox,
                                uncheckedColor = LightGrayTint,
                                checkmarkColor = White
                            )
                        )
                        Text(
                            text = stringResource(R.string.remember_my_choice),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

//            список питомцев
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        pets.forEach { pet ->
                            PetItem(
                                pet = pet,
                                canNavigateBack = !rememberUserChoice,
                                navController = navController,
                                closeSnackbar = { globalScope.coroutineContext.cancelChildren() }
                            )
                        }
                        Spacer(modifier = Modifier.height(50.dp)) // для нормального скролла
                    }
                }
            }

            // кнопка добавления нового питомца в список
            ButtonComponent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(innerPadding.calculateBottomPadding()),
                onClick = {
                    globalScope.coroutineContext.cancelChildren()
                    navController.navigate(Routes.CreateProfile.route) { launchSingleTop = true }
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
