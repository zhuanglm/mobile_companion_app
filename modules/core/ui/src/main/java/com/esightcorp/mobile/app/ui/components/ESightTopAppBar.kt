/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback


/**
 * Displays a top app bar with optional back and settings buttons.
 * The buttons are displayed based on the boolean parameters `showBackButton` and `showSettingsButton`.
 * The back button is displayed on the left, and the settings button is displayed on the right.
 * The app bar has the `CenterAlignedTopAppBar` design with a title in the center.
 * The container color of the top app bar is set to the `surface` color of the current material theme.
 *
 * This composable uses experimental Material 3 API.
 *
 * @param showBackButton If true, the back button is displayed. Otherwise, it's not displayed.
 * @param showSettingsButton If true, the settings button is displayed. Otherwise, it's not displayed.
 * @param onBackButtonInvoked A lambda function that will be triggered when the back button is clicked.
 * @param onSettingsButtonInvoked A lambda function that will be triggered when the settings button is clicked.
 * @param modifier A [Modifier] applied to the top app bar for layout and styling.
 *
 * @see CenterAlignedTopAppBar
 * @see TopAppBarTitle
 * @see TopAppBarNavIconButton
 * @see TopAppBarSettingsIconButton
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ESightTopAppBar(
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    onBackButtonInvoked: OnActionCallback? = null,
    onSettingsButtonInvoked: OnActionCallback? = null,
    modifier: Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier.semantics(mergeDescendants = true) {},
        title = { TopAppBarTitle() },
        navigationIcon = {
            if (showBackButton) TopAppBarNavIconButton(
                onClick = { onBackButtonInvoked?.invoke() },
                modifier = modifier
            )
        },
        actions = {
            if (showSettingsButton) TopAppBarSettingsIconButton(
                onClick = { onSettingsButtonInvoked?.invoke() },
                modifier = modifier
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colors.surface
        )
    )
}

/**
 * Displays a navigation icon button on the top app bar.
 * The icon used is `Icons.Rounded.ArrowBack` which represents a back arrow.
 *
 * @param modifier A [Modifier] applied to the icon button for layout and styling.
 * @param onClick A lambda function that will be triggered when the icon button is clicked.
 */
@Composable
fun TopAppBarNavIconButton(
    modifier: Modifier,
    onClick: OnActionCallback
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary
        ),
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.kAccessibilityButtonBack),
            modifier = modifier.size(dimensionResource(R.dimen.filled_icon_top_app_bar_size)),
        )
    }
}

/**
 * Displays a settings icon button on the top app bar.
 * The icon used is `Icons.Rounded.Settings` which represents settings.
 *
 * @param modifier A [Modifier] applied to the icon button for layout and styling.
 * @param onClick A lambda function that will be triggered when the icon button is clicked.
 */
@Composable
fun TopAppBarSettingsIconButton(
    modifier: Modifier,
    onClick: OnActionCallback
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary
        ),
    ) {
        Icon(
            Icons.Rounded.Settings,
            contentDescription = stringResource(R.string.kSettingsViewTitleText),
            modifier = modifier.size(dimensionResource(R.dimen.filled_icon_top_app_bar_size))
        )
    }
}


//region Internal implementation

private const val TAG = "ESightTopAppBar"

/**
 * Displays a title for the top app bar, which is a logo image wrapped inside a box.
 * The background color of the box is determined based on the theme of the system.
 *
 * @param darkTheme A boolean value to determine whether the system theme is dark or not.
 * Defaults to `isSystemInDarkTheme()`.
 */
@Composable
private fun TopAppBarTitle(
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val boxColor =
        if (darkTheme) MaterialTheme.colors.surface else MaterialTheme.colors.surface
    Log.i(TAG, "TopAppBarTitle: $boxColor")
    Box(
        modifier = Modifier
            .background(
                boxColor,
                shape = RoundedCornerShape(dimensionResource(R.dimen.logo_corner_radius_top_app_bar))
            )
            .wrapContentSize()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "",
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Min)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ESightTopAppBarPreview() = MaterialTheme {
    ESightTopAppBar(
        showBackButton = true,
        showSettingsButton = true,
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarTitlePreview() = MaterialTheme {
    TopAppBarTitle(darkTheme = false)
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarNavIconButtonPreview() = MaterialTheme {
    TopAppBarNavIconButton(modifier = Modifier, onClick = { })
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarSettingsIconButtonPreview() = MaterialTheme {
    TopAppBarSettingsIconButton(modifier = Modifier, onClick = { })
}

//endregion
