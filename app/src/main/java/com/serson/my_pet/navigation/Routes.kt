package com.serson.my_pet.navigation

import com.serson.my_pet.R

const val START = "start"

sealed class Routes(
    val route: String,
    val title: Int
) {
    data object ListProfile : Routes("ListProfile", R.string.list_profile_screen_title)
    data object CreateProfile : Routes("CreateProfile", R.string.create_profile_screen_title)
    data object UpdateProfile : Routes("UpdateProfile", R.string.update_profile_screen_title)


    sealed class BottomBarRoutes(route: String, title: Int) : Routes(route, title) {
        data object Profile :
            BottomBarRoutes("Profile", R.string.profile_screen_title)
    }
}