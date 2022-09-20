package com.esightcorp.mobile.app.eshare.navigation

sealed class EshareScreens(val route: String){
    object EshareHomeScreen: EshareScreens("eshare_home")
    object IncomingNavigationRoute: EshareScreens("eshare")
}
