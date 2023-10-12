package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.state.UnableToConnectUiState
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UnableToConnectViewModel @Inject constructor(
    application: Application, val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(UnableToConnectUiState())
    val uiState: StateFlow<UnableToConnectUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController

    private val listener = object : BluetoothConnectionRepositoryCallback{
        override fun scanStatus(isScanning: ScanningStatus) {
            //unused by this composable
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            //unused by this composable
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            //unused by this composable
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            updateBtEnabledState(enabled)
        }
    }

    init {
        btConnectionRepository.registerListener(listener)
        btConnectionRepository.setupBtModelListener()
    }
    private fun updateBtEnabledState(enabled: Boolean){
        _uiState.update {state ->
            state.copy(isBtEnabled = enabled)
        }
    }

    fun setNavController(navController: NavController){
        this.navController = navController
    }

    fun navigateToNoDevicesConnectedScreen(){
        if(this::navController.isInitialized){
            navController.navigate(BtConnectionScreens.NoDevicesConnectedRoute.route){
                popUpTo(BtConnectionScreens.NoDevicesConnectedRoute.route){
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    fun navigateToBtSearchingScreen(){
        if(this::navController.isInitialized){
            navController.navigate(BtConnectionScreens.BtSearchingRoute.route){
                popUpTo(BtConnectionScreens.NoDevicesConnectedRoute.route){
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

}