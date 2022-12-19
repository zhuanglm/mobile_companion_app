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
import com.google.accompanist.permissions.*
import java.time.LocalDateTime

const val TAG = "BtConnectionScreen"

@Composable
fun BtConnectionScreen(
    navController: NavController,
    vm: BtConnectionViewModel = hiltViewModel()){
    val btUiState by vm.uiState.collectAsState()

    IsBluetoothEnabled(vm = vm)
    if(btUiState.isBtEnabled && !btUiState.btConnectionStatus){
        BaseBtScreen(vm = vm, btUiState = btUiState, navController = navController)
    } else if (btUiState.btConnectionStatus){
       NavigateHome(navController = navController, btUiState = btUiState)
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



@Composable
fun BaseBtScreen(vm: BtConnectionViewModel,
                   btUiState: BluetoothUiState,
                   navController: NavController){
    Surface(color = Color(0x004c4c)) {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
        ) {
            val (settingsRow, personalGreeting, connectToDeviceButton, progress) = createRefs()
            SettingsRow(Modifier.constrainAs(settingsRow){
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 32.dp)
            })
            PersonalGreeting(Modifier.constrainAs(personalGreeting){
                top.linkTo(settingsRow.bottom, margin = 32.dp)
                start.linkTo(parent.start, margin = 32.dp)
            }, btUiState)
            if(!btUiState.btConnectionStatus){
                ConnectToDeviceButton(modifier = Modifier.constrainAs(connectToDeviceButton){
                    top.linkTo(personalGreeting.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 32.dp)
                }, navController = navController)
                /*when(btUiState.isScanning){
                    ScanningStatus.Success -> {
                    }
                    ScanningStatus.Failed -> {
                        Text(text = "Scanning has failed! OH NO!")}
                    ScanningStatus.InProgress ->{
                        CircularProgressIndicator(Modifier.constrainAs(progress){
                            top.linkTo(connectToDeviceButton.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                    }
                    else -> {
                        Log.d(TAG, "BaseHomeScreen: Unknown scanning status for BLE")
                    }
                }*/
            }else{
                LaunchedEffect(Unit){
                    navController.navigate("home_first/{${btUiState.getConnectedDevice}}")
                }
            }
        }
    }
}

@Composable
fun SettingsRow(modifier: Modifier) {
    IconButton(onClick = { Log.d(TAG, "settingsRow: Open settings here") }, modifier) {
        Icon(Icons.Filled.Settings,"Settings")
    }

}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun PersonalGreeting(modifier: Modifier,
 btUiState: BluetoothUiState){
    Box(modifier = modifier){
        ConstraintLayout(
            modifier =
            Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()) {
            val (greeting, connectionStatus) = createRefs()
            when(LocalDateTime.now().hour){
                in 0..12 -> {
                    Text(text = "Good Morning",
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = Modifier.constrainAs(greeting){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }
                in 12..16 -> {
                    Text(text = "Good Afternoon",
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = Modifier.constrainAs(greeting){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }
                else -> {
                    Text(text = "Good Evening",
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = Modifier.constrainAs(greeting){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }
            }
            Text(text = "You are not connected to an eSight Go",
                modifier = Modifier.constrainAs(connectionStatus){
                    top.linkTo(greeting.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

        }
    }

}

@Composable
fun ConnectToDeviceButton(
    modifier: Modifier,
    navController: NavController){
    Button(onClick = {navController.navigate(BtConnectionScreens.BtDevicesScreen.route) },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier) {
        ConstraintLayout() {
            val (icon, text) = createRefs()
            Image(
                Icons.Filled.Add,
                contentDescription = "Connect to an eSight",
                modifier = Modifier.constrainAs(icon){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })
            Text(text = "Connect to an eSight", modifier = Modifier.constrainAs(text){
                start.linkTo(icon.end, margin = 8.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
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


