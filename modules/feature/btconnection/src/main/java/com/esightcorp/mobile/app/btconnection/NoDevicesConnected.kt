package com.esightcorp.mobile.app.btconnection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesConnectedViewModel
import com.esightcorp.mobile.app.ui.components.AddDeviceButton
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.TermsAndPolicy
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting

private const val TAG = "BluetoothScreens"

@Composable
fun NoDeviceConnectedRoute(
    navController: NavController, vm: NoDevicesConnectedViewModel = hiltViewModel()
) {
    // Get Bluetooth UI state from view model
    val btUiState by vm.uiState.collectAsState()
    //We want this to run with every recomposition,so we constantly have the most up to date.
    vm.checkBtEnabledStatus()

    // Check if Bluetooth is enabled
    if (!btUiState.isBtEnabled) {
        // If Bluetooth is not enabled, show the Bluetooth enabled screen
        IsBluetoothEnabled(vm = vm, navController = navController)
    } else if (!btUiState.btConnectionStatus) {
        // If Bluetooth is enabled and there is no device connected, show the no device connected screen
        NoDeviceConnectedScreen(onSettingsButtonPressed = { },
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
    onTermsAndConditionsPressed: () -> Unit,
    onPrivacyPolicyPressed: () -> Unit,
    btUiState: BluetoothUiState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Set up UI using ConstraintLayout
    Surface(modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout {
            val (topBar, greeting, deviceButton, terms, feedback) = createRefs()

            // Set up top app bar with settings button
            ESightTopAppBar(showBackButton = false,
                showSettingsButton = true,
                onBackButtonInvoked = { /*Unused*/ },
                onSettingsButtonInvoked = { onSettingsButtonPressed },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            // Set up greeting message
            PersonalGreeting(
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(greeting) {
                        top.linkTo(topBar.bottom, margin = 50.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                connected = false,
            )

            // Set up device button to navigate to Bluetooth searching screen
            AddDeviceButton(onClick = { navController.navigate(BtConnectionScreens.BtSearchingRoute.route) },
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(deviceButton) {
                        top.linkTo(greeting.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            // Set up terms and policy buttons
            TermsAndPolicy(onTermsInvoked = onTermsAndConditionsPressed,
                onPrivacyPolicyInvoked = onPrivacyPolicyPressed,
                modifier = modifier
                    .padding(15.dp, 0.dp, 15.dp, 0.dp)
                    .constrainAs(terms) {
                        bottom.linkTo(feedback.top, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            // Set up feedback button
            FeedbackButton(onFeedbackClick = onFeedbackButtonPressed,
                modifier = modifier.constrainAs(feedback) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}

@Composable
fun NavigateHome(
    navController: NavController, device: String
) {
// Use LaunchedEffect to navigate to home screen after a delay
    LaunchedEffect(Unit) {
        navController.navigate("home_first/{${device}}")
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


