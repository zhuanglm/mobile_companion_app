package com.esightcorp.mobile.app.btconnection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectionViewModel
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.google.accompanist.permissions.*

const val TAG = "BtConnectionScreen"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BtConnectionScreen(
    navController: NavController,
    vm: BtConnectionViewModel = hiltViewModel()){
    val bluetoothPermissionState = rememberMultiplePermissionsState(permissions = vm.getBluetoothPermissionsList())
    val btUiState by vm.uiState.collectAsState()

    if(btUiState.btConnectionStatus){
        Log.d(TAG, "BtConnectionScreen: CONNECTED")
        Text(text = btUiState.getConnectedDevice)
        BluetoothDevicePage(vm = vm, btUiState = btUiState, navController = navController)
    }else{
        BluetoothLandingPage(vm = vm, permissionList = bluetoothPermissionState, btUiState = btUiState)
    }
    Log.d(TAG, "BtConnectionScreen: ${bluetoothPermissionState.permissions}")

}
@Composable
fun BluetoothDevicePage(vm: BtConnectionViewModel,
                        btUiState: BluetoothUiState,
                        navController: NavController
){
    val scaffoldState = rememberScaffoldState()

    MaterialTheme{
        Scaffold (
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    elevation = 4.dp,
                    content = {
                        Log.d(TAG, "BluetoothDevicePage: ${btUiState.getConnectedDevice}")
                        Text(text = btUiState.getConnectedDevice)
                    })
            },
            snackbarHost = {

            }){ contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Green),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Column content
                Button(onClick = { navController.navigate(WifiConnectionScreens.IncomingNavigationRoute.route)}) {
                    Text(text = "Send over wifi credentials")
                }

            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothLandingPage(
    vm: BtConnectionViewModel,
    permissionList: MultiplePermissionsState,
    btUiState: BluetoothUiState
){
    val scaffoldState = rememberScaffoldState()

    MaterialTheme{
        Scaffold (
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    elevation = 4.dp, 
                    content = {
                        Text(text = "Ble Devices")
                    })
            },
            snackbarHost = {

            }){ contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Green),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Column content
                if(permissionList.allPermissionsGranted){
                    vm.updatePermissionsState(state = true)
                    if(btUiState.isBtEnabled){
                        Log.d(TAG, "BluetoothLandingPage: DISPLAY DEVICES")
                        DisplayDevices(vm = vm, deviceList = btUiState.listOfAvailableDevices )
                    }else{
                        Log.d(TAG, "BluetoothLandingPage: BLUETOOTH ENABLED")
                        IsBluetoothEnabled(vm)
                    }
                }else{
                    Log.d(TAG, "BluetoothLandingPage: NEED PERMISSIONS")
                    vm.updatePermissionsState(false)
                    RequestPermissions(permissionList = permissionList)
                }

                when(btUiState.isScanning){
                    ScanningStatus.Success -> {
                    }
                    ScanningStatus.Failed -> {
                        Text(text = "Scanning has failed! OH NO!")}
                    else -> {
                        CircularProgressIndicator()
                    }
                }

            }
        }
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
fun DisplayDevices(
    vm: BtConnectionViewModel,
    deviceList: List<String>){
    LaunchedEffect(Unit){
        vm.refreshUiDeviceList()
    }
    deviceList.forEach { device ->
        Card(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.8f),
            shape = MaterialTheme.shapes.medium,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
            elevation = 20.dp
        ) {
            Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.verticalScroll(
                ScrollState(0)
            )) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = device, modifier = Modifier.padding(20.dp, 0.dp),
                    fontWeight = FontWeight.Bold)
                Button(modifier = Modifier
                    .padding(20.dp),
                    onClick = {vm.connectToDevice(device)},
                    shape = MaterialTheme.shapes.medium,
                    content = {
                        Text(text = "Connect to $device")
                    })
            }

        }
    }
}


//TODO: REQUEST_PERMISSION composable -> move this to somewhere reusable, preferrably somewhere in a lib module
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(permissionList: MultiplePermissionsState) {
    Card(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth(0.8f),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
        elevation = 20.dp
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "You have not given us permissions. You need to.", modifier = Modifier.padding(20.dp, 0.dp))
            OutlinedButton(modifier = Modifier.padding(20.dp),
                onClick = {permissionList.launchMultiplePermissionRequest()},
                content = {
                    Text(text = "Into the void")
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


