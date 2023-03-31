package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.containers.HomeBaseScreen
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
    modifier: Modifier = Modifier, 
    onSettingsButtonInvoked: () -> Unit = { Log.e(TAG, "BaseHomeScreen: OnSettingsButtonInvoked", ) }
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
        HomeBaseScreen(
            modifier = modifier,
            showBackButton = false,
            showSettingsButton = true,
            onBackButtonInvoked = { /*Unused*/ },
            onSettingsButtonInvoked = onSettingsButtonInvoked,
            bottomButton = {
                FeedbackButton(modifier = modifier) {
                    Log.e(TAG, "BaseHomeScreen: On feedback pressed")
                }
            }) {
            HomeScreenBody(
                modifier = modifier,
                device = device,
                navController = navController,
                vm = vm
            )
        }
    }

   
}

@Composable
private fun HomeScreenBody(
    modifier: Modifier = Modifier,
    device: String = "0123456",
    navController: NavController,
    vm: HomeViewModel
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (personalGreeting, deviceCard, appContainer) = createRefs()
        PersonalGreeting(
            modifier = modifier.constrainAs(personalGreeting) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start)
            }, connected = true
        )
        DeviceCard(
            modifier = modifier
                .constrainAs(deviceCard) {
                    top.linkTo(personalGreeting.bottom, margin = 25.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onClick = { Unit },
            deviceModel = device.substringBeforeLast('-'),
            serialNumber = device.substringAfterLast('-')
        )

        SquareTileCardLayout(
            modifier = modifier.constrainAs(appContainer) {
                top.linkTo(deviceCard.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }, navController = navController, vm = vm
        )

    }
}

data class CardData(val text: String, val iconResId: Int, val onClick: () -> Unit)

@Composable
fun SquareTileCardLayout(
    modifier: Modifier = Modifier, vm: HomeViewModel, navController: NavController
) {
    val cards = listOf(CardData(
        "Connect to Wi-Fi", com.esightcorp.mobile.app.ui.R.drawable.round_wifi_24
    ) {
        vm.navigateToWifiCredsOverBt(navController)
    }, CardData(
        "Share your view", com.esightcorp.mobile.app.ui.R.drawable.baseline_camera_alt_24
    ) {
        vm.navigateToShareYourView(navController)
    }, CardData("Wifi via QR", com.esightcorp.mobile.app.ui.R.drawable.round_qr_code_24) {
        vm.navigateToWifiCredsQr(navController)
    })

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        itemsIndexed(cards) { _, card ->
            IconAndTextSquareButton(
                text = card.text,
                painter = painterResource(id = card.iconResId),
                onClick = card.onClick,
                modifier = modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)
            )
        }
    }
}




