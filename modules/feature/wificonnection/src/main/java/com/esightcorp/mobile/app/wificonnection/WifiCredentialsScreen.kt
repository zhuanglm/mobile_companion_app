package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel

private const val TAG = "WifiCredentialsRoute"

@Composable
internal fun WifiCredentialsRoute(
    navController: NavController,
    ssid: String,
    viewModel: WifiConnectionViewModel = hiltViewModel()
){
    Log.d(TAG, "WifiCredentialsRoute: $ssid")
    val wifiUiState by viewModel.uiState.collectAsState()
    viewModel.updateSsid(ssid)
    WifiCredentialsScreen(
        onPasswordSubmitted = viewModel::wifiPasswordSubmitted,
        onPasswordUpdated = viewModel::updatePassword,
        onWifiTypeSubmitted = viewModel::wifiTypeSubmitted,
        onWifiTypeUpdated = viewModel::updateWifiType,
        onWifiNetworkSelected = viewModel::updateSsid,
        wifiUiState = wifiUiState,
        navController = navController,
    )
}



@Composable
internal fun WifiCredentialsScreen(
    onPasswordSubmitted: () -> Unit,
    onWifiTypeSubmitted: () -> Unit,
    onWifiNetworkSelected: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onWifiTypeUpdated: (String) -> Unit,
    wifiUiState: WifiConnectionUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
){
    Surface(modifier.fillMaxSize()) {
        ConstraintLayout {
            val (type, pwd) = createRefs()
            if(wifiUiState.passwordSubmitted){
                WifiTypeField(
                    onWifiTypeSubmitted = onWifiTypeSubmitted,
                    modifier = modifier.constrainAs(type){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onWifiTypeUpdated = onWifiTypeUpdated, navController = navController)
            }else{
                PasswordField(
                    onPasswordSubmitted = onPasswordSubmitted,
                    modifier = modifier.constrainAs(pwd){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onPasswordUpdated = onPasswordUpdated,
                    wifiUiState = wifiUiState)
            }
        }
    }

}


@Composable
private fun PasswordField(
    onPasswordSubmitted: () -> Unit,
    onPasswordUpdated: (String) -> Unit,
    modifier: Modifier,
    wifiUiState: WifiConnectionUiState
){

    //textfield
    OutlinedTextField(value = wifiUiState.password,
        onValueChange = onPasswordUpdated,
        label = { Text("Type your password here") },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
    )
    SubmitButton(modifier = modifier, action = onPasswordSubmitted)
}

@Composable
private fun WifiTypeField(
    onWifiTypeSubmitted: () -> Unit,
    onWifiTypeUpdated: (String) -> Unit,
    modifier: Modifier,
    navController: NavController){

    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ){
        val (outlinedButton, submit, dropDown) =  createRefs()
        BoxedDropdownMenu(
            modifier = modifier.constrainAs(dropDown){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onWifiTypeUpdated = onWifiTypeUpdated)

        SubmitButton(
            modifier = Modifier.constrainAs(submit){
                top.linkTo(dropDown.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            action = { onWifiTypeSubmitted()
                navController.navigate("home")})
    }
}

@Composable
private fun BoxedDropdownMenu(
    modifier: Modifier,
    onWifiTypeUpdated: (String) -> Unit
){
    val wifiTypes = listOf("WPA-2/WPA", "WEP", "None")
    var expanded by remember{ mutableStateOf(true)}
    var selectedIndex by remember{ mutableStateOf(0)}

    Box(modifier = modifier
        .wrapContentSize(Alignment.Center)
    ){
        OutlinedButton(onClick = { expanded = true }, modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)) {
            Text(text = wifiTypes[selectedIndex])
            onWifiTypeUpdated(wifiTypes[selectedIndex])
        }
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = modifier.fillMaxWidth()) {
            wifiTypes.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onWifiTypeUpdated(item)
                    selectedIndex = index
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
private fun SubmitButton(
    modifier: Modifier,
    action: () -> Unit
) {
    Button(modifier = modifier,
        onClick = action) {
        Text(text = "Submit")
    }
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    //textfield
    OutlinedTextField("String",
        onValueChange = { stubStringFunction("testing") },
        label = { Text("Type your password here") },
        modifier = Modifier
    )
    SubmitButton(modifier = Modifier, action = { stubFunction() })
}

fun stubFunction(){
    //use this to stub out your preview functions
    Log.d(TAG, "stubFunction: ")
}

fun stubStringFunction(value: String){
    //use this to stub out your preview functions
    Log.d(TAG, "stubStringFunction: ")
}


