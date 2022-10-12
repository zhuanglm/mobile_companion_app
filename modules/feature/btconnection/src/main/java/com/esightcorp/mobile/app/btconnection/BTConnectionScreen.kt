package com.esightcorp.mobile.app.btconnection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiEvent
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectionViewModel
import com.google.accompanist.permissions.*
import kotlin.contracts.contract

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BtConnectionScreen(
    navController: NavController,
    vm: BtConnectionViewModel = hiltViewModel()){
    val bluetoothPermissionState = rememberMultiplePermissionsState(permissions = vm.getBluetoothPermissionsList())
    val state = vm.uiState.value
    val context = LocalContext.current


    if(bluetoothPermissionState.allPermissionsGranted){
        vm.onEvent(BluetoothUiEvent.PermissionsGranted(areGranted = true))
        isBluetoothEnabled(vm)
    }else{
        RequestPermissions(permissionList = bluetoothPermissionState)
    }
}

@Composable
fun isBluetoothEnabled( vm: BtConnectionViewModel){
    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(), onResult = {
        Log.d("TAG", "isBluetoothEnabled: ${it}")
        if(it.resultCode != Activity.RESULT_OK){
            vm.onEvent(BluetoothUiEvent.BluetoothEnabled(false))
            Log.d("TAG", "isBluetoothEnabled: false ")
        }else{
            vm.onEvent(BluetoothUiEvent.BluetoothEnabled(true))
            Log.d("TAG", "isBluetoothEnabled: true ")
            vm.onEvent(BluetoothUiEvent.BluetoothReadyToGo(true))
            vm.getBtConnectionState()

        }
    })
    SideEffect {
        if(!vm.uiState.value.isBluetoothEnabled){
            launcher.launch(intent)
        }
    }

}


//TODO: REQUEST_PERMISSION composable -> move this to somewhere reusable, preferrably somewhere in a lib module
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(permissionList: MultiplePermissionsState) {
    Column() {
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { permissionList.launchMultiplePermissionRequest()}) {
            Text(text = "Request Permissions")
        }
    }
}


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


