package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner

@Composable
fun SearchingForNetworksRoute(
    navController: NavController
) {

    SearchingForNetworksScreen(
        modifier = Modifier,
        navController = navController
        /*loadingText = TODO: add string resource here*/
    )
}

@Composable
internal fun SearchingForNetworksScreen(
    modifier: Modifier = Modifier,
    loadingText: String = "Searching for Wi-Fi networks",
    navController: NavController
) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {
        LoadingScreenWithSpinner(loadingText = loadingText, modifier = modifier)
    }
}

@Preview
@Composable
fun SearchingForNetworksPreview() {
//    SearchingForNetworksScreen()
}