package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectionViewModel

@Composable
fun BtDevicesScreen(
    navController: NavController, 
    vm: BtConnectionViewModel = hiltViewModel()){
    BaseDevicesScreen(vm = vm, navController = navController)
}

@Composable
fun BaseDevicesScreen(vm: BtConnectionViewModel,
    navController: NavController){
    val btUiState by vm.uiState.collectAsState()
    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(Unit){
            vm.refreshUiDeviceList()
        }
        if(btUiState.btConnectionStatus){
            LaunchedEffect(Unit){
                Log.d(TAG, "BaseDevicesScreen: ")
                navController.navigate("home_first/{${btUiState.getConnectedDevice}}")
            }
        }else{
            Column(verticalArrangement = SpaceEvenly, horizontalAlignment = CenterHorizontally) {
                if(btUiState.deviceMapCache.isEmpty()){
                    CircularProgressIndicator(Modifier.fillMaxSize(0.5f))
                    Text(text = "Scanning...")
                }else{
                    btUiState.deviceMapCache.forEach { device ->
                        DeviceCard(device = device, vm = vm)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: String,
vm: BtConnectionViewModel){
    Button(onClick = {vm.connectToDevice(device)},
    modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth(0.8f)) {
        ConstraintLayout(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()) {
            val (icon, text) = createRefs()
            Image(Icons.Filled.Star, contentDescription = "eSight device", modifier = Modifier.constrainAs(icon){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, margin = 8.dp)
            })
            Text(text = device, modifier = Modifier.constrainAs(text){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(icon.end, margin = 8.dp)
            })
        }
    }
}
