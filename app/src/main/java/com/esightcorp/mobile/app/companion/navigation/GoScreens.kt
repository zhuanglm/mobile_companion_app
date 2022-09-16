package com.esightcorp.mobile.app.companion.navigation

sealed class GoScreens(val route: String){
    object HomeScreen: GoScreens("home")
    object EshareScreen: GoScreens("eshare")
    object BtConnectionScreen: GoScreens("btconnection")
    object WifiConnectionScreen: GoScreens("wificonnection")
}
