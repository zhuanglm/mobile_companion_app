package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.UnableToConnectUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.UnableToConnectViewModel
import com.esightcorp.mobile.app.ui.components.*
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.R

@Composable
fun UnableToConnectRoute(
    navController: NavController,
    vm: UnableToConnectViewModel = hiltViewModel()
){
    val btUiState by vm.uiState.collectAsState()
    UnableToConnectScreen(
        onBackButtonClicked = { /*TODO*/ },
        navController = navController ,
        uiState = btUiState,
        vm = vm
    )
}

@Composable
internal fun UnableToConnectScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    navController: NavController,
    uiState: UnableToConnectUiState,
    vm: UnableToConnectViewModel
){
    Log.i("UnableToConnectScreen", "UnableToConnectScreen: ")
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, header, subtitle, help1, help2, button, footerButton, footerText) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButtonClicked,
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Header1Text(text = stringResource(id = R.string.bt_unable_to_connect_header), modifier = modifier.constrainAs(header) {
                top.linkTo(topBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Header2Text(
                text = stringResource(id = R.string.bt_unable_to_connect_subtitle),
                modifier = modifier.constrainAs(subtitle) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            NumberedHelpItem(
                number = 1,
                text = stringResource(id =R.string.bt_unable_to_connect_1),
                modifier = modifier.constrainAs(help1) {
                    top.linkTo(subtitle.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            NumberedHelpItem(
                number = 2,
                text = stringResource(id = R.string.bt_unable_to_connect_2),
                modifier = modifier.constrainAs(help2) {
                    top.linkTo(help1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(button) {
                top.linkTo(help2.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, text = stringResource(id = R.string.bt_unable_to_connect_button))
            
            BodyText(text = stringResource(id = R.string.bt_unable_to_connect_footer), modifier = modifier.constrainAs(footerText) {
                bottom.linkTo(footerButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            CantFindDeviceButton(
                onHelpClick = { /*TODO: Deeplink needed here*/ },
                modifier = modifier.constrainAs(footerButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })


        }
    }
}