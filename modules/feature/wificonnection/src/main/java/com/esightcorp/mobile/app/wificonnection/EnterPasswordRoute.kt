package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.text.EEditText
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.EnterPasswordViewModel

private const val TAG = "WifiCredentialsRoute"

@Composable
fun EnterPasswordRoute(
    navController: NavController,
    viewModel: EnterPasswordViewModel = hiltViewModel()
) {
    Log.d(TAG, "EnterPasswordRoute:")
    val wifiUiState by viewModel.uiState.collectAsState()
    viewModel.setNavController(navController = navController)
    EnterPasswordScreen(
        onPasswordSubmitted = viewModel::wifiPasswordSubmitted,
        onPasswordUpdated = viewModel::updatePassword,
        onWifiTypeSubmitted = viewModel::wifiTypeSubmitted,
        onWifiTypeUpdated = viewModel::updateWifiType,
        wifiUiState = wifiUiState,
        navController = navController,
    )
}


@Composable
internal fun EnterPasswordScreen(
    onPasswordSubmitted: () -> Unit,
    onWifiTypeSubmitted: () -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onWifiTypeUpdated: (String) -> Unit,
    wifiUiState: WifiCredentialsUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    PasswordField(
        onPasswordSubmitted = onPasswordSubmitted,
        modifier = modifier,
        onPasswordUpdated = onPasswordUpdated,
        wifiUiState = wifiUiState,
        navController = navController
    )

}


@Composable
private fun PasswordField(
    onPasswordSubmitted: () -> Unit,
    onPasswordUpdated: (String) -> Unit,
    modifier: Modifier,
    wifiUiState: WifiCredentialsUiState,
    navController: NavController
) {

    Surface(
        modifier = modifier
            .fillMaxSize(), color = Color.Black
    ) {
        ConstraintLayout {
            val (topbar, header, editText, button) = createRefs()

            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { Unit },
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Header1Text(text = "Enter Wi-Fi password", modifier = modifier
                .constrainAs(header) {
                    top.linkTo(topbar.bottom)
                    start.linkTo(parent.start)

                }
                .padding(25.dp, 50.dp, 0.dp, 0.dp))

            EEditText(
                value = wifiUiState.password,
                onValueChange = onPasswordUpdated,
                modifier = Modifier
                    .constrainAs(editText) {
                        top.linkTo(header.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(25.dp)
            )

            TextRectangularButton(
                onClick = onPasswordSubmitted ,
                modifier = modifier.constrainAs(button) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                    end.linkTo(editText.end)
                },
                text = "Connect"
            )


        }
    }
}


