/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiTypeViewModel

@Composable
fun WifiTypeRoute(
    navController: NavController, vm: WifiTypeViewModel = hiltViewModel()
) {
    WifiTypeScreen(
        navController = navController,
        modifier = Modifier,
        onTypePressed = vm::onTypePressed,
    )
}


@Composable
internal fun WifiTypeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onTypePressed: (NavController, String) -> Unit,
) {


    BaseScreen(
        modifier = modifier,
        showBackButton = false ,
        showSettingsButton = false ,
        onBackButtonInvoked = { /*unused*/ },
        onSettingsButtonInvoked = { /*unused*/ },
        isBottomButtonNeeded = false,
        bottomButton = { Unit }) {
        WifiTypeBody(
            modifier = modifier,
            navController = navController,
            onTypePressed = onTypePressed,
        )

    }


}

@Composable
private fun WifiTypeBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    onTypePressed: (NavController, String) -> Unit,
){
    ConstraintLayout(
        modifier = modifier
    ) {
        val (header, wpa, wep, none) = createRefs()
        Header1Text(text = stringResource(id = R.string.kWifiViewControllerSelectSecurityLabelText),
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        TextRectangularButton(onClick = { onTypePressed(navController, "WPA/WPA2") }, modifier = modifier.constrainAs(wpa) {
            top.linkTo(header.bottom, margin = 35.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = stringResource(id = R.string.kWifiSecurityTypeWPA))
        TextRectangularButton(onClick = { onTypePressed(navController, "WEP") }, modifier = modifier.constrainAs(wep) {
            top.linkTo(wpa.bottom, margin = 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = stringResource(id = R.string.kWifiSecurityTypeWEP))
        TextRectangularButton(onClick = { onTypePressed(navController, "None") }, modifier = modifier.constrainAs(none) {
            top.linkTo(wep.bottom, margin = 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = stringResource(id = R.string.kWifiSecurityTypeNone))

    }
}
