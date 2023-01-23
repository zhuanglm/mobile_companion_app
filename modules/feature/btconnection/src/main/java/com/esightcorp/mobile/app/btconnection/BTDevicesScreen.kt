package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectionViewModel
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.YellowDeviceCard
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton

@Composable
fun BtDevicesScreen(
    navController: NavController,
    vm: BtConnectionViewModel = hiltViewModel()
) {
//    BaseDevicesScreen(vm = vm, navController = navController)
    BtDevicesScreen(navController = navController) {

    }
}

@Composable
internal fun BtDevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackButtonPressed: () -> Unit,
) {
    /**
     * Dummy data
     */
    val dummyDeviceList = listOf<String>(
        "eSight-Go-3141592",
        "eSight-Go-2340923",
        "eSight-Go-3049534",
        "eSight-Go-2342345",
        "eSight-Go-87645675",
        "eSight-Go-3456736",
        "eSight-Go-8764756",
        "eSight-Go-2546765"
    )


    Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout {
            val (topBar, header, deviceContainer, help) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { onBackButtonPressed },
                onSettingsButtonInvoked = { /*Unused*/ Unit },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Header1Text(
                text = "Select your eSight",
                modifier = modifier
                    .padding(25.dp, 0.dp)
                    .constrainAs(header) {
                        top.linkTo(topBar.bottom, margin = 50.dp)
                        start.linkTo(parent.start)
                    })
            LazyColumn(modifier = modifier
                .constrainAs(deviceContainer) {
                    top.linkTo(header.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(help.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
                items(dummyDeviceList) { device ->
                    val deviceModel = device.substringBeforeLast('-')
                    val serialNumber = device.substringAfterLast('-')
                    YellowDeviceCard(
                        deviceModel = deviceModel,
                        serialNumber = serialNumber,
                        modifier = modifier.padding(12.dp)
                    )
                }

            }
            CantFindDeviceButton(modifier = modifier.padding(0.dp, 15.dp, 0.dp, 0.dp)
                .constrainAs(help){
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {}

        }
    }
}

@Composable
fun BaseDevicesScreen(
    vm: BtConnectionViewModel,
    navController: NavController
) {
    val btUiState by vm.uiState.collectAsState()
    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(Unit) {
            vm.refreshUiDeviceList()
        }
        if (btUiState.btConnectionStatus) {
            LaunchedEffect(Unit) {
                Log.d(TAG, "BaseDevicesScreen: ")
                navController.navigate("home_first/{${btUiState.getConnectedDevice}}")
            }
        } else {
            Column(verticalArrangement = SpaceEvenly, horizontalAlignment = CenterHorizontally) {
                if (btUiState.deviceMapCache.isEmpty()) {
                    CircularProgressIndicator(Modifier.fillMaxSize(0.5f))
                    Text(text = "Scanning...")
                } else {
                    btUiState.deviceMapCache.forEach { device ->
                        DeviceCard(device = device, vm = vm)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(
    device: String,
    vm: BtConnectionViewModel
) {
    Button(
        onClick = { vm.connectToDevice(device) },
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.8f)
    ) {
        ConstraintLayout(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val (icon, text) = createRefs()
            Image(
                Icons.Filled.Star,
                contentDescription = "eSight device",
                modifier = Modifier.constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 8.dp)
                })
            Text(text = device, modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(icon.end, margin = 8.dp)
            })
        }
    }
}
