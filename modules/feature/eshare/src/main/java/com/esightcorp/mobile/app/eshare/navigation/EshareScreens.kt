package com.esightcorp.mobile.app.eshare.navigation

sealed class EshareScreens(val route: String){
    object IncomingNavigationRoute: EshareScreens("eshare")
    object EshareConnectingRoute: EshareScreens("eshare_connecting")
    object EshareConnectedRoute: EshareScreens("eshare_connected")
    object EshareConnectionStoppedRoute: EshareScreens("eshare_connection_stopped")
    object EshareConnectionUnsuccessfulRoute: EshareScreens("eshare_connection_unsuccessful")
    object EshareHowToSetupRoute: EshareScreens("eshare_how_to_setup")
    object EshareUnableToConnectRoute: EshareScreens("eshare_unable_to_connect")
    object EshareWifiDisabledRoute: EshareScreens("eshare_wifi_disabled")
    object HotspotSetupRoute: EshareScreens("hotspot_setup")
}
