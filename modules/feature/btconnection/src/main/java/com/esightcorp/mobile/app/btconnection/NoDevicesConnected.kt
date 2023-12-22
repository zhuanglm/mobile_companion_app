package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
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
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesConnectedViewModel
import com.esightcorp.mobile.app.ui.components.buttons.AddDeviceButton
import com.esightcorp.mobile.app.ui.components.buttons.TermsAndPolicy
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun NoDeviceConnectedRoute(
    navController: NavController,
    vm: NoDevicesConnectedViewModel = hiltViewModel(),
) {
    // Get Bluetooth UI state from view model
    val btUiState by vm.uiState.collectAsState()

    BackStackLogger(navController, TAG)

    Log.d(
        TAG,
        "NoDeviceConnectedRoute - isBtEnabled: ${btUiState.isBtEnabled}\nconnectionStatus: ${btUiState.btConnectionStatus}"
    )

    // Check if Bluetooth is enabled
    when (btUiState.isBtEnabled) {
        false -> {
            // If Bluetooth is not enabled, show the Bluetooth enabled screen
            Log.d(TAG, "NoDeviceConnectedRoute: Bluetooth not enabled")
            NavigateBluetoothDisabled(navController = navController)
        }

        true -> when (btUiState.btConnectionStatus) {
            false -> {
                // If Bluetooth is enabled and there is no device connected, show the no device connected screen
                Log.d(TAG, "NoDeviceConnectedRoute: Bluetooth enabled but not connected ")
                NoDeviceConnectedScreen(
                    onScanESightPressed = vm::navigateToScanESight,
                    onSettingsButtonPressed = vm::navigateToSettings,
                    onFeedbackButtonPressed = vm::showFeedbackPage,
                    onTermsAndConditionsPressed = { },
                    onPrivacyPolicyPressed = { },
                    navController = navController
                )
            }

            true -> {
                Log.i(TAG, "NoDeviceConnectedRoute: Navigate Home should be called.")

                // If Bluetooth is enabled and there is a device connected, navigate to the home screen
                LaunchedEffect(Unit) {
                    navController.navigate(
                        HomeNavigation.FirstScreenRoute,
                        popUntil = BtConnectionNavigation.IncomingRoute,
                    )
                }
            }
        }
    }
}

// Use LaunchedEffect to navigate to Bluetooth disabled screen after a delay
@Composable
fun NavigateBluetoothDisabled(navController: NavController) = LaunchedEffect(Unit) {
    navController.navigate(BtConnectionNavigation.BtDisabledScreen)
}

//region Internal implementation

private const val TAG = "BluetoothScreens"

@Composable
private fun NoDeviceConnectedScreen(
    modifier: Modifier = Modifier,
    onScanESightPressed: OnNavigationCallback? = null,
    onSettingsButtonPressed: OnNavigationCallback? = null,
    onFeedbackButtonPressed: OnActionCallback? = null,
    onTermsAndConditionsPressed: (Int) -> Unit,
    onPrivacyPolicyPressed: (Int) -> Unit,
    navController: NavController,
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = false,
        showSettingsButton = true,
        onSettingsButtonInvoked = { onSettingsButtonPressed?.invoke(navController) },
        bottomButton = {
            FeedbackButton(
                modifier = modifier,
                onFeedbackClick = { onFeedbackButtonPressed?.invoke() },
            )
        },
    ) {
        NoDevicesBody(
            modifier = modifier,
            onPrivacyPolicyPressed = onPrivacyPolicyPressed,
            onTermsAndConditionsPressed = onTermsAndConditionsPressed,
            onScanESightPressed = onScanESightPressed,
            navController = navController
        )
    }
}

@Composable
private fun NoDevicesBody(
    modifier: Modifier,
    onPrivacyPolicyPressed: (Int) -> Unit,
    onTermsAndConditionsPressed: (Int) -> Unit,
    onScanESightPressed: OnNavigationCallback? = null,
    navController: NavController
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (greeting, deviceButton, terms) = createRefs()

        // Set up greeting message
        PersonalGreeting(
            modifier = modifier.constrainAs(greeting) {
                top.linkTo(parent.top)
            },
            connected = false,
        )

        // Set up device button to navigate to Bluetooth searching screen
        AddDeviceButton(
            onClick = { onScanESightPressed?.invoke(navController) },
            modifier = modifier.constrainAs(deviceButton) {
                top.linkTo(greeting.bottom, margin = 25.dp)
            },
        )

        // Set up terms and policy buttons
        TermsAndPolicy(
            onTermsInvoked = onTermsAndConditionsPressed,
            onPrivacyPolicyInvoked = onPrivacyPolicyPressed,
            modifier = modifier.constrainAs(terms) {
                bottom.linkTo(parent.bottom, margin = 20.dp)
            },
            textColor = MaterialTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoDeviceConnectedScreenPreview() = MaterialTheme {
    NoDeviceConnectedScreen(
        onTermsAndConditionsPressed = { },
        onPrivacyPolicyPressed = { },
        navController = rememberNavController(), // Note: this won't actually navigate in preview
        modifier = Modifier
    )
}

//endregion
