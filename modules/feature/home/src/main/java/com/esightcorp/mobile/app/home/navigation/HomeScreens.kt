//package com.esightcorp.mobile.app.home.navigation
//
//import androidx.navigation.NavType
//import androidx.navigation.navArgument
//
//@Deprecated("")
//sealed class HomeScreens(val route: String){
//    object HomeFirstScreen: HomeScreens("home_first"){
//        const val deviceArg = "device"
//        val routeWithArgs = "${route}/{${deviceArg}}"
//        val arguments = listOf(
//            navArgument(deviceArg){type = NavType.StringType}
//        )
//    }
//    object HomePermissionScreen: HomeScreens("home_permissions")
//    object IncomingNavigationRoute: HomeScreens("home")
//}
