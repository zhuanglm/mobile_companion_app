package com.esightcorp.mobile.app.home

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.viewmodels.PermissionViewModel
import com.esightcorp.mobile.app.ui.components.containers.BaseSurface
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
fun HomePermissionScreen(navController: NavController) {
    BasePermissionScreen(navController = navController)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BasePermissionScreen(
    navController: NavController, vm: PermissionViewModel = hiltViewModel()
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = vm.getPermissionList())
    if (permissionsState.allPermissionsGranted) {
        LaunchedEffect(Unit) {
            navController.navigate(BtConnectionNavigation.IncomingRoute)
        }

    } else {
        BaseSurface() {
            Rationale(permissionsState = permissionsState)
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Rationale(permissionsState: MultiplePermissionsState) {
    PermissionDialog(permissionsState = permissionsState)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    permissionsState: MultiplePermissionsState?
) {
    AlertDialog(onDismissRequest = { Unit },
        confirmButton = {
            Button(onClick = { permissionsState?.launchMultiplePermissionRequest() }) {
                Text(text = "Request Permissions")
            }
        },
        title = {
            Text(text = "Permissions are required to use this app")
        },
        text = { Text(text = "These permissions allow you to have access to the full functionality of the app.") },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        dismissButton = {})

}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun PermissionDialogPreview() {
    PermissionDialog(null)

}