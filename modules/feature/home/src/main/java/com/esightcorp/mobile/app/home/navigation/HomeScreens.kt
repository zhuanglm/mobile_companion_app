package com.esightcorp.mobile.app.home.navigation

sealed class HomeScreens(val route: String){
    object HomeFirstScreen: HomeScreens("home_first")
    object HomePermissionScreen: HomeScreens("home_permissions")
    object IncomingNavigationRoute: HomeScreens("home")
}
