package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.navigation.EshareScreens
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.DeviceConnectionState
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.state.RadioState
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "EshareConnectedViewMode"

@HiltViewModel
class EshareConnectedViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository

) : AndroidViewModel(application), EshareRepositoryListener, SurfaceTextureListener {

    private var surfaceTexture: SurfaceTexture? = null

    private var _uiState = MutableStateFlow(EshareConnectedUiState())
    val uiState: StateFlow<EshareConnectedUiState> = _uiState.asStateFlow()
    private var wasStoppedByMobile: Boolean = false



    init {
        eShareRepository.setupEshareListener(this)
        wasStoppedByMobile = false
    }


    private fun updateConnectionState(state: eShareConnectionStatus) {
        _uiState.update { uiState ->
            uiState.copy(connectionState = state)
        }
    }

    private fun updateBluetoothDeviceState(state: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(deviceConnectionState = DeviceConnectionState(state))
        }
    }

    private fun updateBluetoothRadioState(state: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(radioState = RadioState(isBtEnabled = state, isWifiEnabled = uiState.radioState.isWifiEnabled))
        }
    }

    private fun updateWifiRadioState(state: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(radioState = RadioState(isBtEnabled = uiState.radioState.isBtEnabled, isWifiEnabled = state))
        }
    }

    override fun onEshareStateChanged(state: eShareConnectionStatus) {
        Log.i(TAG, "onEshareStateChanged: $state")
        updateConnectionState(state)
    }


    override fun onEshareStateRequested(state: eShareConnectionStatus) {
        updateConnectionState(state)
    }

    override fun onBluetoothDeviceDisconnected() {
        updateBluetoothDeviceState(false)
    }

    override fun onBluetoothDisabled() {
        updateBluetoothRadioState(false)
    }

    override fun onWifiStateChanged(state: Boolean) {
       updateWifiRadioState(state)
    }

    override fun onInputStreamCreated(inputStream: InputStream) {
        Log.i(TAG, "onInputStreamCreated: ")
        if (surfaceTexture != null) {
            Log.d(TAG, "onInputStreamCreated: surfaceTexture is not null")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                Log.d(TAG, "onInputStreamCreated: starting stream")
                eShareRepository.startStreamFromHMD(Surface(surfaceTexture!!), inputStream)
            }, 1000)
        }
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared: ")
        super.onCleared()
    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        Log.i(TAG, "onSurfaceTextureAvailable: " + width + " " + height)
        this.surfaceTexture = surfaceTexture
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureSizeChanged: ")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Log.i(TAG, "onSurfaceTextureDestroyed: ")
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        Log.i(TAG, "onSurfaceTextureUpdated: ")
    }

    fun navigateToStoppedRoute(navController: NavController) {
        if(wasStoppedByMobile){
            navController.navigate(HOME_FIRST_SCREEN){
                popUpTo(HOME_FIRST_SCREEN){
                    inclusive = false
                }
                launchSingleTop = true
            }
        }else{
            navController.navigate(EshareScreens.EshareConnectionStoppedRoute.route)
        }
    }

    fun navigateToBusyRoute(navController: NavController) {
        Log.i(TAG, "navigateToBusyRoute: ")
        navController.navigate(EshareScreens.EshareBusyRoute.route) {
            popUpTo(EshareScreens.IncomingNavigationRoute.route) {
                inclusive = true
            }
            launchSingleTop = true
        }

    }

    fun navigateToUnableToConnectRoute(navController: NavController) {
        Log.i(TAG, "navigateToUnableToConnectRoute: ")
        navController.navigate(EshareScreens.EshareUnableToConnectRoute.route) {
            popUpTo(EshareScreens.IncomingNavigationRoute.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun startEshareConnection(){
        Log.i(TAG, "startEshareConnection: ")
        eShareRepository.startEshareConnection()
    }

    fun onCancelButtonClicked(navController: NavController) {
        Log.i(TAG, "onCancelButtonClicked: ")
        wasStoppedByMobile = true
        eShareRepository.cancelEshareConnection()

    }

    fun upButtonPress(){
        eShareRepository.writeUpButtonPress()
    }
    fun downButtonPress(){
        eShareRepository.writeDownButtonPress()
    }

    fun volUpButtonPress(){
        eShareRepository.writeVolumeUpButtonPress()
    }

    fun volDownButtonPress(){
        eShareRepository.writeVolumeDownButtonPress()
    }

    fun modeButtonPress(){
        eShareRepository.writeModeButtonPress()
    }

    fun menuButtonPress(){
        eShareRepository.writeMenuButtonPress()
    }

    fun finderButtonPress(){
        eShareRepository.writeFinderButtonPress()
    }

    fun actionUpButtonPress(){
        eShareRepository.writeActionUpEvent()
    }

    companion object{
        const val HOME_FIRST_SCREEN = "home_first"
    }
}


