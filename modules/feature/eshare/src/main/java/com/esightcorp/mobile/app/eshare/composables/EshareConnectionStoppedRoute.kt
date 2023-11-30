package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.state.EshareStoppedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectionStoppedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback


@Composable
fun EshareConnectionStoppedRoute(
    navController: NavController,
    reason: EShareStoppedReason? = null,
    vm: EshareConnectionStoppedViewModel = hiltViewModel(),
) {
    vm.updateState(reason)
    val uiState by vm.uiState.collectAsState()

    EshareConnectionStoppedScreen(
        uiState = uiState,
        navController = navController,
        onReturnPressed = vm::gotoMainScreen,
    )
}

//region Internal implementation
private const val TAG = "EshareConnectionStoppedRoute"

@Composable
internal fun EshareConnectionStoppedScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: EshareStoppedUiState,
    onReturnPressed: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    onBackButtonInvoked = { },
    onSettingsButtonInvoked = { },
    bottomButton = { },
) {
    BackStackLogger(navController, TAG)

    Column(
        modifier = modifier
            .padding(vertical = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(drawableId = R.drawable.ic_settings_disconnected)

        ItemSpacer(30.dp)
        Header1Text(
            text = stringResource(uiState.titleId),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(30.dp)
        Subheader(
            text = stringResource(uiState.descriptionId),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(60.dp)
        TextRectangularButton(
            onClick = { onReturnPressed?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.label_eshare_btn_return),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
internal fun ConnectionStoppedPreview() = MaterialTheme {
    EshareConnectionStoppedScreen(
        navController = rememberNavController(),
        uiState = EshareStoppedUiState(
            titleId = R.string.label_eshare_stopped_title,
            descriptionId = R.string.label_eshare_stopped_message
        ),
    )
}
//endregion
