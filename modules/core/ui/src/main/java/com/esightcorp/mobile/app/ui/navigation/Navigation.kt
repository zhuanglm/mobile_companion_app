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

    //TODO: refactor all other route from BtConnectionScreens
}

sealed class EShareNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : EShareNavigation("eshare")

    object ConnectedRoute : EShareNavigation("eshare_connected")
    object ConnectionStoppedRoute : EShareNavigation("eshare_connection_stopped")
    object UnableToConnectRoute : EShareNavigation("eshare_unable_to_connect")
    object HotspotSetupRoute : EShareNavigation("hotspot_setup")
    object RemoteBusyRoute : EShareNavigation("eshare_busy")
}
