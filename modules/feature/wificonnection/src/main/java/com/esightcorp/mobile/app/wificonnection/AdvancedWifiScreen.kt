package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.esightcorp.mobile.app.ui.components.text.CustomEditText
import com.esightcorp.mobile.app.ui.components.text.PasswordEditText
import com.esightcorp.mobile.app.wificonnection.viewmodels.AdvancedWifiViewModel
@Composable
fun AdvancedWifiRoute(
    navController: NavController,
    vm: AdvancedWifiViewModel = hiltViewModel()
){
    AdvancedWifiScreen(
        navController = navController,
        modifier = Modifier,
        vm = vm,
        onSsidUpdated = vm::onSsidUpdated,
        onPasswordUpdated = vm::onPasswordUpdated
    )
}

@Composable
internal fun AdvancedWifiScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: AdvancedWifiViewModel,
    onSsidUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
){
    val wifiUiState by vm.uiState.collectAsState()
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topAppBar, ssidHeader, ssidField, pwdHeader, pwdField, securityHeader, securityButton, finishButton) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { /*TODO*/ },
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Header1Text(text = stringResource(id = R.string.wifi_advanced_ssidHeader) , modifier = modifier.constrainAs(ssidHeader){
                top.linkTo(topAppBar.bottom, margin = 50.dp)
                start.linkTo(parent.start, margin = 25.dp)
            })

            CustomEditText(value = wifiUiState.ssid,
                onValueChange = onSsidUpdated,
                modifier = modifier.constrainAs(ssidField){
                    top.linkTo(ssidHeader.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 25.dp)
                    end.linkTo(parent.end, margin = 25.dp)
                })

            Header1Text(text = stringResource(id = R.string.wifi_advanced_pwdHeader) , modifier = modifier.constrainAs(pwdHeader){
                top.linkTo(ssidField.bottom, margin = 35.dp)
                start.linkTo(parent.start, margin = 25.dp)
            })
            PasswordEditText(value = wifiUiState.password, onValueChange = onPasswordUpdated, modifier = modifier.constrainAs(pwdField){
                top.linkTo(pwdHeader.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            })
            Header1Text(text = stringResource(id = R.string.wifi_advanced_securityHeader) , modifier = modifier.constrainAs(securityHeader){
                top.linkTo(pwdField.bottom, margin = 35.dp)
                start.linkTo(parent.start, margin = 25.dp)
            })

            TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(securityButton){
                top.linkTo(securityHeader.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            },  text = wifiUiState.wifiType)

            TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(finishButton){
                top.linkTo(securityButton.bottom, margin = 35.dp)
                start.linkTo(parent.start, margin = 25.dp)
                end.linkTo(parent.end, margin = 25.dp)
            },  text = stringResource(id = R.string.wifi_advanced_QR_finishButton))

        }
    }
}