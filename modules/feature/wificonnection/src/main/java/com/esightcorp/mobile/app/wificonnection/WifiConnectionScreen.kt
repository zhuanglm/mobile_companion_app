package com.esightcorp.mobile.app.wificonnection

import android.Manifest
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel
import com.google.accompanist.permissions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope


private const val TAG = "WifiConnectionScreen"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WifiConnectionScreen(
    navController: NavController,
    vm : WifiConnectionViewModel = hiltViewModel()){
    var wifiPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = vm.getWifiPermissionList(), onPermissionsResult = { result ->
       result.keys.forEach{ key ->
           Log.e(TAG, "WifiConnectionScreen: ${key}, ${result[key]}", )
       }
    })
    val wifiUiState by vm.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(key1 = lifecycleOwner, effect = {
//        val observer = LifecycleEventObserver{ _, event ->
//            if(event == Lifecycle.Event.ON_START){
//                Log.d(TAG, "WifiConnectionScreen: LAUNCH THE REQUEST")
//                wifiPermissionState.launchMultiplePermissionRequest()
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//
//    })
    Log.d(TAG, "WifiConnectionScreen: ${vm.getWifiPermissionList().toString()}")

    MaterialTheme{
        Scaffold (
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    elevation = 4.dp,
                    content = {
                        Text(text = "Wifi connection screen")
                    })
            },
            snackbarHost = {

            }){ contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Column content
                Log.d(TAG, "WifiConnectionScreen: ${wifiPermissionState.permissions.toString()}")
                Log.d(TAG, "WifiConnectionScreen: ${wifiPermissionState.allPermissionsGranted}")

//                /**
//                 * Permission state code
//                 */
//                wifiPermissionState.permissions.forEach { perm ->
//                    when(perm.permission){
//                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
//                            when{
//                                perm.status.isGranted -> {
//                                    Log.i(TAG, "WifiConnectionScreen: COARSE LOCATION PERMISSIONS ACCEPTED")
//                                }
//                                perm.status.shouldShowRationale ->{
//                                    Log.e(TAG, "WifiConnectionScreen: COARSE LOCATION SHOW RATIONALE PERMISSIONS " )
//                                }
//                                perm.status.isPermanentlyDenied() -> {//This should be turning it off fully?
//                                    Log.e(TAG, "WifiConnectionScreen:COARSE LOCATION PERMANENTLY DENIED " )
//
//                                }
//                            }
//                        }
//                        Manifest.permission.ACCESS_FINE_LOCATION -> {
//                            when{
//                                perm.status.isGranted -> {
//                                    Log.i(TAG, "WifiConnectionScreen:FINE LOCATION PERMISSIONS ACCEPTED")
//                                }
//                                perm.status.shouldShowRationale ->{
//                                    Log.e(TAG, "WifiConnectionScreen:FINE LOCATION SHOW RATIONALE PERMISSIONS " )
//                                }
//                                perm.status.isPermanentlyDenied() -> {//This should be turning it off fully?
//                                    Log.e(TAG, "WifiConnectionScreen:FINE LOCATION PERMANENTLY DENIED " )
//
//                                }
//                            }
//                        }
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION-> {
//                            when{
//                                perm.status.isGranted -> {
//                                    Log.i(TAG, "WifiConnectionScreen:BACKGROUND LOCATION PERMISSIONS ACCEPTED")
//                                }
//                                perm.status.shouldShowRationale ->{
//                                    Log.e(TAG, "WifiConnectionScreen:BACKGROUND LOCATION SHOW RATIONALE PERMISSIONS " )
//                                }
//                                perm.status.isPermanentlyDenied() -> {//This should be turning it off fully?
//                                    Log.e(TAG, "WifiConnectionScreen: BACKGROUND LOCATION PERMANENTLY DENIED " )
//
//                                }
//                            }
//                        }
//                        Manifest.permission.CHANGE_WIFI_STATE -> {
//                            when{
//                                perm.status.isGranted -> {
//                                    Log.i(TAG, "WifiConnectionScreen: CHANGE_WIFI_STATE PERMISSIONS ACCEPTED")
//                                }
//                                perm.status.shouldShowRationale ->{
//                                    Log.e(TAG, "WifiConnectionScreen: CHANGE_WIFI_STATE SHOW RATIONALE PERMISSIONS " )
//                                }
//                                perm.status.isPermanentlyDenied() -> {//This should be turning it off fully?
//                                    Log.e(TAG, "WifiConnectionScreen: CHANGE_WIFI_STATE PERMANENTLY DENIED " )
//
//                                }
//                            }
//                        }
//                    }
//                }

                /**
                 * end of permission state code
                 */
                if(wifiPermissionState.allPermissionsGranted){
                    vm.updatePermissionsGranted(true)
                    OutlinedTextField(value = wifiUiState.ssid,
                        onValueChange = { vm.updateSsid(it)},
                        label = {Text("SSID")}
                    )
                    OutlinedTextField(value = wifiUiState.password,
                        onValueChange = { vm.updatePassword(it)},
                        label = {Text("PASSWORD")}
                    )
                    OutlinedTextField(value = wifiUiState.wifiType,
                        onValueChange = { vm.updateWifiType(it)},
                        label = {Text("Wifi Type")}
                    )

                    Button(onClick = {
                        Log.e(TAG, "WifiConnectionScreen: SSID = ${wifiUiState.ssid}, Password = ${wifiUiState.password}, Wifi Type = ${wifiUiState.wifiType}" )
                        vm.sendWifiCredsViaBluetooth()
                    }) {
                        Text(text = "Blah")
                    }
                }else if(!wifiPermissionState.allPermissionsGranted){
                    vm.updatePermissionsGranted(false)
                    RequestPermissions(permissionList = wifiPermissionState)

                }


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
            Button(modifier = Modifier.padding(20.dp),
                onClick = {
                    permissionList.launchMultiplePermissionRequest()
                          },
                content = {
                    Text(text = "Into the void")
                })
        }
    }
}