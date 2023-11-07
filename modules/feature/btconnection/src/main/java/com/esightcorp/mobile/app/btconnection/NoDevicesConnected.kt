package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesConnectedViewModel
import com.esightcorp.mobile.app.ui.components.AddDeviceButton
import com.esightcorp.mobile.app.ui.components.TermsAndPolicy
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting

private const val TAG = "BluetoothScreens"

@Composable
fun NoDeviceConnectedRoute(
    navController: NavController, vm: NoDevicesConnectedViewModel = hiltViewModel()
) {
    // Get Bluetooth UI state from view model
    val btUiState by vm.uiState.collectAsState()

    // Check if Bluetooth is enabled
    Log.d(TAG, "NoDeviceConnectedRoute: ")
    if (!btUiState.isBtEnabled) {
        // If Bluetooth is not enabled, show the Bluetooth enabled screen
        Log.d(TAG, "NoDeviceConnectedRoute: Bluetooth not enabled")
        IsBluetoothEnabled(vm = vm, navController = navController)
    } else if (!btUiState.btConnectionStatus) {
        // If Bluetooth is enabled and there is no device connected, show the no device connected screen
        Log.d(TAG, "NoDeviceConnectedRoute: Bluetooth enabled but not connected ")
        NoDeviceConnectedScreen(
            onSettingsButtonPressed = { vm.navigateToSettings(navController) },
            onFeedbackButtonPressed = { },
            onConnectToDeviceButtonPressed = { },
            onTermsAndConditionsPressed = { },
            onPrivacyPolicyPressed = { },
            btUiState = btUiState,
            navController = navController
        )
    } else {
        // If Bluetooth is enabled and there is a device connected, navigate to the home screen
        NavigateHome(navController = navController, device = btUiState.connectedDevice)
    }
}

@Composable
internal fun NoDeviceConnectedScreen(
    onSettingsButtonPressed: () -> Unit,
    onFeedbackButtonPressed: () -> Unit,
    onConnectToDeviceButtonPressed: () -> Unit,
    onTermsAndConditionsPressed: (Int) -> Unit,
    onPrivacyPolicyPressed: (Int) -> Unit,
    btUiState: BluetoothUiState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Set up UI using ConstraintLayout

    BaseScreen(modifier = modifier,
        showBackButton = false,
        showSettingsButton = true,
        onBackButtonInvoked = { /*Unused*/ },
        onSettingsButtonInvoked = onSettingsButtonPressed,
        bottomButton = {
            FeedbackButton(
                onFeedbackClick = onFeedbackButtonPressed, modifier = modifier
            )
        }) {
        NoDevicesBody(
            modifier = modifier,
            onPrivacyPolicyPressed = onPrivacyPolicyPressed,
            onTermsAndConditionsPressed = onTermsAndConditionsPressed,
            navController = navController
        )

    }
}

@Preview(showBackground = true)
@Composable
fun NoDeviceConnectedScreenPreview() {
    MaterialTheme {
        NoDeviceConnectedScreen(
            onSettingsButtonPressed = { /* implement dummy action for preview */ },
            onFeedbackButtonPressed = { /* implement dummy action for preview */ },
            onConnectToDeviceButtonPressed = { /* implement dummy action for preview */ },
            onTermsAndConditionsPressed = { /* implement dummy action for preview */ },
            onPrivacyPolicyPressed = { /* implement dummy action for preview */ },
            btUiState = BluetoothUiState(), // provide dummy state
            navController = rememberNavController(), // Note: this won't actually navigate in preview
            modifier = Modifier
        )
    }
}

@Composable
private fun NoDevicesBody(
    modifier: Modifier,
    onPrivacyPolicyPressed: (Int) -> Unit,
    onTermsAndConditionsPressed: (Int) -> Unit,
    navController: NavController
) {
    ConstraintLayout(modifier = modifier) {
        val (greeting, deviceButton, spacer, terms) = createRefs()

        // Set up greeting message
        PersonalGreeting(
            modifier = modifier.constrainAs(greeting) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            connected = false,
        )

        // Set up device button to navigate to Bluetooth searching screen
        AddDeviceButton(onClick = { navController.navigate(BtConnectionScreens.BtSearchingRoute.route) },
            modifier = modifier.constrainAs(deviceButton) {
                top.linkTo(greeting.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        Spacer(modifier = modifier
            .height(300.dp)
            .constrainAs(spacer) {
                top.linkTo(deviceButton.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

            })

        // Set up terms and policy buttons
        TermsAndPolicy(
            onTermsInvoked = onTermsAndConditionsPressed,
            onPrivacyPolicyInvoked = onPrivacyPolicyPressed,
            modifier = modifier.constrainAs(terms) {
                top.linkTo(spacer.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textColor = MaterialTheme.colors.onSurface
        )


    }

}

@Preview(showBackground = true)
@Composable
fun NoDevicesBodyPreview() {
    MaterialTheme {
        NoDevicesBody(
            modifier = Modifier,
            onPrivacyPolicyPressed = { /* implement dummy action for preview */ },
            onTermsAndConditionsPressed = { /* implement dummy action for preview */ },
            navController = rememberNavController() // Note: this won't actually navigate in preview
        )
    }
}

@Composable
fun NavigateHome(
    navController: NavController, device: String
) {
// Use LaunchedEffect to navigate to home screen after a delay
    LaunchedEffect(Unit) {
        navController.navigate("home_first")
    }
}

@Composable
fun NavigateBluetoothDisabled(
    navController: NavController,
) {
// Use LaunchedEffect to navigate to Bluetooth disabled screen after a delay
    LaunchedEffect(Unit) {
        navController.navigate(BtConnectionScreens.BtDisabledScreen.route)
    }
}

@Composable
fun IsBluetoothEnabled(
    vm: NoDevicesConnectedViewModel,
    navController: NavController,
) {
// Get Bluetooth UI state from view model
    val uiState by vm.uiState.collectAsState()
    // Check if Bluetooth is not enabled
    if (!uiState.isBtEnabled) {
        NavigateBluetoothDisabled(navController = navController)
    }
}


//TODO:move with the REQUEST_PERMISSION composable
//@OptIn(ExperimentalPermissionsApi::class)
//private fun getTextToShowGivenPermissions(
//    permissions: List<PermissionState>,
//    shouldShowRationale: Boolean
//): String {
//    val revokedPermissionsSize = permissions.size
//    if (revokedPermissionsSize == 0) return ""
//
//    val textToShow = StringBuilder().apply {
//        append("The ")
//    }
//
//    for (i in permissions.indices) {
//        textToShow.append(permissions[i].permission)
//        when {
//            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
//                textToShow.append(", and ")
//            }
//            i == revokedPermissionsSize - 1 -> {
//                textToShow.append(" ")
//            }
//            else -> {
//                textToShow.append(", ")
//            }
//        }
//    }
//    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
//    textToShow.append(
//        if (shouldShowRationale) {
//            " important. Please grant all of them for the app to function properly."
//        } else {
//            " denied. The app cannot function without them."
//        }
//    )
//    return textToShow.toString()
//}


