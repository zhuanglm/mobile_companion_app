package com.esightcorp.mobile.app.companion.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.esightcorp.mobile.app.companion.navigation.toplevel.SupportedProducts

private const val TAG = "SelectionScreen"

@Composable
fun SelectionScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.i(TAG, "SelectionScreen - Navigating to product screen ...")

        navController.navigate(SupportedProducts.GoProduct.route) {
            launchSingleTop = true
            navController.graph.startDestinationRoute?.let {
                popUpTo(route = it) { inclusive = true }
            }
        }
    }
}
