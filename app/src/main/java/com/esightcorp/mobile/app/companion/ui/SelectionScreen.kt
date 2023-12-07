package com.esightcorp.mobile.app.companion.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.esightcorp.mobile.app.companion.navigation.toplevel.SupportedProducts

@Composable
fun SelectionScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate(SupportedProducts.GoProduct.route)
    }
}
