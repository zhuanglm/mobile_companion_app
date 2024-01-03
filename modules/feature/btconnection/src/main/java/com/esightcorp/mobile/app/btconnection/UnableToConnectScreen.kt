package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
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
import com.esightcorp.mobile.app.btconnection.viewmodels.UnableToConnectViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.BodyText
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

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
            onConnectClicked = vm::showHowToConnectPage
        )
    } else {
        NavigateBluetoothDisabled(navController = navController)
    }
}

@Preview
@Composable
fun UnableToConnectScreenPreview() = MaterialTheme {
    UnableToConnectScreen(
        onBackButtonClicked = {},
        onTryAgainClicked = {},
        onConnectClicked = {},
    )
}

//region Internal implementation
@Composable
private fun UnableToConnectBody(modifier: Modifier,
                                onTryAgainClicked: OnActionCallback) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (topBar, header, subtitle, help1, help2, button) = createRefs()

        Header1Text(
            text = stringResource(id = R.string.kTroubleshootingUnableToConnectTitle),
            modifier = modifier
                .constrainAs(header) {
                    top.linkTo(topBar.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                },
        )

        Subheader(
            text = stringResource(id = R.string.kTroubleshootingUnableToConnectSubtitle),
            modifier = modifier
                .constrainAs(subtitle) {
                    top.linkTo(header.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
        )

        NumberedHelpItem(
            number = 1,
            text = stringResource(id = R.string.kTroubleshootingInstructionEsightWithinRangeShort),
            modifier = modifier
                .constrainAs(help1) {
                    top.linkTo(subtitle.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        NumberedHelpItem(
            number = 2,
            text = stringResource(id = R.string.kTroubleshootingInstructionsSufficientChargeShort),
            modifier = modifier
                .constrainAs(help2) {
                    top.linkTo(help1.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        TextRectangularButton(
            onClick = onTryAgainClicked,
            modifier = modifier
                .constrainAs(button) {
                    top.linkTo(help2.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = stringResource(id = R.string.kTryAgainButtonTitle)
        )
    }

}
//region Internal implementation
@Composable
internal fun UnableToConnectScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onTryAgainClicked: () -> Unit,
    onConnectClicked: () -> Unit
) {
    Log.i("UnableToConnectScreen", "UnableToConnectScreen: ")
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackButtonClicked.invoke() },
        bottomButton = {
            HowToConnectButton(
                modifier,
                onConnectClick = onConnectClicked,
            )
        },

        bottomAlignedContent = {
            BodyText(
                text = stringResource(R.string.kBTTroubleshootingUnableToConnectDescriptionText),
                modifier,
                MaterialTheme.colors.onSurface,
            )
        }

    ) {
        UnableToConnectBody(
            modifier = modifier,
            onTryAgainClicked = { onTryAgainClicked.invoke() },
        )
    }
}
//endregion
