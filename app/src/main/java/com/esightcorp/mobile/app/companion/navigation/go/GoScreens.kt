package com.esightcorp.mobile.app.companion.navigation.go

import com.esightcorp.mobile.app.home.navigation.HomeScreens

sealed class GoScreens(val route: String){
    object HomeScreen: GoScreens(HomeScreens.IncomingNavigationRoute.route)

}
