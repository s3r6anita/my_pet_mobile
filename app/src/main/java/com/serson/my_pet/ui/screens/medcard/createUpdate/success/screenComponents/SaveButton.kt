package com.serson.my_pet.ui.screens.medcard.createUpdate.screenComponents

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.serson.my_pet.R
import com.serson.my_pet.ui.components.ButtonComponent
import com.serson.my_pet.ui.theme.GreenButton

@Composable
fun SaveButton(save: () -> Unit) {
    ButtonComponent(
        onClick = {
            save()
        },
        text = stringResource(id = R.string.save_button_description),
        color = ButtonDefaults.buttonColors(containerColor = GreenButton),
        icon = null,
        modifier = Modifier,
        textColor = Color.White,
        borderColor = GreenButton,
        enabled = true,
    )
}
