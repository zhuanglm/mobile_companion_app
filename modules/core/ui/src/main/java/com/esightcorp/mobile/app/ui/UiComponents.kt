package com.esightcorp.mobile.app.ui

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
public fun CustomTopAppBar(
    showBackButton: Boolean,
    showSettingsButton: Boolean,
//    onBackButtonInvoked: () -> Unit,
//    onSettingsButtonInvoked: () -> Unit,
    modifier: Modifier

){

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { TopAppBarTitle() },
        navigationIcon = { if(showBackButton) TopAppBarNavIconButton() },
        actions = {
            if(showSettingsButton) TopAppBarSettingsIconButton()
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        )

    )
}

@Composable
private fun TopAppBarTitle(){
    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Settings")
}

@Composable fun TopAppBarNavIconButton(){
    FilledIconButton(
        onClick = { Unit },
        modifier = Modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
        ) {
        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back button")
    }
}

@Composable
fun TopAppBarSettingsIconButton(){
    FilledIconButton(
        onClick = { Unit },
        modifier = Modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
    ) {
        Icon(Icons.Rounded.Settings, contentDescription = "Back button")
    }
}

@Composable
fun AddDeviceButton(){
    ElevatedButton(
        onClick = { Unit },
        modifier = Modifier,
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(Color.Yellow, Color.Black),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        Icon(Icons.Rounded.Add, contentDescription = "Add something new", Modifier.padding(15.dp, 0.dp))
        Text("Connect to eSight Go", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FeedbackButton(){
    ExtendedFloatingActionButton(
        text = { Text(text = "Feedback")},
        icon =  { FeedbackIcon() },
        onClick = {Unit},
        containerColor = Color.Black,
        contentColor = Color.White)
}

@Composable
private fun FeedbackIcon(){
    FilledIconButton(
        onClick = { Unit },
        modifier = Modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
    ) {
        Icon(painter = painterResource(id = R.drawable.round_feedback_20), contentDescription = "Feedback")
    }
}

@Preview
@Composable
fun Preview(){
//    TopAppBarSettingsIconButton()
//    TopAppBarNavIconButton()
//    TopAppBarTitle()
//    CustomTopAppBar(false, true, Modifier)
//    AddDeviceButton()
    FeedbackButton()
//    FeedbackIcon()
}

fun stubUnitFunc(){

}
