package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
public fun ESightTopAppBar(
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    onBackButtonInvoked: @Composable () -> Unit,
    onSettingsButtonInvoked: @Composable () -> Unit,
    modifier: Modifier

){

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { TopAppBarTitle() },
        navigationIcon = { if(showBackButton) TopAppBarNavIconButton(onClick = onBackButtonInvoked, modifier = modifier) },
        actions = {
            if(showSettingsButton) TopAppBarSettingsIconButton(onClick = onSettingsButtonInvoked, modifier = modifier)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        )

    )
}

@Composable
private fun TopAppBarTitle(){
    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "eSight Logo", modifier = Modifier.size(25.dp))

}

@Composable
fun TopAppBarNavIconButton(
    modifier: Modifier,
    onClick: @Composable () -> Unit
){
    FilledIconButton(
        onClick = { onClick },
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
    ) {
        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back button")
    }
}

@Composable
fun TopAppBarSettingsIconButton(
    modifier: Modifier,
    onClick: @Composable () -> Unit
){
    FilledIconButton(
        onClick = { onClick },
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
    ) {
        Icon(Icons.Rounded.Settings, contentDescription = "Settings button", modifier = modifier.size(30.dp))
    }
}