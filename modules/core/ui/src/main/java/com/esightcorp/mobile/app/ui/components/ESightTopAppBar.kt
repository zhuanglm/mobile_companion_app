package com.esightcorp.mobile.app.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R


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
    onBackButtonInvoked: () -> Unit,
    onSettingsButtonInvoked: () -> Unit,
    modifier: Modifier

) {

    CenterAlignedTopAppBar(
        modifier = modifier.semantics(mergeDescendants = true){},
        title = { TopAppBarTitle() },
        navigationIcon = {
            if (showBackButton) TopAppBarNavIconButton(
                onClick = onBackButtonInvoked,
                modifier = modifier
            )
        },
        actions = {
            if (showSettingsButton) TopAppBarSettingsIconButton(
                onClick = onSettingsButtonInvoked,
                modifier = modifier
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colors.surface
        )

    )
}

@Preview(showBackground = true)
@Composable
fun ESightTopAppBarPreview() {
    MaterialTheme {
        ESightTopAppBar(
            showBackButton = true,
            showSettingsButton = true,
            onBackButtonInvoked = { /* implement action for back button here in your real app */ },
            onSettingsButtonInvoked = { /* implement action for settings button here in your real app */ },
            modifier = Modifier
        )
    }
}

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
        if (darkTheme) androidx.compose.material.MaterialTheme.colors.surface else androidx.compose.material.MaterialTheme.colors.surface
    Log.i("TAG", "TopAppBarTitle: ${boxColor.toString()}")
    Box(
        modifier = Modifier
            .background(boxColor, shape = RoundedCornerShape(dimensionResource(id = R.dimen.logo_corner_radius_top_app_bar)))
            .wrapContentSize()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "eSight Logo",
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Min)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarTitlePreview() {
    MaterialTheme {
        TopAppBarTitle(darkTheme = false)
    }
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
    onClick: () -> Unit
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
            contentDescription = stringResource(id = R.string.back),
            modifier = modifier.size(dimensionResource(id = R.dimen.filled_icon_top_app_bar_size)),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarNavIconButtonPreview() {
    MaterialTheme {
        TopAppBarNavIconButton(modifier = Modifier, onClick = {Unit})
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
    onClick: () -> Unit
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
            contentDescription = stringResource(id = R.string.kSettingsViewTitleText),
            modifier = modifier.size(dimensionResource(id = R.dimen.filled_icon_top_app_bar_size))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarSettingsIconButtonPreview() {
    MaterialTheme {
        TopAppBarSettingsIconButton(modifier = Modifier, onClick = {Unit})
    }
}