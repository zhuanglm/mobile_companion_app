package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.DeviceConnectionState
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.state.RadioState
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EshareConnectedViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository
) : ESightBaseViewModel(application), EshareRepositoryListener, SurfaceTextureListener {

    private val _tag = this.javaClass.simpleName

    private var surfaceTexture: SurfaceTexture? = null

    private var _uiState = MutableStateFlow(EshareConnectedUiState())
    val uiState: StateFlow<EshareConnectedUiState> = _uiState.asStateFlow()
    private var wasStoppedByMobile: Boolean = false

    init {
        eShareRepository.setupEshareListener(this)
        wasStoppedByMobile = false
    }

    private fun updateConnectionState(state: EShareConnectionStatus) =
        _uiState.update { it.copy(connectionState = state) }

    private fun updateBluetoothDeviceState(state: Boolean) =
        _uiState.update { it.copy(deviceConnectionState = DeviceConnectionState(state)) }

    private fun updateBluetoothRadioState(state: Boolean) = _uiState.update {
        it.copy(
            radioState = RadioState(
                isBtEnabled = state,
                isWifiEnabled = it.radioState.isWifiEnabled
            )
        )
    }

    private fun updateWifiRadioState(state: Boolean) = _uiState.update {
        it.copy(
            radioState = RadioState(
                isBtEnabled = it.radioState.isBtEnabled,
                isWifiEnabled = state
            )
        )
    }

    override fun onEshareStateChanged(state: EShareConnectionStatus) {
        Log.i(_tag, "onEshareStateChanged: $state")
        updateConnectionState(state)
    }

    override fun onEshareStateRequested(state: EShareConnectionStatus) {
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
        Log.i(_tag, "onInputStreamCreated: ")
        if (surfaceTexture != null) {
            Log.d(_tag, "onInputStreamCreated: surfaceTexture is not null")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                Log.d(_tag, "onInputStreamCreated: starting stream")
                eShareRepository.startStreamFromHMD(Surface(surfaceTexture!!), inputStream)
            }, 1000)
        }
    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
//        Log.i(_tag, "onSurfaceTextureAvailable: $width $height")
        this.surfaceTexture = surfaceTexture
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
//        Log.i(_tag, "onSurfaceTextureSizeChanged: ")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//        Log.i(_tag, "onSurfaceTextureDestroyed: ")
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//        Log.i(_tag, "onSurfaceTextureUpdated: ")
    }

    fun navigateToStoppedRoute(navController: NavController) = when (wasStoppedByMobile) {
        true -> gotoMainScreen(navController, EShareNavigation.ConnectedRoute)
        false -> navController.navigate(EShareNavigation.ConnectionStoppedRoute)
    }

    fun navigateToBusyRoute(navController: NavController) = with(navController) {
        navigate(EShareNavigation.RemoteBusyRoute, EShareNavigation.ConnectedRoute)
    }

    fun navigateToUnableToConnectRoute(navController: NavController) = with(navController) {
        navigate(EShareNavigation.UnableToConnectRoute, EShareNavigation.ConnectedRoute)
    }

    fun startEshareConnection() {
        Log.i(_tag, "startEshareConnection: ")
        eShareRepository.startEshareConnection()
    }

    fun onCancelButtonClicked() {
        Log.i(_tag, "onCancelButtonClicked: ")
        wasStoppedByMobile = true
        eShareRepository.cancelEshareConnection()
    }

    fun upButtonPress() {
        eShareRepository.writeUpButtonPress()
    }

    fun downButtonPress() {
        eShareRepository.writeDownButtonPress()
    }

    fun volUpButtonPress() {
        eShareRepository.writeVolumeUpButtonPress()
    }

    fun volDownButtonPress() {
        eShareRepository.writeVolumeDownButtonPress()
    }

    fun modeButtonPress() {
        eShareRepository.writeModeButtonPress()
    }

    fun menuButtonPress() {
        eShareRepository.writeMenuButtonPress()
    }

    fun finderButtonPress() {
        eShareRepository.writeFinderButtonPress()
    }

    fun actionUpButtonPress() {
        eShareRepository.writeActionUpEvent()
    }
}
