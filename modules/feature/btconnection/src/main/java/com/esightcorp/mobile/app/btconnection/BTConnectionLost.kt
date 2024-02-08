/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun BtConnectionLostRoute(navController: NavController) {
    BackStackLogger(navController, TAG)

    BtConnectionLostScreen(
        navController,
        onReconnectCallback = { nav ->
            nav.navigate(target = BtConnectionNavigation.BtSearchingRoute, popCurrent = true)
        },
        onCancelCallback = { nav ->
            nav.navigate(target = BtConnectionNavigation.NoDeviceConnectedRoute, popCurrent = true)
        },
    )
}

//region Private implementation
private const val TAG = "BtConnectionLost"

@Composable
private fun BtConnectionLostScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onReconnectCallback: OnNavigationCallback? = null,
    onCancelCallback: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = Modifier,
    showBackButton = false,
    showSettingsButton = false,
    onSettingsButtonInvoked = { },
    bottomButton = { },
    bottomAlignedContent = { },
) {
    Column(
        modifier = modifier
            .padding(vertical = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(drawableId = R.drawable.ic_settings_disconnected,
            contentDescription = stringResource(id = R.string.kAccessibilityIconDisconnected))

        ItemSpacer(30.dp)
        Header1Text(
            text = stringResource(R.string.kBTErrorConnectionLostTitleText),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(30.dp)
        Subheader(
            text = stringResource(R.string.kBTErrorConnectionLostDescriptionText),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(60.dp)
        TextRectangularButton(
            onClick = { onReconnectCallback?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kBTErrorReconnectButtonText),
            textAlign = TextAlign.Center
        )

        ItemSpacer(30.dp)
        OutlinedTextRectangularButton(
            onClick = { onCancelCallback?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kCancel),
            textAlign = TextAlign.Center,
            textColor = MaterialTheme.colors.onSurface,
        )
    }
}

//region Preview

@Preview
@Composable
private fun BtConnectionLostScreenPreview() = MaterialTheme {
    BtConnectionLostScreen(
        navController = rememberNavController()
    )
}

//endregion

//endregion
