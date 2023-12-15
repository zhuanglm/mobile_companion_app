package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.wificonnection.state.UnableToConnectUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.UnableToConnectViewModel

@Composable
fun UnableToConnectRoute(
    navController: NavController, vm: UnableToConnectViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    vm.setNavController(navController)
    UnableToConnectScreen(
        navController = navController,
        vm = vm,
        uiState = uiState,
        onBackPressed = vm::onBackPressed,
        onTryAgain = vm::onTryAgain)
}

@Composable
internal fun UnableToConnectScreen(
    navController: NavController,
    vm: UnableToConnectViewModel,
    modifier: Modifier = Modifier,
    uiState: UnableToConnectUiState,
    onBackPressed: () -> Unit,
    onTryAgain: () -> Unit
) {
    val TAG = "UnableToConnectScreen"


    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false ,
        onBackButtonInvoked = onBackPressed,
        onSettingsButtonInvoked = { /*Unused*/ },
        bottomButton = {
                       HowToConnectButton(){
                           navController.navigate("how_to_connect")
                       }
        },
        everythingElse = {
            ScreenBody (
                modifier = modifier,
                onTryAgain = onTryAgain
            )
        })
}

@Composable
private fun ScreenBody(
    modifier: Modifier,
    onTryAgain: () -> Unit
){
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val ( headerText, header2Text, nhiOne, nhiTwo, nhiThree, helpText, button) = createRefs()
        Header1Text(
            text = stringResource(id = R.string.kTroubleshootingUnableToConnectTitle),
            modifier = modifier.constrainAs(headerText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 16.dp)
            })

        Subheader(text = stringResource(id = R.string.kTroubleShootingTryFollowingSteps),
            modifier = modifier.constrainAs(header2Text) {
                top.linkTo(headerText.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            })

        NumberedHelpItem(number = 1,
            text = stringResource(id = R.string.kTroubleshootingInstructionWiFiPassword),
            modifier = modifier.constrainAs(nhiOne) {
                top.linkTo(header2Text.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })

        NumberedHelpItem(number = 2,
            text = stringResource(id = R.string.kTroubleshootingInstructionEsightWithinRangeShort),
            modifier = modifier.constrainAs(nhiTwo) {
                top.linkTo(nhiOne.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })

        NumberedHelpItem(number = 3,
            text = stringResource(id = R.string.kTroubleshootingInstructionsSufficientChargeShort),
            modifier = modifier.constrainAs(nhiThree) {
                top.linkTo(nhiTwo.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })
        TextRectangularButton(onClick = onTryAgain, modifier = modifier.constrainAs(button) {
            top.linkTo(nhiThree.bottom, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }, text = stringResource(id = R.string.kTryAgainButtonTitle))

        BodyText(text = stringResource(id =R.string.kWifiTroubleshootingUnableToConnectFooter),
            modifier = modifier.constrainAs(helpText) {
                top.linkTo(button.bottom, margin = 16.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

    }
}