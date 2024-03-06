/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.settings

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.settings.viewmodels.SettingsViewModel
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.LeadingIconTextButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.FineText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.findActivity
import com.esightcorp.mobile.app.utils.permission.PermissionUiState

@Composable
fun SettingsScreen(
    navController: NavController,
    vwModel: SettingsViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)

    val uiState = vwModel.settingsUiState.collectAsState().value
    if (uiState.connState?.isConnectionDropped == true) {
        LaunchedEffect(Unit) { vwModel.onBleDisconnected(navController) }
        return
    }

    SettingsScreenBody(navController = navController, vwModel = vwModel)
}

//region Internal impl
@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() = MaterialTheme {
    SettingsScreenBody(
        modifier = Modifier,
        navController = rememberNavController(),
    )
}

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
        Header1Text(
            stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewTitleText),
            modifier,
        )

        val spacer = dimensionResource(R.dimen.settings_section_spacer)

        ItemSpacer(8.dp)
        SettingsMyESight(modifier, navController, vwModel)

        ItemSpacer(spacer)
        SettingsHelp(modifier, onLinkButtonClicked = { id -> vwModel?.showExternalUrl(id) })

        ItemSpacer(spacer)
        SettingsAbout(modifier, onLinkButtonClicked = { id -> vwModel?.showExternalUrl(id) })

        val uiState = vwModel?.settingsUiState?.collectAsState()?.value
        uiState?.appVersion?.let {
            ItemSpacer(41.dp)
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
        stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewMyeSightSubTitleText),
        modifier,
        MaterialTheme.colors.onSurface,
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { vwModel?.onPermissionsUpdated(it) }
    )
    val context = LocalContext.current

    ExecuteOnce { vwModel?.registerPermissionLauncher(permissionLauncher, context.findActivity()) }
    var isWiFiQrFlow by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val spacer = dimensionResource(R.dimen.settings_section_spacer)
    ItemSpacer(spacer)

    LeadingIconTextButton(
        onClick = {
            isWiFiQrFlow = true
            vwModel?.initPermissionCheck()
            vwModel?.verifyLocationServiceState()
        },
        modifier = modifier,
        iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_qr_40,
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.kCreateWifiCodeButtonText)
    )

    val settingState = vwModel?.settingsUiState?.collectAsState()?.value
    if (settingState?.connState?.isConnected == true) {
        ItemSpacer(spacer)
        LeadingIconTextButton(
            onClick = { vwModel.navigateToDisconnectConfirmation(navController) },
            modifier,
            iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_disconnected_40,
            text = stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewDisconnectButtonText),
        )
    }

    val permissionUiState = vwModel?.permissionUiState?.collectAsState()?.value
    Log.i(
        TAG,
        "WiFi (Location) permission state: ${permissionUiState?.state}\nisWiFiQrFlow: $isWiFiQrFlow"
    )

    when (isWiFiQrFlow) {
        true -> {
            when (permissionUiState?.state) {
                PermissionUiState.PermissionState.GRANTED -> {
                    val isLocationServiceEnabled by vwModel.isLocationServiceEnabled.collectAsState()
                    Log.i(TAG, "Location service enabled: $isLocationServiceEnabled")

                    when (isLocationServiceEnabled) {
                        true -> {
                            ExecuteOnce {
                                navController.navigate(
                                    target = WifiNavigation.ScanningRoute,
                                    param = WifiNavigation.ScanningRoute.PARAM_QR,
                                    popUntil = WifiNavigation.IncomingRoute,
                                )
                            }
                            isWiFiQrFlow = null
                        }

                        false -> {
                            ExecuteOnce { vwModel.navigateToLocationServiceOff(navController) }
                            isWiFiQrFlow = null
                        }

                        else -> Unit
                    }
                }

                PermissionUiState.PermissionState.SHOW_RATIONALE -> {
                    ExecuteOnce {
                        navController.navigate(
                            target = HomeNavigation.LocationPermissionRoute,
                            popCurrent = false
                        )
                    }
                    isWiFiQrFlow = null
                }

                else -> Unit
            }
        }

        else -> Unit
    }

    DisposableEffect(Unit) { onDispose { vwModel?.cleanUp() } }
}

@Composable
internal fun SettingsHelp(
    modifier: Modifier = Modifier,
    onLinkButtonClicked: (Int) -> Unit,
) = Column {
    BodyText(
        stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewHelpSubTitleText),
        modifier,
        MaterialTheme.colors.onSurface,
    )

    val spacer = dimensionResource(R.dimen.settings_section_spacer)
    ItemSpacer(spacer)

    LeadingIconTextButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_home) },
        modifier = modifier,
        icon = ImageVector.vectorResource(com.esightcorp.mobile.app.ui.R.drawable.round_question_mark_24),
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewTutorialButtonText)
    )

    ItemSpacer(spacer)
    LeadingIconTextButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_feedback) },
        modifier,
        icon = ImageVector.vectorResource(com.esightcorp.mobile.app.ui.R.drawable.ic_feedback_40),
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.kFeedbackButtonText)
    )
}

@Composable
internal fun SettingsAbout(
    modifier: Modifier = Modifier,
    onLinkButtonClicked: (Int) -> Unit,
) = Column {
    BodyText(
        stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewAbouteSightSubTitleText),
        modifier,
        MaterialTheme.colors.onSurface,
    )

    val spacer = dimensionResource(R.dimen.settings_section_spacer)
    ItemSpacer(spacer)

    LeadingIconTextButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_home) },
        modifier = modifier,
        iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_website_40,
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.kSettingsViewVisitWebsiteButtonText)
    )

    ItemSpacer(spacer)
    LeadingIconTextButton(
        onClick = { onLinkButtonClicked.invoke(com.esightcorp.mobile.app.ui.R.string.url_esight_privacy_policy) },
        modifier,
        iconDrawableId = com.esightcorp.mobile.app.ui.R.drawable.ic_settings_privacy_40,
        text = stringResource(com.esightcorp.mobile.app.ui.R.string.kDataPolicy)
    )
}

@Composable
internal fun SettingsVersion(modifier: Modifier = Modifier, version: String) = FineText(
    text = stringResource(
        com.esightcorp.mobile.app.ui.R.string.kSettingViewApplicationVersion, version
    ),
    modifier = modifier.fillMaxWidth(),
    textAlign = TextAlign.Center,
    color = MaterialTheme.colors.onSurface
)

private const val TAG = "SettingsScreen"
//endregion
