package com.esightcorp.mobile.app.wificonnection

import android.net.wifi.ScanResult
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel

private const val TAG = "WifiConnectionScreen"

@Composable
fun WifiConnectionScreen(
    navController: NavController,
    vm : WifiConnectionViewModel = hiltViewModel()){
    val wifiUiState by vm.uiState.collectAsState()
    Log.d(TAG, "WifiConnectionScreen: ${wifiUiState.bluetoothConnected}")
    BaseWifiScreen(
        networks = wifiUiState.networkList,
        vm = vm,
        navController = navController)

}

/*@Composable
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
        }
    }
}*/


@OptIn(ExperimentalUnitApi::class)
@Composable
fun BaseWifiScreen(
    networks: List<ScanResult>,
    vm: WifiConnectionViewModel,
    navController: NavController){
    Surface(color = Color.Black) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (header, wifiList, spinner) = createRefs()
            Text(text = "Select Wifi Network",
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(header){
                    top.linkTo(parent.top, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            if(networks.isNotEmpty()){
                LazyColumn(modifier = Modifier
                    .constrainAs(wifiList) {
                        top.linkTo(header.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    items(networks.size){ index ->
                        WifiButton(Modifier, networks[index], navController = navController, updateCurrentSelectedNetwork = vm::updateCurrentSelectedNetwork )
                    }
                }
            }else{
                LaunchedEffect(Unit ){
                    navController.navigate(WifiConnectionScreens.SearchingForNetworkRoute.route)
                }

            }
        }
    }
}

@Composable
fun WifiButton(modifier: Modifier,
               network: ScanResult,
               navController: NavController,
               updateCurrentSelectedNetwork: (ScanResult) -> Unit){
    Button(onClick = {
        updateCurrentSelectedNetwork(network)
        navController.navigate("${WifiConnectionScreens.WifiCredentialsScreen.route}/{${network.SSID}}")},
        modifier = modifier
            .fillMaxWidth(0.8f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp)
    ) {
        ConstraintLayout(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()) {
            val (icon, text) = createRefs()
            Text(text = network.SSID, modifier = Modifier.constrainAs(text){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
            //todo: check if the lock is actually valid for the network
            Image(Icons.Filled.Lock, contentDescription = "Network has a password on it", modifier = Modifier.constrainAs(icon){
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}

