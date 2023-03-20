package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.state.NoDevicesFoundUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesFoundViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Header2Text
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem


@Composable
fun NoDevicesFoundRoute(
    navController: NavController, vm: NoDevicesFoundViewModel = hiltViewModel()
) {
    val btUiState by vm.uiState.collectAsState()
    vm.setNavController(navController)
    if(btUiState.isBtEnabled){
        NoDevicesFoundScreen(
            onBackButtonClicked = vm::navigateToNoDevicesConnectedScreen,
            onTryAgainClicked = vm::navigateToSearchingScreen,
            uiState = btUiState,
            vm = vm
        )
    }else{
        NavigateBluetoothDisabled(navController = navController)
    }

    

}

@Composable
internal fun NoDevicesFoundScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onTryAgainClicked: () -> Unit,
    uiState: NoDevicesFoundUiState,
    vm: NoDevicesFoundViewModel
) {
    val TAG = "NoDevicesFoundScreen"
    Log.i(TAG, "NoDevicesFoundScreen: ")
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, header, subtitle, help1, help2, help3, button, footerButton) = createRefs()
            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButtonClicked,
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })

            Header1Text(text = stringResource(id = R.string.bt_no_devices_found_header),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(header) {
                        top.linkTo(topBar.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                    })

            Header2Text(text = stringResource(id = R.string.bt_no_devices_found_subtitle),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(subtitle) {
                        top.linkTo(header.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })
            NumberedHelpItem(number = 1,
                text = stringResource(id = R.string.bt_no_devices_found_1),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(help1) {
                        top.linkTo(subtitle.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            NumberedHelpItem(number = 2,
                text = stringResource(id = R.string.bt_no_devices_found_2),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(help2) {
                        top.linkTo(help1.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            NumberedHelpItem(number = 3,
                text = stringResource(id = R.string.bt_no_devices_found_3),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(help3) {
                        top.linkTo(help2.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            TextRectangularButton(onClick = onTryAgainClicked,
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(button) {
                        top.linkTo(help3.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.bt_no_devices_found_button)
            )

            CantFindDeviceButton(onHelpClick = { /*TODO: Deeplink needed here*/ },
                modifier = modifier.constrainAs(footerButton) {
                    bottom.linkTo(parent.bottom, margin = 13.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })


        }
    }
}

@Composable
fun NavigateNoDevicesFound(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate(BtConnectionScreens.NoDevicesFoundRoute.route)
    }
}