package com.esightcorp.mobile.app.wificonnection

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


@Composable
internal fun WifiCredentialsRoute(
    navController: NavController,
    viewModel: WifiConnectionViewModel = hiltViewModel()
){
    val wifiUiState by viewModel.uiState.collectAsState()
    WifiCredentialsScreen(
        onPasswordSubmitted = viewModel::wifiPasswordSubmitted,
        onPasswordUpdated = viewModel::updatePassword,
        onWifiTypeSubmitted = viewModel::wifiTypeSubmitted,
        onWifiTypeUpdated = viewModel::updateWifiType,
        onWifiNetworkSelected = viewModel::updateSsid,
        wifiUiState = wifiUiState,
        navController = navController,
        sendWifiCredsViaBluetooth = viewModel::sendWifiCredsViaBluetooth
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
    sendWifiCredsViaBluetooth: () -> Unit
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
                    onWifiTypeUpdated = onWifiTypeUpdated)
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
        label = { Text("Password") }
    )
    SubmitButton(modifier = modifier, action = onPasswordSubmitted)
}

@Composable
private fun WifiTypeField(
    onWifiTypeSubmitted: () -> Unit,
    onWifiTypeUpdated: (String) -> Unit,
    modifier: Modifier){
    val wifiTypes = listOf("WPA-2/WPA", "WEP", "None")
    var expanded by remember{ mutableStateOf(true)}
    var selectedIndex by remember{ mutableStateOf(0)}

    ConstraintLayout(modifier = Modifier.fillMaxWidth().fillMaxHeight().border(4.dp, Color.Red)){
        val (outlinedButton, submit, dropDown) =  createRefs()
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.constrainAs(outlinedButton){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }) {
            Text(text = wifiTypes[selectedIndex])
            onWifiTypeUpdated(wifiTypes[selectedIndex])
        }
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.constrainAs(dropDown){
                top.linkTo(outlinedButton.bottom)
                start.linkTo(outlinedButton.start)
                end.linkTo(outlinedButton.end)
            }) {
            wifiTypes.forEach { item ->
                DropdownMenuItem(onClick = {onWifiTypeUpdated(item)}) {
                    Text(text = item)
                }
            }
        }
        SubmitButton(
            modifier = Modifier.constrainAs(submit){
                top.linkTo(outlinedButton.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            action = onWifiTypeSubmitted)
    }
    
   /* Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(top = 10.dp)
        .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        .clickable(
            onClick = { expanded = true }
        )
    ){
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(2.dp, Color.Red)) {

            val(label) = createRefs()
            Text(text = "WPA/WPA2",
            modifier = Modifier.padding(16.dp)
                .constrainAs(label) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = Color.Green)
            //dropdown menu
            DropdownMenu(expanded = expanded,
                onDismissRequest = {expanded = false},
                modifier = Modifier.background(Color.Green)) {
                wifiTypes.forEachIndexed{ index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        onWifiTypeUpdated
                    }) {
                        Text(text = s, modifier = Modifier.padding(15.dp))
                    }
                }
            }
            SubmitButton(modifier = modifier, action = onWifiTypeSubmitted)

       }
    }*/

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

@Composable
@Preview
fun preview(){

}



