package com.esightcorp.mobile.app.companion.navigation.go

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.esightcorp.mobile.app.btconnection.navigation.addBtConnectionNavigation
import com.esightcorp.mobile.app.companion.navigation.toplevel.SupportedProducts
import com.esightcorp.mobile.app.eshare.navigation.addEshareNavigation
import com.esightcorp.mobile.app.home.navigation.addHomeNavigation
import com.esightcorp.mobile.app.settings.navigation.addSettingsNavigation
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.wificonnection.navigation.addWifiConnectionNavigation

fun NavGraphBuilder.addGoNavigation(navController: NavController) {
    navigation(
        startDestination = HomeNavigation.IncomingRoute.path,
        route = SupportedProducts.GoProduct.route,
    ) {
        addEshareNavigation(navController)
        addHomeNavigation(navController)
        addWifiConnectionNavigation(navController)
        addBtConnectionNavigation(navController)
        addSettingsNavigation(navController)
    }
}