package com.esightcorp.mobile.app.btconnection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.viewmodels.BtDisabledViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon

@Composable
fun BtDisabledScreen(
    navController: NavController,
    vm: BtDisabledViewModel = hiltViewModel(),
) {
    Log.d(TAG, "BtDisabledScreen")
    val uiState by vm.uiState.collectAsState()

    when (uiState.isBtEnabled) {
        false -> {
            BtDisabledScreenImpl(onBtStateChanged = vm::updateBtEnabledState)
        }

        true -> {
            Log.d(TAG, "BtDisabledScreen - BT is now enabled!")
            LaunchedEffect(Unit) { vm.onBtEnabled(navController) }
        }
    }
}

//region Internal implementation

private const val TAG = "BtDisabledScreen"

@Composable
private fun BtDisabledScreenImpl(
    modifier: Modifier = Modifier,
    onBtStateChanged: (Boolean) -> Unit,
) {
    BtDisabledBody(modifier = modifier)

    // If Bluetooth is not enabled, launch system dialog to enable Bluetooth
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            Log.d(TAG, "isBluetoothEnabled: $it")
            onBtStateChanged.invoke(it.resultCode == Activity.RESULT_OK)
        }
    )

    DisposableEffect(Unit) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        launcher.launch(intent)
        onDispose {
            // clean up any resources if needed
        }
    }
}

@Composable
private fun BtDisabledBody(
    modifier: Modifier = Modifier,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    bottomButton = { },
) {
    Column(
        modifier = modifier
            .padding(vertical = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(drawableId = R.drawable.baseline_bluetooth_24)
        ItemSpacer(30.dp)

        // Set up the header text
        Header1Text(
            text = stringResource(R.string.kBTErrorBluetoothOffTitle),
            modifier = modifier,
        )
        ItemSpacer(60.dp)

        // Set up the body text
        Subheader(
            text = stringResource(R.string.kBTErrorBluetoothOffDescription),
            modifier = modifier,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun BtDisabledBodyPreview() = MaterialTheme {
    BtDisabledBody()
}

//endregion
