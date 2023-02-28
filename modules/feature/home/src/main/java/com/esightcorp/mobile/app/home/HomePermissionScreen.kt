package com.esightcorp.mobile.app.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.home.viewmodels.PermissionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
fun HomePermissionScreen(navController: NavController){
    BasePermissionScreen(navController = navController)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BasePermissionScreen(
    navController: NavController,
    vm: PermissionViewModel = hiltViewModel()
){
    val permissionsState = rememberMultiplePermissionsState(permissions = vm.getPermissionList())
    if(permissionsState.allPermissionsGranted){
        LaunchedEffect(Unit){
            navController.navigate(BtConnectionScreens.IncomingNavigationRoute.route)
        }

    }else{
        Surface(color = Color(0xff004c4c)) {
            Rationale(permissionsState = permissionsState)
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Rationale(permissionsState: MultiplePermissionsState){
    val textToShow = "These permissions are required to use this app. Accept them"
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (text, button) = createRefs()
        Text(textToShow, Modifier.constrainAs(text){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start, margin = 16.dp)
            end.linkTo(parent.end, margin = 16.dp)
        })
        Button(onClick = {permissionsState.launchMultiplePermissionRequest()},
        modifier = Modifier.constrainAs(button){
            top.linkTo(text.bottom, margin = 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }){
            Text(text = "Request Permissions")
        }
    }
}