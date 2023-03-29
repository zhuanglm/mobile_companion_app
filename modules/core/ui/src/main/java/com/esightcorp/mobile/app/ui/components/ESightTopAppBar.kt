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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R

//TODO: Update the content descriptions to use resources
//TODO: Change all colours to follow material theme
//TODO: Extrapolate values to res file
//TODO: Create previews for everything

/**
 * There are a few variations on this top app bar.
 * logo + gear
 * back + logo
 * just logo
 * [Note: Dont see an option where it is back + logo + gear]
 * e is always centered and always shown
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
        modifier = modifier,
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

@Composable
private fun TopAppBarTitle(
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val boxColor =
        if (darkTheme) androidx.compose.material.MaterialTheme.colors.surface else androidx.compose.material.MaterialTheme.colors.onSurface
    Log.i("TAG", "TopAppBarTitle: ${boxColor.toString()}")
    Box(
        modifier = Modifier.background(boxColor, shape = RoundedCornerShape(10.dp)).wrapContentSize().padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "eSight Logo",
            modifier = Modifier.height(IntrinsicSize.Min).width(IntrinsicSize.Min)
        )
    }


}

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
            contentDescription = "Back button",
            modifier = modifier.size(30.dp),
        )
    }
}

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
            contentDescription = "Settings button",
            modifier = modifier.size(30.dp)
        )
    }
}