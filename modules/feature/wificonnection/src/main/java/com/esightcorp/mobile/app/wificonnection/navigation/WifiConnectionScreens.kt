package com.esightcorp.mobile.app.wificonnection

import androidx.navigation.NavType
import androidx.navigation.navArgument

@Deprecated("")
sealed class WifiConnectionScreens(val route: String) {
    object IncomingNavigationRoute : WifiConnectionScreens("wificonnection")
    object SearchingForNetworkRoute : WifiConnectionScreens("searching_for_networks"){
        const val flowArg = "flow"
        val routeWithArgs = "${route}/{$flowArg}"
        val arguments = listOf(
            navArgument(flowArg){type = NavType.StringType},
        )
    }
    object SelectNetworkRoute : WifiConnectionScreens("select_network")
    object ConnectingRoute : WifiConnectionScreens("connecting")
    object ConnectedRoute : WifiConnectionScreens("connected")
    object ErrorRoute : WifiConnectionScreens("wifi_error")
    object EnterPasswordRoute : WifiConnectionScreens("enter_password")
    object WifiQRCodeRoute : WifiConnectionScreens("wifi_qrcode")
    object HowToScanQrRoute : WifiConnectionScreens("help_wifi_qrcode")
    object AdvancedNetworkSettingsRoute : WifiConnectionScreens("advanced_network_settings")
    object SelectNetworkSecurityRoute : WifiConnectionScreens("select_network_security")
    object UnableToConnectRoute : WifiConnectionScreens("unable_to_connect")
    object WifiOffRoute : WifiConnectionScreens("wifi_off")
    object NoNetworksFoundRoute : WifiConnectionScreens("no_networks_found")
    object AlreadyConnectedRoute : WifiConnectionScreens("already_connected")


}
