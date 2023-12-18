package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.viewmodels.PermissionViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.containers.BaseSurface
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePermissionScreen(
    navController: NavController,
    vm: PermissionViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)

    val permissionsState = rememberMultiplePermissionsState(permissions = vm.requiredPermissions)
    when (permissionsState.allPermissionsGranted) {
        true -> LaunchedEffect(Unit) { vm.navigateToBluetoothConnection(navController) }

        false -> BaseSurface {
            // Either first time running the app or user does not allow permission
            PermissionScreen(
                onRequestPermissions = {
                    Log.w(
                        TAG,
                        "permissionsState - onRequestPermissions:\nallPermissionsGranted: ${permissionsState.allPermissionsGranted}" +
                                "\nshouldShowRationale: ${permissionsState.shouldShowRationale}"
                    )

                    permissionsState.launchMultiplePermissionRequest()
                },
                onOpenAppSettings = vm::navigateToAppSettings
            )
        }
    }
}

//region Internal implementation

private const val TAG = "HomePermissionScreen"

@Composable
private fun PermissionScreen(
    modifier: Modifier = Modifier,
    onRequestPermissions: OnActionCallback? = null,
    onOpenAppSettings: OnActionCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    bottomButton = { },
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Header1Text(
            text = stringResource(R.string.label_home_screen_request_permission_title),
            modifier = modifier
        )
        ItemSpacer(30.dp)

        Subheader(
            text = stringResource(R.string.label_home_screen_request_permission_description),
            modifier = modifier,
            textAlign = TextAlign.Center
        )
        ItemSpacer(60.dp)

        TextRectangularButton(
            modifier = modifier,
            text = stringResource(R.string.label_home_screen_request_permission_button_confirm),
            onClick = { onRequestPermissions?.invoke() },
            textAlign = TextAlign.Center,
        )
        ItemSpacer(30.dp)

        OutlinedTextRectangularButton(
            onClick = { onOpenAppSettings?.invoke() },
            modifier = modifier,
            text = stringResource(R.string.label_home_screen_request_permission_goto_app_settings),
            textAlign = TextAlign.Center,
            textColor = MaterialTheme.colors.onSurface,
        )
    }
}

@Preview
@Composable
private fun PermissionScreenPreview() = MaterialTheme {
    PermissionScreen()
}

//endregion
