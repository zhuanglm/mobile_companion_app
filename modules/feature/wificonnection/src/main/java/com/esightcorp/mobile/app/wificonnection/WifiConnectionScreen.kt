package com.esightcorp.mobile.app.wificonnection

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel

private const val TAG = "WifiConnectionScreen"

@Composable
fun WifiConnectionScreen(
    navController: NavController, vm: WifiConnectionViewModel = hiltViewModel()
) {
    val wifiUiState by vm.uiState.collectAsState()
    Log.d(TAG, "WifiConnectionScreen: ${wifiUiState.bluetoothConnected}")
    BaseWifiScreen(
        networks = wifiUiState.networkList, vm = vm, navController = navController
    )

}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun BaseWifiScreen(
    networks: List<ScanResult>, vm: WifiConnectionViewModel, navController: NavController
) {
    Surface(color = Color.Black) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (header, wifiList, spinner) = createRefs()
            Text(text = "Select Wifi Network",
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(header) {
                    top.linkTo(parent.top, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            if (networks.isNotEmpty()) {
                LazyColumn(modifier = Modifier
                    .constrainAs(wifiList) {
                        top.linkTo(header.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    items(networks.size) { index ->
                        WifiButton(
                            Modifier,
                            networks[index],
                            navController = navController,
                            updateCurrentSelectedNetwork = vm::updateCurrentSelectedNetwork
                        )
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(WifiConnectionScreens.SearchingForNetworkRoute.route)
                }

            }
        }
    }
}

@Composable
fun WifiButton(
    modifier: Modifier,
    network: ScanResult,
    navController: NavController,
    updateCurrentSelectedNetwork: (ScanResult) -> Unit
) {
    Log.i(TAG, "WifiButton: ${network.capabilities.toString()}")
    Button(
        onClick = {
            updateCurrentSelectedNetwork(network)
            navController.navigate("${WifiConnectionScreens.WifiCredentialsScreen.route}/{${network.SSID}}")
        },
        modifier = modifier
            .fillMaxWidth(0.8f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp)
    ) {
        ConstraintLayout(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val (icon, text) = createRefs()
            Text(text = network.SSID, modifier = Modifier.constrainAs(text) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
            //todo: check if the lock is actually valid for the network
            Image(Icons.Filled.Lock,
                contentDescription = "Network has a password on it",
                modifier = Modifier.constrainAs(icon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })
        }
    }
}

