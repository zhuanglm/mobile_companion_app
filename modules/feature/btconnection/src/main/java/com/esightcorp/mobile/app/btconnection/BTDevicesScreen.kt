package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BtDevicesUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtDevicesViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.YellowDeviceCard
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton

@Composable
fun BtDevicesRoute(
    navController: NavController, vm: BtDevicesViewModel = hiltViewModel()
) {
    vm.getDeviceList()
    val uiState by vm.uiState.collectAsState()
    if(!uiState.isBtEnabled){
        NavigateBluetoothDisabled(navController = navController)
    }else if (uiState.listOfAvailableDevices.isEmpty()){
        NavigateNoDevicesFound(navController = navController)
    }else{
        BtDevicesScreen(
            navController = navController, uiState = uiState, vm = vm
        )
    }

}

@Composable
internal fun BtDevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    uiState: BtDevicesUiState,
    vm: BtDevicesViewModel
) {
    /*
     * Dummy data
     */
    val dummyDeviceList = listOf(
        "eSight-Go-3141592",
        "eSight-Go-2340923",
        "eSight-Go-3049534",
        "eSight-Go-2342345",
        "eSight-Go-87645675",
        "eSight-Go-3456736",
        "eSight-Go-8764756",
        "eSight-Go-2546765"
    )
    val TAG = "BtDevicesScreen"

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout {
            val (topBar, header, deviceContainer, help) = createRefs()
            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { vm.navigateToNoDeviceConnectedScreen(navController = navController) },
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            /*
            Have to bring the margins in as vals since the margin function in .constrainAs
            does not accept a @Composable function, but does accept a value
             */
            val headerMargin = dimensionResource(id = R.dimen.bt_devices_header_margin)
            val lazyColTopMargin = dimensionResource(id = R.dimen.lazy_col_top_margin)

            Header1Text(text = stringResource(id = R.string.select_your_esight),
                modifier = modifier
                    .padding(
                        dimensionResource(id = R.dimen.header_horizontal_padding),
                        dimensionResource(
                            id = R.dimen.header_vertical_padding
                        )
                    )
                    .constrainAs(header) {
                        top.linkTo(
                            topBar.bottom, margin = headerMargin
                        )
                        start.linkTo(parent.start)
                    })
            LazyColumn(modifier = modifier.constrainAs(deviceContainer) {
                    top.linkTo(header.bottom, margin = lazyColTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(help.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
                items(uiState.listOfAvailableDevices) { device ->
                    if (device.contains(stringResource(id = R.string.hyphen))) {
                        val deviceModel =
                            device.substringBeforeLast(stringResource(id = R.string.hyphen))
                        val serialNumber =
                            device.substringAfterLast(stringResource(id = R.string.hyphen))
                        YellowDeviceCard(deviceModel = deviceModel,
                            serialNumber = serialNumber,
                            modifier = modifier.padding(dimensionResource(id = R.dimen.yellow_device_card_padding)),
                            onClick = {
                                Log.i(TAG, "$device was selected. Trying to connect...")
                                vm.navigateToBtConnectingScreen(navController, device)
                            })
                    } else {
                        val serialNumber = stringResource(id = R.string.default_serial_number)
                        YellowDeviceCard(deviceModel = device,
                            serialNumber = serialNumber,
                            modifier = modifier.padding(dimensionResource(id = R.dimen.yellow_device_card_padding)),
                            onClick = { Log.e(TAG, "This device is not an eSight device") })
                    }

                }

            }
            CantFindDeviceButton(modifier = modifier
                .padding(
                    dimensionResource(id = R.dimen.zero),
                    dimensionResource(id = R.dimen.bottom_button_top_padding),
                    dimensionResource(id = R.dimen.zero),
                    dimensionResource(id = R.dimen.zero)
                )
                .constrainAs(help) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {}

        }
    }
}



