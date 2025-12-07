package com.serson.my_pet

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.serson.my_pet.navigation.SetupNavGraph
import com.serson.my_pet.ui.theme.MyPetTheme

@Composable
fun MyPetApp(notificationRoute: String?) {
    MyPetTheme {
        SetupNavGraph(
            navController = rememberNavController(),
            snackbarHostState = remember { SnackbarHostState() },
            notificationRoute = notificationRoute
        )
    }
}
