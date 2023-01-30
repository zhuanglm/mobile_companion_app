package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.home.state.HomeUiState
import com.esightcorp.mobile.app.home.viewmodels.HomeViewModel
import com.esightcorp.mobile.app.ui.components.DeviceCard
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.IconAndTextSquareButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting

private const val TAG = "Home Screen"

@Composable
fun HomeFirstScreen(
    navController: NavController,
    device: String,
    vm: HomeViewModel = hiltViewModel()
) {
    Log.d("HomeFirstScreen", "HomeFirstScreen: $device")
    val homeUiState by vm.uiState.collectAsState()

    BaseHomeScreen(
        vm = vm,
        homeUiState = homeUiState,
        navController = navController,
        device = device,
        modifier = Modifier
    )
}

@Composable
internal fun BaseHomeScreen(
    vm: HomeViewModel,
    homeUiState: HomeUiState,
    navController: NavController,
    device: String = "0123456",
    modifier: Modifier = Modifier
) {
    vm.updateConnectedDevice(device)
    if (!homeUiState.isBluetoothConnected) {
        navigateToBtHomeScreen(navController = navController)
    } else {
        Surface(color = Color.Black ){
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (topBar, personalGreeting, deviceCard, appContainer, feedback) = createRefs()
                ESightTopAppBar(
                    showBackButton = false,
                    showSettingsButton = true,
                    onBackButtonInvoked = { /*Unused*/ Unit },
                    onSettingsButtonInvoked = { Unit },
                    modifier = modifier.constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                PersonalGreeting(
                    Modifier.constrainAs(personalGreeting) {
                        top.linkTo(topBar.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 32.dp)
                    },
                    connected = true
                )
                DeviceCard(modifier = modifier
                    .padding(25.dp, 0.dp)
                    .constrainAs(deviceCard) {
                        top.linkTo(personalGreeting.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                AppContainer(modifier = modifier.constrainAs(appContainer) {
                    top.linkTo(deviceCard.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(feedback.top)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }, navController)
                FeedbackButton(modifier = modifier.constrainAs(feedback){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Unit
                }
            }
        }
    }
}

@Composable
fun navigateToBtHomeScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate(BtConnectionScreens.BtConnectionHomeScreen.route)
    }
}

@Composable
fun AppContainer(
    modifier: Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()) {
            IconAndTextSquareButton(
                onClick = { Unit },
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .aspectRatio(1F)
                    .weight(1F),
                painter = painterResource(
                    id = com.esightcorp.mobile.app.ui.R.drawable.round_wifi_24
                ),
                text = "Connect to Wi-Fi"
            )
            IconAndTextSquareButton(
                onClick = { Unit },
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .aspectRatio(1F)
                    .weight(1F),
                painter = painterResource(
                    id = com.esightcorp.mobile.app.ui.R.drawable.round_qr_code_24
                ),
                text = "Wifi via QR"
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()) {
            IconAndTextSquareButton(
                onClick = { Unit },
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .aspectRatio(1F),
                painter = painterResource(
                    id = com.esightcorp.mobile.app.ui.R.drawable.baseline_camera_alt_24
                ),
                text = "Share your view"
            )
        }
    }
}



