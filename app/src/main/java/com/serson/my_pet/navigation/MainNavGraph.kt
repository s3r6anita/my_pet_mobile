package com.serson.my_pet.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.serson.my_pet.ui.screens.bugReport.BugReportScreen
import com.serson.my_pet.ui.screens.profile.createUpdate.CreateUpdateProfileScreen
import com.serson.my_pet.ui.screens.profile.list.ListProfileScreen
import kotlinx.coroutines.CoroutineScope

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    globalScope: () -> CoroutineScope
) {
    navigation(
        route = START,
        startDestination = Routes.ListProfile.route

    ) {
        /** список профилей */
        composable(route = Routes.ListProfile.route) {
            ListProfileScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                globalScope = globalScope
            )
        }

        /** профиль */
        composable(
            route = "${Routes.BottomBarRoutes.Profile.route}/{profileId}/{canNavigateBack}",
            arguments = listOf(
                navArgument(name = "profileId") {
                    type = NavType.IntType
                },
                navArgument(name = "canNavigateBack") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
//            ProfileScreen(
//                navController = navController,
//                snackbarHostState = snackbarHostState,
//                profileId = backStackEntry.arguments?.getInt("profileId") ?: -1,
//                canNavigateBack = backStackEntry.arguments?.getBoolean("canNavigateBack") ?: true
//            )
        }

        /** создание профиля */
        composable(route = Routes.CreateProfile.route) {
            CreateUpdateProfileScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                isCreateScreen = true,
                globalScope = globalScope
            )
        }

        /** обновление профиля */
        composable(
            route = "${Routes.UpdateProfile.route}/{profileId}",
            arguments = listOf(
                navArgument(name = "profileId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
//            CreateUpdateProfileScreen(
//                navController = navController,
//                snackbarHostState = snackbarHostState,
//                isCreateScreen = false,
//                globalScope = globalScope,
//                profileId = backStackEntry.arguments?.getInt("profileId") ?: -1
//            )
        }

        /** обратная связь **/
        composable(
            route = Routes.BugReport.route
        ) { backStackEntry ->
            BugReportScreen(
                navController = navController
            )
        }
    }
}
