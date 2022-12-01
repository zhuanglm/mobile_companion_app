package com.esightcorp.mobile.app.wificonnection

import android.Manifest
import android.net.Network
import android.net.wifi.ScanResult
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel
import com.google.accompanist.permissions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.intellij.lang.annotations.JdkConstants


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


                if(wifiPermissionState.allPermissionsGranted){
                    vm.updatePermissionsGranted(true)
                    if(wifiUiState.currentSelectedNetwork != null){
                        //TODO: Need something here to show the currently selected network
                        NetworkSelected(wifiUiState = wifiUiState, vm = vm)
                    }else{
                        NetworkList(networks = wifiUiState.networkList, wifiUiState = wifiUiState, vm = vm)
                    }
                }else if(!wifiPermissionState.allPermissionsGranted){
                    vm.updatePermissionsGranted(false)
                    RequestPermissions(permissionList = wifiPermissionState)

                }
            }
        }
    }
}

@Composable
fun NetworkSelected(
    wifiUiState: WifiConnectionUiState,
    vm: WifiConnectionViewModel
){
    var expanded by remember{mutableStateOf(false)}
    var selectedIndex by remember{ mutableStateOf(0)}
    val wifiTypes = listOf("WPA-2/WPA", "WEP", "None")
    vm.updateWifiType(wifiTypes[0])

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color.Black),
        elevation = 20.dp
    ) {
        Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
            wifiUiState.currentSelectedNetwork?.SSID?.let { ssid ->
                vm.updateSsid(ssid)
                Text(text = ssid,
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta)
            }
            OutlinedTextField(value = wifiUiState.password,
                onValueChange = { vm.updatePassword(it)},
                label = {Text("Password")}
            )
            Box(modifier = Modifier
                .fillMaxSize(0.8f)
                .wrapContentSize(Alignment.TopStart)){
                Text(wifiTypes[selectedIndex],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable { expanded = true }
                        )
                DropdownMenu(expanded = expanded,
                    onDismissRequest = {expanded = false},
                    modifier = Modifier.background(Color.Green)) {
                    wifiTypes.forEachIndexed{ index, s ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            vm.updateWifiType(s)
                        }) {
                            Text(text = s, modifier = Modifier.padding(15.dp))
                        }
                    }
                }

            }
            Button(onClick = { vm.sendWifiCredsViaBluetooth() }) {
                Text(text = "Send the creds over bt")
            }

            Row() {

            }
            Row() {


            }
        }

    }
}


@Composable
fun NetworkList(
    networks: List<ScanResult>,
    wifiUiState: WifiConnectionUiState,
    vm: WifiConnectionViewModel){
    if(networks.isNotEmpty()){
        LazyColumn{
            items(networks.size){ index ->
                NetworkRow(networks[index], vm = vm)
            }
        }
    }else{
        CircularProgressIndicator()
        Button(onClick = {
            Log.e(TAG, "WifiConnectionScreen: SSID = ${wifiUiState.ssid}, Password = ${wifiUiState.password}, Wifi Type = ${wifiUiState.wifiType}" )
            vm.startWifiScan()
        }) {
            Text(text = "get networks")
        }
    }

}

@Composable
fun NetworkRow(
    network: ScanResult,
    vm: WifiConnectionViewModel){
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = { vm.updateCurrentSelectedNetwork(network)}) {
            Text(text = network.SSID)
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

