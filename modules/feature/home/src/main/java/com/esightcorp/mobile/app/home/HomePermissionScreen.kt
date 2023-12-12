package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.viewmodels.PermissionViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.containers.BaseSurface
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePermissionScreen(
    navController: NavController,
    vm: PermissionViewModel = hiltViewModel(),
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = vm.requiredPermissions)
    Log.w(
        TAG,
        "permissionsState:\nallPermissionsGranted: ${permissionsState.allPermissionsGranted}" +
                "\nshouldShowRationale: ${permissionsState.shouldShowRationale}"
    )
    when (permissionsState.allPermissionsGranted) {
        true -> LaunchedEffect(Unit) { vm.navigateToBluetoothConnection(navController) }

        false -> BaseSurface {
            Rationale(permissionsState = permissionsState)
        }
    }
}

//region Internal implementation

private const val TAG = "HomePermissionScreen"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Rationale(permissionsState: MultiplePermissionsState) {
    PermissionDialog(permissionsState = permissionsState)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionDialog(
    permissionsState: MultiplePermissionsState?
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { permissionsState?.launchMultiplePermissionRequest() }) {
                Text(text = stringResource(R.string.label_home_screen_request_permission_button_confirm))
            }
        },
        title = {
            Text(text = stringResource(R.string.label_home_screen_request_permission_title))
        },
        text = { Text(stringResource(R.string.label_home_screen_request_permission_description)) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
private fun PermissionDialogPreview() = PermissionDialog(null)

//endregion
