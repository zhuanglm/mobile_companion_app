package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectionViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.BigIcon
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Header2Text

@Composable
fun BtDisabledScreen(
    navController: NavController,
//    vm:BtConnectionViewModel = hiltViewModel()
){
    Log.d(TAG, "BtDisabledScreen: ")
    TurnOnBluetoothScreen(onBackPressed = { Unit }, modifier = Modifier)


}

@Composable
internal fun TurnOnBluetoothScreen(
    onBackPressed: ()-> Unit,
    modifier: Modifier

){
    Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout {
            val (topBar, bigIcon, headerText, header2Text) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { onBackPressed },
                onSettingsButtonInvoked = {/*Unused*/ Unit},
                modifier = modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            BigIcon(
                painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                contentDescription = "Bluetooth Icon",
                modifier = modifier.constrainAs(bigIcon){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })

            Header1Text(
                text = "Turn on Bluetooth",
                modifier = modifier.constrainAs(headerText){
                    top.linkTo(bigIcon.bottom, margin = 25.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
            })
            
            Header2Text(
                text = "Open Settings on your phone, and turn on Bluetooth to connect",
                modifier = modifier
                    .padding(35.dp, 0.dp)
                    .constrainAs(header2Text){
                    top.linkTo(headerText.bottom, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end) },
                textAlign = TextAlign.Center

            )
        }
    }
}