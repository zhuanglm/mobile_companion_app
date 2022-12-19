package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.home.state.HomeUiState
import com.esightcorp.mobile.app.home.viewmodels.HomeViewModel
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import java.time.LocalDateTime

private const val TAG = "Home Screen"

@Composable
fun HomeFirstScreen(
    navController: NavController,
    device: String,
    vm: HomeViewModel = hiltViewModel()){
    Log.d("HomeFirstScreen", "HomeFirstScreen: $device")
    val homeUiState by vm.uiState.collectAsState()

    BaseHomeScreen(vm = vm, homeUiState = homeUiState, navController = navController, device = device)
//    Column(modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Row(modifier = Modifier.fillMaxWidth(.80f),
//            horizontalArrangement = Arrangement.SpaceEvenly) {
//            Column(modifier = Modifier.wrapContentHeight()) {
//                Text(text = "Good evening ## TIME", style = MaterialTheme.typography.h2)
//                Text(
//                    text = "You are not connected to an eSight #### BLUETOOTH PERMISSIONS",
//                    style = MaterialTheme.typography.caption
//                )
//            }
//        }
//        Button(onClick = { navController.navigate(BtConnectionScreens.IncomingNavigationRoute.route) },
//            Modifier
//                .padding(20.dp, 5.dp)
//                .fillMaxWidth(0.75f)
//        ) {
//            Text(text = "Connect to a bluetooth device")
//        }
//
//
//        Button(onClick = { navController.navigate(WifiConnectionScreens.IncomingNavigationRoute.route) }) {
//            Text(text = "wifi")
//        }
//
//        Button(onClick = { navController.navigate(EshareScreens.IncomingNavigationRoute.route) }) {
//            Text(text = "eshare")
//        }
//
//    }
}
@Composable
fun BaseHomeScreen(vm: HomeViewModel,
                   homeUiState: HomeUiState,
                   navController: NavController,
                   device: String){
    vm.updateConnectedDevice(device)
    if(!homeUiState.isBluetoothConnected){
        navigateToBtHomeScreen(navController = navController)
    }else{
        Surface(color = Color(0x004c4c)) {
            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
            ) {
                val (settingsRow, personalGreeting, deviceCard, appContainer) = createRefs()
                SettingsRow(Modifier.constrainAs(settingsRow){
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 32.dp)
                }, navController)
                PersonalGreeting(Modifier.constrainAs(personalGreeting){
                    top.linkTo(settingsRow.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 32.dp)
                }, homeUiState)
                ConnectedToBar(homeUiState = homeUiState, modifier = Modifier.constrainAs(deviceCard){
                    top.linkTo(personalGreeting.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 32.dp)
                })
                AppContainer(modifier = Modifier.constrainAs(appContainer){
                    top.linkTo(deviceCard.bottom, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, navController )
            }
        }
    }
}

@Composable 
fun navigateToBtHomeScreen(navController: NavController){
    LaunchedEffect(Unit){
        navController.navigate(BtConnectionScreens.BtConnectionHomeScreen.route)
    }
}

@Composable
fun SettingsRow(modifier: Modifier,
    navController: NavController) {
    IconButton(onClick = { navController.navigate(BtConnectionScreens.BtConnectionHomeScreen.route) }, modifier) {
        Icon(Icons.Filled.Settings,"Settings")
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun PersonalGreeting(modifier: Modifier,
                     homeUiState: HomeUiState){
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
            Text(text = "You are connected to",
                modifier = Modifier.constrainAs(connectionStatus){
                    top.linkTo(greeting.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

        }
    }

}
@Composable
fun ConnectedToBar(
    homeUiState: HomeUiState,
    modifier: Modifier,
){
    Card(modifier = modifier
        .fillMaxWidth(0.8f)
        .wrapContentHeight(),
        backgroundColor = Color.Yellow) {
        ConstraintLayout(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()) {
            val (icon, text) = createRefs()

            Image(Icons.Filled.Star,
                contentDescription = "eSight device you are connected to",
                modifier = Modifier.constrainAs(icon){
                    top.linkTo(parent.top, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                })
            Text(text = homeUiState.connectedDevice,
                color = Color.Black,
                modifier = Modifier.constrainAs(text){
                    top.linkTo(parent.top, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    start.linkTo(icon.end, margin = 8.dp)
            })
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun AppContainer(modifier : Modifier,
navController: NavController){
    ConstraintLayout(modifier = modifier.wrapContentHeight().fillMaxWidth(0.8f)) {
     val (header, wifiOverBt, wifiQr, eShare) = createRefs()
        Text(text = "Apps",
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            modifier = Modifier.constrainAs(header){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            })
        Button(onClick = {  navController.navigate(WifiConnectionScreens.IncomingNavigationRoute.route )}, modifier = Modifier.constrainAs(wifiOverBt) {
            start.linkTo(parent.start)
            top.linkTo(header.bottom, margin = 16.dp)
        }.fillMaxWidth(0.5f)) {
            ConstraintLayout {
                val (icon, text) = createRefs()
                Image(Icons.Filled.AccountBox, contentDescription = "Icon", modifier = Modifier.constrainAs(icon){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                })
                Text(text = "Wifi over Bt", modifier = Modifier.constrainAs(text) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(icon.bottom)
                })
            }
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(wifiQr) {
            start.linkTo(wifiOverBt.end, margin = 8.dp)
            top.linkTo(header.bottom, margin = 16.dp)
        }.fillMaxWidth(0.5f)) {
            ConstraintLayout {
                val (icon, text) = createRefs()
                Image(Icons.Filled.Build, contentDescription = "Icon", modifier = Modifier.constrainAs(icon){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                })
                Text(text = "Wifi QR", modifier = Modifier.constrainAs(text) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(icon.bottom)
                })
            }
        }
    }
}



