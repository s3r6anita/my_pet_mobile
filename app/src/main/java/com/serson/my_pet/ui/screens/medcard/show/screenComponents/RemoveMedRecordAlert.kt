package com.serson.my_pet.ui.screens.medcard.show.screenComponents

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serson.my_pet.R
import com.serson.my_pet.data.db.entities.MedRecord
import com.serson.my_pet.ui.screens.medcard.show.MedRecordViewModel
import kotlinx.coroutines.launch


@Composable
fun RemoveMedRecordAlert(
    medRecord: MedRecord,
    closeAlertDialog: () -> Unit,
    viewModel: MedRecordViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()

    AlertDialog(
        shape = RoundedCornerShape(12.dp),
        title = {
            Text(stringResource(R.string.medrecord_delete))
        },
        text = {
            Text(stringResource(R.string.medrecord_question_to_delete))
        },
        onDismissRequest = { closeAlertDialog() },
        confirmButton = {
            TextButton(
                onClick = {
                    closeAlertDialog()
                    scope.launch {
                        viewModel.deleteMedRecord(medRecord)
                    }
                }
            ) {
                Text(stringResource(R.string.medrecord_delete_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    closeAlertDialog()
                }
            ) {
                Text(stringResource(R.string.medrecord_cancel_button))
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        )
    )
}
