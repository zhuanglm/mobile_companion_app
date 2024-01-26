/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

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
    object ErrorRoute : WifiConnectionScreens("wifi_error")
    object EnterPasswordRoute : WifiConnectionScreens("enter_password")
    object WifiQRCodeRoute : WifiConnectionScreens("wifi_qrcode")
    object HowToScanQrRoute : WifiConnectionScreens("help_wifi_qrcode")
    object AdvancedNetworkSettingsRoute : WifiConnectionScreens("advanced_network_settings")
    object SelectNetworkSecurityRoute : WifiConnectionScreens("select_network_security")
    object WifiOffRoute : WifiConnectionScreens("wifi_off")
    object NoNetworksFoundRoute : WifiConnectionScreens("no_networks_found")
    object AlreadyConnectedRoute : WifiConnectionScreens("already_connected")


}
