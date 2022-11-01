package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                .fillMaxHeight()
                .background(Color.Green),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Column content
                Button(onClick = { }) {
                    Text(text = "Blah")
                }

            }
        }
    }
}