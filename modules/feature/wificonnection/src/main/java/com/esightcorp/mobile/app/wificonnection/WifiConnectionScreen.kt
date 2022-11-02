package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel
import com.google.accompanist.permissions.*


private const val TAG = "WifiConnectionScreen"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WifiConnectionScreen(
    navController: NavController,
    vm : WifiConnectionViewModel = hiltViewModel()){

    val wifiPermissionState = rememberPermissionState(permission = android.Manifest.permission.CHANGE_WIFI_STATE)
    val wifiUiState by vm.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    var ssid by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}
    var wifiType by remember{ mutableStateOf("")}


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
                OutlinedTextField(value = wifiUiState.ssid,
                    onValueChange = { vm.updateSsid(it)},
                    label = {Text("SSID")}
                )
                OutlinedTextField(value = wifiUiState.password,
                    onValueChange = { vm.updatePassword(it)},
                    label = {Text("PASSWORD")}
                )
                OutlinedTextField(value = wifiType,
                    onValueChange = { wifiType = it},
                    label = {Text("Wifi Type")}
                )

                Button(onClick = {Log.e(TAG, "WifiConnectionScreen: SSID = ${wifiUiState.ssid}, Password = ${wifiUiState.password}, Wifi Type = $wifiType" ) }) {
                    Text(text = "Blah")
                }

            }
        }
    }
}