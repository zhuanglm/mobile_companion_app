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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.viewmodels.UnableToConnectViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToConnectButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun UnableToConnectRoute(
    navController: NavController,
    vm: UnableToConnectViewModel = hiltViewModel(),
) {
    val btUiState by vm.uiState.collectAsState()
    if (btUiState.isBtEnabled) {
        UnableToConnectScreen(
            navController = navController,
            onBackButtonClicked = { navController.popBackStack() },
            onTryAgainClicked = vm::navigateToBtSearchingScreen,
            onConnectClicked = vm::showHowToConnectPage,
        )
    } else {
        NavigateBluetoothDisabled(navController = navController)
    }
}

//region Internal implementation
private const val TAG = "UnableToConnectScreen"

@Composable
private fun UnableToConnectScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackButtonClicked: OnActionCallback,
    onTryAgainClicked: OnNavigationCallback,
    onConnectClicked: OnActionCallback,
) {
    Log.i(TAG, "UnableToConnectScreen: ")
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, header, subtitle, help1, help2, button, footerButton, footerText) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButtonClicked,
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )

            Header1Text(
                text = stringResource(R.string.kTroubleshootingUnableToConnectTitle),
                modifier = modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .constrainAs(header) {
                        top.linkTo(topBar.bottom, margin = 50.dp)
                        start.linkTo(parent.start)
                    },
            )

            Subheader(
                text = stringResource(R.string.kTroubleshootingUnableToConnectSubtitle),
                modifier = modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .constrainAs(subtitle) {
                        top.linkTo(header.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    },
            )

            NumberedHelpItem(
                number = 1,
                text = stringResource(R.string.kTroubleshootingInstructionEsightWithinRangeShort),
                modifier = modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .constrainAs(help1) {
                        top.linkTo(subtitle.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )

            NumberedHelpItem(
                number = 2,
                text = stringResource(R.string.kTroubleshootingInstructionsSufficientChargeShort),
                modifier = modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .constrainAs(help2) {
                        top.linkTo(help1.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )

            TextRectangularButton(
                onClick = { onTryAgainClicked.invoke(navController) },
                modifier = modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .constrainAs(button) {
                        top.linkTo(help2.bottom, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(R.string.kTryAgainButtonTitle)
            )

            BodyText(
                text = stringResource(R.string.kBTTroubleshootingUnableToConnectDescriptionText),
                modifier = modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .padding(start = 25.dp, end = 25.dp)
                    .constrainAs(footerText) {
                        bottom.linkTo(footerButton.top, margin = 35.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )

            HowToConnectButton(
                onConnectClick = onConnectClicked,
                modifier = modifier.constrainAs(footerButton) {
                    bottom.linkTo(parent.bottom, margin = 13.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )
        }
    }
}

@Preview
@Composable
private fun UnableToConnectScreenPreview() = MaterialTheme {
    UnableToConnectScreen(
        navController = rememberNavController(),
        onBackButtonClicked = {},
        onTryAgainClicked = {},
        onConnectClicked = {},
    )
}
//endregion
