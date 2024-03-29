/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument

abstract class Navigation(open val path: String)

/**
 * The 1st navigation route of all flows/functionalities
 */
sealed class GoProduct(override val path: String) : Navigation(path) {
    object IncomingRoute : GoProduct("go_product")
}

sealed class HomeNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : HomeNavigation("home")

    object FirstScreenRoute : HomeNavigation("home_first")
    object LocationPermissionRoute : HomeNavigation("home_location_permission")
    object LocationServiceOffRoute : HomeNavigation("home_location_service_off")
}

sealed class SettingsNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : SettingsNavigation("settings")
    object EntranceRoute : SettingsNavigation("settings_entrance")
    object DisconnectConfirmationRoute : SettingsNavigation("settings_disconnect_device")
}

sealed class BtConnectionNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : BtConnectionNavigation("btconnection")

    object NoDeviceConnectedRoute : BtConnectionNavigation("btconnection_home")
    object ScanResultRoute : BtConnectionNavigation("bt_devices")
    object BtPermissionRoute : BtConnectionNavigation("bt_permission")
    object BtConnectingRoute : BtConnectionNavigation("bt_connecting")
    object BtConnectedRoute : BtConnectionNavigation("bt_connected")
    object BtSearchingRoute : BtConnectionNavigation("bt_searching")
    object NoDevicesFoundRoute : BtConnectionNavigation("no_devices_found_bt")
    object DeviceDisconnectedRoute : BtConnectionNavigation("bt_device_disconnected")

    object BtDisabledScreen : BtConnectionNavigation("bt_disabled")
    object UnableToConnectRoute : BtConnectionNavigation("unable_to_connect_bt")
}

sealed class EShareNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : EShareNavigation("eshare")

    object ConnectedRoute : EShareNavigation("eshare_connected")
    object ConnectionStoppedRoute : EShareNavigation("eshare_connection_stopped") {
        const val reason = "reason"

        val routeWithArgs = "$path/{$reason}"
        val arguments = listOf(navArgument(reason) { type = NavType.StringType })
    }

    object UnableToConnectRoute : EShareNavigation("eshare_unable_to_connect")
    object HotspotSetupRoute : EShareNavigation("hotspot_setup")

    object WifiDisabledRoute : EShareNavigation("eshare_wifi_disabled")
    object ConnectionRejectedRoute : EShareNavigation("eshare_connection_rejected")
    object WifiSetupRoute : EShareNavigation("eshare_setup_wifi")
}

sealed class WifiNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : WifiNavigation("wificonnection")

    object SelectNetworkRoute : WifiNavigation("select_network")
    object EnterPasswordRoute : WifiNavigation("enter_password")

    object WifiOffRoute : WifiNavigation("wifi_off")

    object ScanningRoute : WifiNavigation("searching_for_networks") {
        private const val flowArg = "flow"
        val routeWithArgs = "$path/{$flowArg}"
        val arguments = listOf(navArgument(flowArg) { type = NavType.StringType })

        const val PARAM_BLUETOOTH = "bluetooth"
        const val PARAM_QR = "qr"
        const val PARAM_WIFI_CONNECTION = "check_wifi_connection"

        fun getFlowArgument(arguments: Bundle?) = arguments?.getString(flowArg)
    }

    object NoNetworksFoundRoute : WifiNavigation("no_networks_found")

    object ConnectedRoute : WifiNavigation("connected")
    object UnableToConnectRoute : WifiNavigation("unable_to_connect")
    object HowToScanRoute : WifiNavigation("how_to_scan")

    object AlreadyConnectedRoute : WifiNavigation("already_connected")

    object AdvancedNetworkSettingsRoute : WifiNavigation("advanced_network_settings")

    object ConnectingRoute : WifiNavigation("connecting")

    object WifiQRCodeRoute : WifiNavigation("wifi_qrcode")
    object SelectNetworkSecurityRoute : WifiNavigation("select_network_security")

}
