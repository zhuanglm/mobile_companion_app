package com.esightcorp.mobile.app.companion.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.companion.navigation.toplevel.SupportedProducts
import com.esightcorp.mobile.app.companion.viewmodels.AppViewModel

private const val TAG = "SelectionScreen"

@Composable
fun SelectionScreen(
    navController: NavController,
    vm: AppViewModel = hiltViewModel(),
) {
    vm.setNavController(navController)

    LaunchedEffect(Unit) {
        Log.i(TAG, "SelectionScreen - Navigating to product screen ...")

        navController.navigate(SupportedProducts.GoProduct.route) {
            launchSingleTop = true
        }
    }
}
