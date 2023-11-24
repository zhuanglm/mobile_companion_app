package com.esightcorp.mobile.app.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.settings.viewmodels.SettingsViewModel
import com.esightcorp.mobile.app.ui.components.BodyText
import com.esightcorp.mobile.app.ui.components.FineText
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.IconAndTextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen

@Composable
fun SettingsScreen(navController: NavController, vwModel: SettingsViewModel = hiltViewModel()) =
    SettingsScreenBody(navController = navController, vwModel = vwModel)

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() = MaterialTheme {
    SettingsScreenBody(
        modifier = Modifier,
        navController = rememberNavController(),
    )
}

//region Internal impl
@Composable
internal fun SettingsScreenBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    vwModel: SettingsViewModel? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    showSettingsButton = false,
    onBackButtonInvoked = { navController.popBackStack() },
    onSettingsButtonInvoked = { },
    bottomButton = { },
) {
    Column {
        Header1Text(stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings), modifier)

        val spacer = dimensionResource(R.dimen.settings_section_spacer)

        ItemSpacer(spacer)
        SettingsMyESight(modifier, navController, vwModel)

        ItemSpacer(spacer)
        SettingsHelp(modifier, onLinkButtonClicked = { id -> vwModel?.showExternalUrl(id) })

        ItemSpacer(spacer)
        SettingsAbout(modifier, onLinkButtonClicked = { id -> vwModel?.showExternalUrl(id) })

        val uiState = vwModel?.settingsUiState?.collectAsState()?.value
        uiState?.appVersion?.let {
            ItemSpacer(spacer)
            SettingsVersion(modifier, it)
        }
    }
}

@Composable
internal fun SettingsMyESight(
    modifier: Modifier = Modifier,
    navController: NavController,
    vwModel: SettingsViewModel? = null,
) = Column {
    BodyText(
        stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_my_esight),
        modifier.padding(vertical = dimensionResource(R.dimen.settings_subtitle_padding)),
        MaterialTheme.colors.onSurface,
    )

    IconAndTextRectangularButton(
        onClick = { navController.navigate("searching_for_networks/qr") },
        modifier = modifier,
        icon = ImageVector.vectorResource(com.esightcorp.mobile.app.ui.R.drawable.round_qr_code_24),
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_btn_create_wifi_code)
    )

    val settingState = vwModel?.settingsUiState?.collectAsState()?.value
    if (settingState?.isConnected == true) {
        ItemSpacer()
        IconAndTextRectangularButton(
            onClick = { vwModel.navigateToDisconnect(navController) },
            modifier,
            iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_disconnected_24,
            text = stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_btn_disconnect_esight),
        )
    }
}

@Composable
internal fun SettingsHelp(
    modifier: Modifier = Modifier,
    onLinkButtonClicked: (Int) -> Unit,
) = Column {
    BodyText(
        stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_help),
        modifier.padding(vertical = dimensionResource(R.dimen.settings_subtitle_padding)),
        MaterialTheme.colors.onSurface,
    )

    IconAndTextRectangularButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_home) },
        modifier = modifier,
        icon = ImageVector.vectorResource(com.esightcorp.mobile.app.ui.R.drawable.round_question_mark_24),
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_btn_tutorials)
    )

    ItemSpacer()
    IconAndTextRectangularButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_feedback) },
        modifier,
        icon = ImageVector.vectorResource(com.esightcorp.mobile.app.ui.R.drawable.round_chat_bubble_outline_24),
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_btn_feedback)
    )
}

@Composable
internal fun SettingsAbout(
    modifier: Modifier = Modifier,
    onLinkButtonClicked: (Int) -> Unit,
) = Column {
    BodyText(
        stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_about),
        modifier.padding(vertical = dimensionResource(R.dimen.settings_subtitle_padding)),
        MaterialTheme.colors.onSurface,
    )

    IconAndTextRectangularButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_home) },
        modifier = modifier,
        iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_website_24,
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_btn_visit_website)
    )

    ItemSpacer()
    IconAndTextRectangularButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_privacy_policy) },
        modifier,
        iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_privacy_24,
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.label_settings_btn_privacy_policy)
    )
}

@Composable
internal fun SettingsVersion(modifier: Modifier = Modifier, version: String) = FineText(
    text = stringResource(
        com.esightcorp.mobile.app.ui.R.string.label_settings_version_format, version
    ),
    modifier = modifier.fillMaxWidth(),
    textAlign = TextAlign.Center,
    color = MaterialTheme.colors.onSurface
)

@Composable
internal fun ItemSpacer(space: Dp = dimensionResource(R.dimen.settings_item_spacer)) =
    Spacer(modifier = Modifier.height(space))
//endregion
