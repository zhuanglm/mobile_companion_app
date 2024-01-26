package com.esightcorp.mobile.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.settings.viewmodels.DisconnectConfirmationViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.utils.bluetooth.BleConnectionStatus

@Composable
fun DisconnectConfirmationRoute(
    navController: NavController,
    vwModel: DisconnectConfirmationViewModel = hiltViewModel(),
) {
    val uiState by vwModel.uiState.collectAsState()

    if (uiState.isConnectionDropped == true) {
        LaunchedEffect(Unit) { vwModel.onBleDisconnected(navController) }
        return
    }

    when (uiState.state) {
        BleConnectionStatus.Disconnecting -> LoadingScreenWithSpinner(
            loadingText = stringResource(R.string.kSettingDisconnectingSpinnerTitle),
            cancelButtonNeeded = false,
        )

        BleConnectionStatus.Disconnected -> LaunchedEffect(Unit) {
            // Disconnection has been executed successfully
            // Now navigate to the Not-Connected screen
            vwModel.navigateToDisconnectedScreen(navController)
        }

        else -> DisconnectConfirmationScreen(
            nav = navController,
            onCancelPressed = vwModel::navigateBack,
            onDisconnectPressed = { vwModel.disconnectToESight() },
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun DisconnectConfirmationScreenPreview() = MaterialTheme {
    DisconnectConfirmationScreen(
        nav = rememberNavController(),
        onCancelPressed = { },
        onDisconnectPressed = { },
    )
}

//region Internal impl
@Composable
internal fun DisconnectConfirmationScreen(
    modifier: Modifier = Modifier,
    nav: NavController,
    onCancelPressed: (NavController) -> Unit,
    onDisconnectPressed: (NavController) -> Unit,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    onBackButtonInvoked = { },
    onSettingsButtonInvoked = { },
    bottomButton = { },
) {
    Column(
        modifier = modifier.padding(vertical = 30.dp),
//            .debugBorder(Color.Red)
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BigIcon(
            drawableId = R.drawable.warning,
            contentDescription = "",
        )

        Header1Text(
            text = stringResource(R.string.kBTDisconnectTitle),
            modifier.padding(
                vertical = dimensionResource(
                    com.esightcorp.mobile.app.settings.R.dimen.settings_disconnect_title_margin
                )
            ),
            textAlign = TextAlign.Center
        )

        TextRectangularButton(
            onClick = { onCancelPressed.invoke(nav) },
            modifier = modifier,
            text = stringResource(R.string.kCancel),
            textAlign = TextAlign.Center
        )

        ItemSpacer(dimensionResource(com.esightcorp.mobile.app.settings.R.dimen.settings_section_spacer))
        OutlinedTextRectangularButton(
            onClick = { onDisconnectPressed.invoke(nav) },
            modifier = modifier,
            text = stringResource(R.string.kBTDiconnect),
            textAlign = TextAlign.Center,
            textColor = MaterialTheme.colors.onSurface,
        )
    }
}
//endregion
