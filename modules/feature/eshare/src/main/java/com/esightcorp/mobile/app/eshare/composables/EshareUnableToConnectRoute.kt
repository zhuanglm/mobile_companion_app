package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareUnableToConnectViewModel
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.SetupHotspotButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun EshareUnableToConnectRoute(
    navController: NavController,
    vm: EshareUnableToConnectViewModel = hiltViewModel(),
) {
    EshareUnableToConnectScreen(
        navController = navController,
        onBackPressed = vm::gotoMainScreen,
        onSetupHotspotPressed = vm::onSetupHotspotPressed,
        modifier = Modifier
    )
}

//region Internal implementation

@Composable
internal fun EshareUnableToConnectScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBackPressed: OnNavigationCallback? = null,
    onSetupHotspotPressed: OnNavigationCallback? = null,
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = { SetupHotspotButton { onSetupHotspotPressed?.invoke(navController) } },
    ) {
        Text(text = "Help text goes here")
    }
}

@Preview
@Composable
internal fun EshareUnableToConnectScreenPreview() = MaterialTheme {
    EshareUnableToConnectScreen(navController = rememberNavController())
}
//endregion
