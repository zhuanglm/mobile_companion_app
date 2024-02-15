/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.containers.Centered
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.SubHeader
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun PermissionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    @StringRes titleId: Int = R.string.kPermissionRequiredTitle,
    @StringRes descriptionId: Int = R.string.label_home_screen_request_permission_description,
    @StringRes okLabelId: Int = R.string.kPermissionOpenSettingsButton,
    onCancelPressed: OnNavigationCallback? = null,
    onOkPressed: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    isContentCentered = true,
    bottomButton = { },
) {
    Centered {
        BigIcon(
            drawableId = R.drawable.warning,
            contentDescription = stringResource(R.string.kAccessibilityIconExclamation)
        )

        ItemSpacer(30.dp)
        Header1Text(
            text = stringResource(titleId),
            modifier = modifier,
            textAlign = TextAlign.Center,
        )
        ItemSpacer(30.dp)

        SubHeader(
            text = stringResource(descriptionId),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(60.dp)

        TextRectangularButton(
            onClick = { onOkPressed?.invoke(navController) },
            modifier = modifier,
            text = stringResource(okLabelId),
            textAlign = TextAlign.Center,
        )
        ItemSpacer(30.dp)

        OutlinedTextRectangularButton(
            onClick = { onCancelPressed?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kCancel),
            textAlign = TextAlign.Center,
            textColor = MaterialTheme.colors.onSurface,
        )
    }
}

//region Private implementation
@Preview
@Composable
private fun PermissionScreenPreview() = MaterialTheme {
    PermissionScreen(
        navController = rememberNavController()
    )
}
//endregion
