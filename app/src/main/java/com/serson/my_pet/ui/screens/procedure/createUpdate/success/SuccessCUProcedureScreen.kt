package com.serson.my_pet.ui.screens.procedure.createUpdate.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.serson.my_pet.R
import com.serson.my_pet.alarm.scheduleNotification
import com.serson.my_pet.navigation.Routes
import com.serson.my_pet.ui.components.ButtonComponent
import com.serson.my_pet.ui.components.MyPetTopBar
import com.serson.my_pet.ui.screens.procedure.createUpdate.CreateUpdateProcedureViewModel
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.DatePickerSelector
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.FrequencySelector
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.NotificationsSelector
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.SelectProcedureType
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.TimeDoneSelector
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.getDropdownMenuColors
import com.serson.my_pet.ui.screens.procedure.createUpdate.success.screenComponents.getOutLinedTextFieldColors
import com.serson.my_pet.ui.theme.GreenButton
import com.serson.my_pet.util.PetDateTimeFormatter
import com.serson.my_pet.util.validate
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
fun SuccessCUProcedureScreen(
    navController: NavHostController,
    isCreateScreen: Boolean,
    profileId: Int,
    viewModel: CreateUpdateProcedureViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val types = viewModel.types

    val procedureDB by viewModel.procedureUiState.collectAsState()

    val type by remember { mutableStateOf(viewModel.type) }
    var title by remember { mutableStateOf(viewModel.title) }
    var procedure by remember { mutableStateOf(procedureDB) }

    LaunchedEffect(procedureDB) {
        procedure = procedureDB
    }

    // создание уведомлений
    val createDelayedNotification = remember { mutableStateOf(false) }
    if (createDelayedNotification.value) {
        val titleNotification = if (title.name.isBlank()) "Процедура" else title.name
        val timeMessage = procedure.dateDone.format(PetDateTimeFormatter.date)
        val dateMessage = procedure.dateDone.format(PetDateTimeFormatter.time)
        val message = "$dateMessage — $timeMessage"
        val procedureId = procedure.id

        // Получаем дату и время из процедуры
        val reminderDate = procedure.reminder!!.format(PetDateTimeFormatter.date)
        val reminderTime = procedure.reminder!!.format(PetDateTimeFormatter.time)

        // Создаем LocalDateTime из даты и времени
        val reminderDateTime = LocalDateTime.of(
            LocalDate.parse(reminderDate, PetDateTimeFormatter.date), // Преобразуем дату из строки в LocalDate
            LocalTime.parse(reminderTime, PetDateTimeFormatter.time)  // Преобразуем время из строки в LocalTime
        )
        // Логирование времени перед передачей в scheduleNotification
        scheduleNotification(context, titleNotification, message, reminderDateTime, procedureId)
        createDelayedNotification.value = false
    }

    Scaffold(
        topBar = {
            MyPetTopBar(
                text = if (isCreateScreen) stringResource(Routes.CreateProcedure.title) else stringResource(
                    Routes.UpdateProcedure.title
                ),
                canNavigateBack = true,
                navigateUp = { navController.navigateUp() },
                actions = {}
            )
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // тип процедуры
            var selectedType by remember {
                mutableStateOf(
                    if (isCreateScreen) types.first()
                    else type
                )
            }
            SelectProcedureType(
                types = types.toImmutableList(),
                selectedType = selectedType,
                dropdownMenuColors = getDropdownMenuColors(),
                changeSelectedType = { newType ->
                    selectedType = newType
                    title = title.copy(type = newType.id)
                },
            )

            // Название процедуры
            var titleIsCorrect by remember { mutableStateOf(!isCreateScreen) }
            // TODO: сделать чтобы при вводе тайтла выпадали последние введенные

            OutlinedTextField(
                value = title.name,
                onValueChange = {
                    titleIsCorrect = validate(it)
                    title = title.copy(name = it)
                },
                singleLine = true,
                label = { Text(stringResource(R.string.creation_procedure_screen_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                isError = !titleIsCorrect,
                colors = getOutLinedTextFieldColors()
            )

            // периодичность - выпадающее меню с выбором
            FrequencySelector(
                procedure = procedure,
                isCreateScreen = isCreateScreen,
                onProcedureChange = { newProcedure ->
                    procedure = newProcedure
                }
            )

            // Время выполнения - тайм пикер
            TimeDoneSelector(
                procedure = procedure,
                onProcedureChange = { newProcedure ->
                    procedure = newProcedure
                }
            )

            // дата выполнения
            DatePickerSelector(
                procedure = procedure,
                onProcedureChange = { newProcedure ->
                    procedure = newProcedure
                }
            )

            // уведомление
            NotificationsSelector(
                procedure = procedure,
                onProcedureChange = { newProcedure ->
                    procedure = newProcedure
                }
            )

            // заметки
            OutlinedTextField(
                value = procedure.notes,
                onValueChange = { procedure = procedure.copy(notes = it) },
                label = { Text(stringResource(id = R.string.creation_procedure_screen_notes)) },
                trailingIcon = {
                    IconButton(onClick = { procedure = procedure.copy(notes = "") }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = false,
                shape = RoundedCornerShape(8.dp),
                colors = getOutLinedTextFieldColors()
            )

            // сохранение
            ButtonComponent(
                onClick = {
                    if (isCreateScreen) {
                        procedure = procedure.copy(pet = profileId)
                        title = title.copy(type = selectedType.id)
                        viewModel.createProcedure(procedure, title)

                    } else {
                        title = title.copy(type = selectedType.id)
                        viewModel.updateProcedure(procedure, title)
                    }
                    if (procedure.reminder != null) {
                        createDelayedNotification.value = true
                    }
                },
                text = stringResource(id = R.string.save_button_description),
                color = ButtonDefaults.buttonColors(containerColor = GreenButton),
                icon = null,
                modifier = Modifier.fillMaxWidth(),
                textColor = Color.White,
                borderColor = GreenButton,
                enabled = true,
            )
        }
    }
}
