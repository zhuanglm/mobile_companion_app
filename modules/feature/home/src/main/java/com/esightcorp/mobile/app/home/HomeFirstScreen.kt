package com.esightcorp.mobile.app.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
    navController: NavController, vm: HomeViewModel = hiltViewModel()
) {
    val homeUiState by vm.uiState.collectAsState()

    BaseHomeScreen(
        vm = vm,
        homeUiState = homeUiState,
        navController = navController,
        device = homeUiState.connectedDevice,
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
    if (!homeUiState.isBluetoothConnected && homeUiState.isBluetoothEnabled) {
        LaunchedEffect(Unit) {
            vm.navigateToBluetoothStart(navController)
        }
    } else if (!homeUiState.isBluetoothEnabled) {
        LaunchedEffect(Unit) {
            vm.navigateToBluetoothDisabled(navController)
        }
    } else {
        Surface(color = MaterialTheme.colors.surface) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (topBar, personalGreeting, deviceCard, appContainer, feedback) = createRefs()
                ESightTopAppBar(showBackButton = false,
                    showSettingsButton = true,
                    onBackButtonInvoked = { /*Unused*/ Unit },
                    onSettingsButtonInvoked = { Unit },
                    modifier = modifier.constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                PersonalGreeting(
                    Modifier.constrainAs(personalGreeting) {
                        top.linkTo(topBar.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 32.dp)
                    }, connected = true
                )
                DeviceCard(modifier = modifier
                    .padding(25.dp, 0.dp)
                    .constrainAs(deviceCard) {
                        top.linkTo(personalGreeting.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = { Unit },
                    deviceModel = device.substringBeforeLast('-'),
                    serialNumber = device.substringAfterLast('-')
                )
                AppContainer(modifier = modifier.constrainAs(appContainer) {
                    top.linkTo(deviceCard.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(feedback.top)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }, navController, vm = vm)
                FeedbackButton(modifier = modifier.constrainAs(feedback) {
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
fun AppContainer(
    modifier: Modifier, navController: NavController, vm: HomeViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            IconAndTextSquareButton(
                onClick = {
                    vm.navigateToWifiCredsOverBt(navController)
                },
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
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



