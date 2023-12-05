package com.esightcorp.mobile.app.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

abstract class Navigation(open val path: String)

sealed class HomeNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : HomeNavigation("home")

    object FirstScreenRoute : HomeNavigation("home_first") {
        const val deviceArg = "device"
        val routeWithArgs = "$path/{$deviceArg}"
        val arguments = listOf(navArgument(deviceArg) { type = NavType.StringType })
    }

    object PermissionRoute : HomeNavigation("home_permissions")
}

sealed class SettingsNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : SettingsNavigation("settings")
    object EntranceRoute : SettingsNavigation("settings_entrance")
    object DisconnectRoute : SettingsNavigation("settings_disconnect_device")
}

sealed class BtConnectionNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : BtConnectionNavigation("btconnection")

    object NoDeviceConnectedRoute : BtConnectionNavigation("btconnection_home")
    object ScanResultRoute : BtConnectionNavigation("bt_devices")
    object BtConnectingRoute : BtConnectionNavigation("bt_connecting")
    object BtConnectedRoute : BtConnectionNavigation("bt_connected")
    object BtSearchingRoute : BtConnectionNavigation("bt_searching")
    object NoDevicesFoundRoute : BtConnectionNavigation("no_devices_found_bt")

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
    object RemoteBusyRoute : EShareNavigation("eshare_busy")

    object WifiDisabledRoute : EShareNavigation("eshare_wifi_disabled")
    object ConnectionRejectedRoute : EShareNavigation("eshare_connection_rejected")
    object WifiSetupRoute : EShareNavigation("eshare_setup_wifi")
}

sealed class WifiNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : WifiNavigation("wificonnection")

    object ScanningRoute : WifiNavigation("searching_for_networks") {
        const val flowArg = "flow"
        val routeWithArgs = "$path/{$flowArg}"
        val arguments = listOf(navArgument(flowArg) { type = NavType.StringType })

        const val PARAM_BLUETOOTH = "bluetooth"
    }

    //TODO: migrate all other paths
}
