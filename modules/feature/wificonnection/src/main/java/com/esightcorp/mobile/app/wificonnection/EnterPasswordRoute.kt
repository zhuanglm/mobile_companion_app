package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.text.PasswordEditText
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.EnterPasswordViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.AdvancedSettingsButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen

private const val TAG = "WifiCredentialsRoute"

@Composable
fun EnterPasswordRoute(
    navController: NavController,
    viewModel: EnterPasswordViewModel = hiltViewModel()
) {
    Log.d(TAG, "EnterPasswordRoute:")
    val wifiUiState by viewModel.uiState.collectAsState()
    EnterPasswordScreen(
        onPasswordSubmitted = viewModel::wifiPasswordSubmitted,
        onPasswordUpdated = viewModel::updatePassword,
        onAdvancedButtonPressed = viewModel::onAdvancedButtonPressed,
        onBackPressed = viewModel::onBackButtonPressed,
        wifiUiState = wifiUiState,
        navController = navController,
    )
}


@Composable
internal fun EnterPasswordScreen(
    onPasswordSubmitted: (NavController) -> Unit,
    onAdvancedButtonPressed: (NavController) -> Unit,
    onBackPressed: (NavController) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    wifiUiState: WifiCredentialsUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    PasswordField(
        onPasswordSubmitted = onPasswordSubmitted,
        modifier = modifier,
        onPasswordUpdated = onPasswordUpdated,
        wifiUiState = wifiUiState,
        navController = navController,
        onBackPressed = onBackPressed,
        onAdvancedButtonClicked = onAdvancedButtonPressed,
    )

}


@Composable
private fun PasswordField(
    onPasswordSubmitted: (NavController) -> Unit,
    onBackPressed: (NavController) -> Unit,
    onAdvancedButtonClicked: (NavController) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    modifier: Modifier,
    wifiUiState: WifiCredentialsUiState,
    navController: NavController
) {

    Surface(
        modifier = modifier
            .fillMaxSize(), color = MaterialTheme.colors.surface
    ) {

    }
    BaseScreen(
        modifier = modifier,
        showBackButton = true ,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed(navController) },
        onSettingsButtonInvoked = { /*unused*/ },
        bottomButton = { AdvancedSettingsButton(
            modifier = modifier,
            onAdvancedSettingsClick = { onAdvancedButtonClicked(navController) }
        ) }) {
        PasswordBody(
            modifier = modifier,
            onPasswordSubmitted = onPasswordSubmitted,
            onPasswordUpdated = onPasswordUpdated ,
            wifiUiState = wifiUiState ,
            navController = navController
        )

    }
}

@Composable
private fun PasswordBody(
    modifier: Modifier,
    onPasswordSubmitted: (NavController) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    wifiUiState: WifiCredentialsUiState,
    navController: NavController
){
    ConstraintLayout {
        val (header, editText, button) = createRefs()

        Header1Text(text = "Enter Wi-Fi password", modifier = modifier
            .constrainAs(header) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
            })

        PasswordEditText(
            value = wifiUiState.password,
            onValueChange = onPasswordUpdated,
            modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(header.bottom, margin = 25.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
        )

        TextRectangularButton(
            onClick = { onPasswordSubmitted(navController) } ,
            modifier = modifier.constrainAs(button) {
                top.linkTo(editText.bottom, margin = 25.dp)
                start.linkTo(editText.start)
                end.linkTo(editText.end)
            },
            text = stringResource(id = R.string.wifi_connect_button)
        )


    }
}


