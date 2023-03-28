package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiTypeViewModel

@Composable
fun WifiTypeRoute(
    navController: NavController, vm: WifiTypeViewModel = hiltViewModel()
) {
    WifiTypeScreen(
        navController = navController,
        modifier = Modifier,
        vm = vm,
        onBackButtonPressed = navController::popBackStack,
        onWpaPressed = vm::onWpaPressed,
        onWepPressed = vm::onWepPressed,
        onNonePressed = vm::onNonePressed
    )
}


@Composable
internal fun WifiTypeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: WifiTypeViewModel,
    onBackButtonPressed: () -> Unit,
    onWpaPressed: () -> Unit,
    onWepPressed: () -> Unit,
    onNonePressed: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        ConstraintLayout(
            modifier = modifier
        ) {
            val (topAppBar, header, wpa, wep, none) = createRefs()
            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButtonPressed,
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Header1Text(text = stringResource(id = R.string.wifi_advanced_securityHeader),
                modifier = Modifier.constrainAs(header) {
                    top.linkTo(topAppBar.bottom, margin = 50.dp)
                    start.linkTo(parent.start, margin = 25.dp)
                    end.linkTo(parent.end, margin = 25.dp)
                })
            TextRectangularButton(onClick = onWpaPressed, modifier = modifier.constrainAs(wpa) {
                top.linkTo(header.bottom, margin = 35.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            }, text = stringResource(id = R.string.wifi_type_security_wpa))
            TextRectangularButton(onClick = onWepPressed, modifier = modifier.constrainAs(wep) {
                top.linkTo(wpa.bottom, margin = 20.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            }, text = stringResource(id = R.string.wifi_type_security_wep))
            TextRectangularButton(onClick = onNonePressed, modifier = modifier.constrainAs(none) {
                top.linkTo(wep.bottom, margin = 20.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            }, text = stringResource(id = R.string.wifi_type_security_none))

        }
    }


}
