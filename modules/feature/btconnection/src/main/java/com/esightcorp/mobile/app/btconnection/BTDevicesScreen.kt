package com.esightcorp.mobile.app.btconnection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BtDevicesUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtDevicesViewModel
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.YellowDeviceCard
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton

@Composable
fun BtDevicesRoute(
    navController: NavController,
    vm: BtDevicesViewModel = hiltViewModel()
) {
    vm.getDeviceList()
    val uiState by vm.uiState.collectAsState()
    BtDevicesScreen(
        navController = navController,
        onBackButtonPressed = {
            vm.navigateToNoDeviceConnectedScreen(navController)
        },
        uiState = uiState
    )
}

@Composable
internal fun BtDevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackButtonPressed: () -> Unit,
    uiState: BtDevicesUiState,
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
                items(uiState.listOfAvailableDevices) { device ->
                    if(device.contains('-')){
                        val deviceModel = device.substringBeforeLast('-')
                        val serialNumber = device.substringAfterLast('-')
                        YellowDeviceCard(
                            deviceModel = deviceModel,
                            serialNumber = serialNumber,
                            modifier = modifier.padding(12.dp)
                        )
                    }else{
                        val serialNumber = "00001"
                        YellowDeviceCard(
                            deviceModel = device,
                            serialNumber = serialNumber,
                            modifier = modifier.padding(12.dp)
                        )
                    }

                }

            }
            CantFindDeviceButton(modifier = modifier
                .padding(0.dp, 15.dp, 0.dp, 0.dp)
                .constrainAs(help) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {}

        }
    }
}