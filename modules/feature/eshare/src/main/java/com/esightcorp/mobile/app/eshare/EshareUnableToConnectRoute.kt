package com.esightcorp.mobile.app.eshare

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareUnableToConnectViewModel
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.SetupHotspotButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen


@Composable
fun EshareUnableToConnectRoute(
    navController: NavController,
    vm: EshareUnableToConnectViewModel = hiltViewModel()
) {

    EshareUnableToConnectScreen(
        navController = navController,
        onBackPressed = { navController.popBackStack("home_first", false) },
        onTryAgainPressed = {Unit},
        onSetupHotspotPressed = vm::onSetupHotspotPressed,
        modifier = Modifier
    )

}

@Composable
internal fun EshareUnableToConnectScreen(
    navController: NavController,
    onBackPressed: (NavController) -> Unit = { navController.popBackStack("home_first", false) },
    onTryAgainPressed: (NavController) -> Unit = {Unit},
    onSetupHotspotPressed: (NavController) -> Unit = {Unit},
    modifier: Modifier = Modifier

) {
 BaseScreen(
     modifier = modifier,
     showBackButton = true,
     showSettingsButton = false,
     onBackButtonInvoked = { onBackPressed(navController) },
     onSettingsButtonInvoked = { Unit},
     bottomButton = {
         SetupHotspotButton {
                onSetupHotspotPressed(navController)
         }
     }) {

     Text(text = "Help text goes here")

 }


}