package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.SetupHotspotButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun EshareWifiDisabledRoute(
    navController: NavController,
    vwModel: EshareViewModel = hiltViewModel()
) = EshareWifiDisabledScreen(
    navController = navController,
    onRetryPressed = vwModel::onRetryPressed,
    onBackPressed = vwModel::gotoMainScreen,
    onSetupHotspotPressed = vwModel::onSetupHotspotPressed
)

//region Internal implementation

@Composable
internal fun EshareWifiDisabledScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onRetryPressed: OnNavigationCallback? = null,
    onBackPressed: OnNavigationCallback? = null,
    onSetupHotspotPressed: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    showSettingsButton = false,
    onBackButtonInvoked = { onBackPressed?.invoke(navController) },
    onSettingsButtonInvoked = { },
    bottomButton = { SetupHotspotButton { onSetupHotspotPressed?.invoke(navController) } },
) {
    Column(
        modifier = modifier.padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BigIcon(drawableId = R.drawable.round_wifi_24)

        ItemSpacer(30.dp)
        Header1Text(
            text = stringResource(R.string.label_eshare_wifi_disabled_title),
            modifier = modifier,
            textAlign = TextAlign.Center,
        )

        ItemSpacer(30.dp)
        Subheader(
            text = stringResource(R.string.label_eshare_wifi_disabled_instruction),
            modifier = modifier,
        )

        ItemSpacer(60.dp)
        TextRectangularButton(
            onClick = { onRetryPressed?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.label_eshare_btn_retry),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
internal fun Preview() = MaterialTheme {
    EshareWifiDisabledScreen(navController = rememberNavController())
}

//endregion
