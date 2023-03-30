package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.viewmodels.UnableToConnectViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.*
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToConnectButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem

@Composable
fun UnableToConnectRoute(
    navController: NavController, vm: UnableToConnectViewModel = hiltViewModel()
) {
    val btUiState by vm.uiState.collectAsState()
    vm.setNavController(navController)
    if (btUiState.isBtEnabled) {
        UnableToConnectScreen(
            onBackButtonClicked = vm::navigateToNoDevicesConnectedScreen,
            onTryAgainClicked = vm::navigateToBtSearchingScreen,
            vm = vm
        )
    } else {
        NavigateBluetoothDisabled(navController = navController)
    }

}

@Composable
internal fun UnableToConnectScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onTryAgainClicked: () -> Unit,
    vm: UnableToConnectViewModel
) {
    Log.i("UnableToConnectScreen", "UnableToConnectScreen: ")
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, header, subtitle, help1, help2, button, footerButton, footerText) = createRefs()
            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButtonClicked,
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header1Text(text = stringResource(id = R.string.bt_unable_to_connect_header),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(header) {
                        top.linkTo(topBar.bottom, margin = 50.dp)
                        start.linkTo(parent.start)
                    })

            Subheader(text = stringResource(id = R.string.bt_unable_to_connect_subtitle),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(subtitle) {
                        top.linkTo(header.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })
            NumberedHelpItem(number = 1,
                text = stringResource(id = R.string.bt_unable_to_connect_1),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(help1) {
                        top.linkTo(subtitle.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            NumberedHelpItem(number = 2,
                text = stringResource(id = R.string.bt_unable_to_connect_2),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(help2) {
                        top.linkTo(help1.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            TextRectangularButton(
                onClick = onTryAgainClicked,
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(button) {
                        top.linkTo(help2.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.bt_unable_to_connect_button)
            )

            BodyText(text = stringResource(id = R.string.bt_unable_to_connect_footer),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(footerText) {
                        bottom.linkTo(footerButton.top, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            HowToConnectButton(onConnectClick = { /*TODO: Deeplink needed here*/ },
                modifier = modifier.constrainAs(footerButton) {
                    bottom.linkTo(parent.bottom, margin = 13.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })


        }
    }
}