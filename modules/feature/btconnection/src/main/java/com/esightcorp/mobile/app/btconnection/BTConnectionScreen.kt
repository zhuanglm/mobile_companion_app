package com.esightcorp.mobile.app.btconnection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectionViewModel
import com.esightcorp.mobile.app.ui.components.*
import com.google.accompanist.permissions.*
import java.time.LocalDateTime

const val TAG = "BtConnectionScreen"

@Composable
fun BtConnectionScreen(
    navController: NavController,
    vm: BtConnectionViewModel = hiltViewModel()){
    Log.d(TAG, "BtConnectionScreen: ")
    val btUiState by vm.uiState.collectAsState()
    NoDeviceConnectedScreen(
        onSettingsButtonPressed = { },
        onFeedbackButtonPressed = { },
        onConnectToDeviceButtonPressed = { },
        onTermsAndConditionsPressed = { },
        onPrivacyPolicyPressed = { },
        btUiState = btUiState,
        navController = navController
    )

   /* IsBluetoothEnabled(vm = vm)
    if(btUiState.isBtEnabled && !btUiState.btConnectionStatus){
        BaseBtScreen(vm = vm, btUiState = btUiState, navController = navController)
    } else if (btUiState.btConnectionStatus){
       NavigateHome(navController = navController, btUiState = btUiState)
    }
*/
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
){
    Surface(modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout {
            val (topBar, greeting, deviceButton, terms, feedback) = createRefs()
            ESightTopAppBar(
                showBackButton = false,
                showSettingsButton = true,
                onBackButtonInvoked = { /*Unused*/ },
                onSettingsButtonInvoked = {onSettingsButtonPressed},
                modifier = modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            PersonalGreeting(modifier = modifier
                .padding(25.dp, 0.dp, 25.dp, 0.dp)
                .constrainAs(greeting) {
                    top.linkTo(topBar.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            AddDeviceButton(
                onClick = {navController.navigate(BtConnectionScreens.BtSearchingRoute.route)},
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(deviceButton) {
                        top.linkTo(greeting.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            //Need to verify whats going on with this padding, 15.dp seems better than 25.dp which would match everything else
            TermsAndPolicy(
                onTermsInvoked = { onTermsAndConditionsPressed },
                onPrivacyPolicyInvoked = { onPrivacyPolicyPressed },
                modifier = modifier
                    .padding(15.dp, 0.dp, 15.dp, 0.dp)
                    .constrainAs(terms) {
                        bottom.linkTo(feedback.top, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            FeedbackButton(onClick = { },
                modifier = modifier.constrainAs(feedback){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

        }
    }
}


@Composable
fun NavigateHome(
    navController: NavController,
    btUiState: BluetoothUiState){
    LaunchedEffect(Unit){
        Log.d(TAG, "BtConnectionScreen: ")
        navController.navigate("home_first/{${btUiState.getConnectedDevice}}")
    }
}

@Composable
fun NavigateBluetoothDisabled(
    navController: NavController,
){
    LaunchedEffect(Unit){
        Log.d(TAG, "NavigateBluetoothDisabled: ")
        navController.navigate(BtConnectionScreens.BtDisabledScreen.route)
    }
}

@Composable
fun IsBluetoothEnabled(
    vm: BtConnectionViewModel){
    /**
     * No real UI here, we're calling the system UI to turn on bluetooth
     */
    val uiState by vm.uiState.collectAsState()
    if(!uiState.isBtEnabled){
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(), onResult = {
            Log.d("TAG", "isBluetoothEnabled: $it")
            vm.updateBtEnabledState(it.resultCode == Activity.RESULT_OK)
        })
        SideEffect {
            launcher.launch(intent)
        }
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


