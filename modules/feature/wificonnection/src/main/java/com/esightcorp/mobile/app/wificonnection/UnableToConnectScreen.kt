/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.wificonnection.viewmodels.UnableToConnectViewModel

@Composable
fun UnableToConnectRoute(
    navController: NavController,
    vm: UnableToConnectViewModel = hiltViewModel(),
) {
    UnableToConnectScreen(
        navController = navController,
        onBackPressed = vm::onBackPressed,
        onTryAgainClicked = vm::onTryAgain,
        onHowToConnectClicked = vm::gotoEsightSupportSite,
    )
}

//region Private implementation
@Composable
internal fun UnableToConnectScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackPressed: OnNavigationCallback? = null,
    onTryAgainClicked: OnNavigationCallback? = null,
    onHowToConnectClicked: OnActionCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    showSettingsButton = false,
    onBackButtonInvoked = { onBackPressed?.invoke(navController) },
    onSettingsButtonInvoked = { },
    bottomButton = {
        HowToConnectButton(
            modifier = modifier,
            onConnectClick = { onHowToConnectClicked?.invoke() }
        )
    },
    bottomAlignedContent = {
        BodyText(
            modifier = modifier,
            text = stringResource(R.string.kWifiTroubleshootingUnableToConnectFooter),
            color = MaterialTheme.colors.onSurface,
        )
    },
) {
    ScreenBody(
        modifier = modifier,
        navController = navController,
        onTryAgain = onTryAgainClicked
    )
}

@Composable
private fun ScreenBody(
    modifier: Modifier,
    navController: NavController,
    onTryAgain: OnNavigationCallback? = null,
) {
    ConstraintLayout {
        val (headerText, header2Text, nhiOne, nhiTwo, nhiThree, helpText, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.kTroubleshootingUnableToConnectTitle),
            modifier = modifier.constrainAs(headerText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 16.dp)
            },
        )

        BoldSubheader(
            text = stringResource(R.string.kTroubleshootingUnableToConnectSubtitle),
            modifier = modifier.constrainAs(header2Text) {
                top.linkTo(headerText.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            },
        )

        NumberedHelpItem(
            number = 1,
            text = stringResource(R.string.kTroubleshootingInstructionWiFiPassword),
            modifier = modifier.constrainAs(nhiOne) {
                top.linkTo(header2Text.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            },
        )

        NumberedHelpItem(
            number = 2,
            text = stringResource(R.string.kTroubleshootingInstructionEsightWithinRangeShort),
            modifier = modifier.constrainAs(nhiTwo) {
                top.linkTo(nhiOne.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            },
        )

        NumberedHelpItem(
            number = 3,
            text = stringResource(R.string.kTroubleshootingInstructionsSufficientChargeShort),
            modifier = modifier.constrainAs(nhiThree) {
                top.linkTo(nhiTwo.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            },
        )

        TextRectangularButton(
            onClick = { onTryAgain?.invoke(navController) },
            modifier = modifier.constrainAs(button) {
                top.linkTo(nhiThree.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            text = stringResource(R.string.kTryAgainButtonTitle),
        )
    }
}

@Preview(locale = "es")
@Composable
private fun UnableToConnectScreenPreview() = MaterialTheme {
    UnableToConnectScreen(
        navController = rememberNavController()
    )
}

//endregion
