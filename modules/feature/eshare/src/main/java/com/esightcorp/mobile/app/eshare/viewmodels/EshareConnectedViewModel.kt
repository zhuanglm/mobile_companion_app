/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.repositories.EShareRepoManager
import com.esightcorp.mobile.app.eshare.repositories.EShareRepoManagerImpl
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.state.RadioState
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EshareConnectedViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository
) : ESightBaseViewModel(application), EshareRepositoryListener, SurfaceTextureListener,
    EShareRepoManager by EShareRepoManagerImpl(eShareRepository) {

    private val _tag = this.javaClass.simpleName

    private var surfaceTexture: SurfaceTexture? = null

    private val _uiState = MutableStateFlow(EshareConnectedUiState())
    val uiState: StateFlow<EshareConnectedUiState> = _uiState.asStateFlow()
    private var wasStoppedByMobile: Boolean = false

    init {
        eShareRepository.setupEshareListener(this)

        configureBtConnectionListener {
            // Callback when disconnection happen
            _uiState.update { it.copy(isDeviceConnected = false) }
        }
    }

    //region EshareRepositoryListener

    override fun onEshareStateChanged(state: EShareConnectionStatus) {
        Log.i(_tag, "onEshareStateChanged: $state")
        updateConnectionState(state)
    }

    override fun onEshareStateRequested(state: EShareConnectionStatus) {
        updateConnectionState(state)
    }

    override fun onBluetoothDisabled() {
        _uiState.update {
            it.copy(
                radioState = RadioState(
                    isBtEnabled = false,
                    isWifiEnabled = it.radioState.isWifiEnabled
                )
            )
        }
    }

    override fun onWifiStateChanged(state: Boolean) {
        updateWifiRadioState(state)
    }

    override fun onInputStreamCreated(inputStream: InputStream) {
        Log.i(_tag, "onInputStreamCreated")
        surfaceTexture?.let {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    Log.d(_tag, "onInputStreamCreated: starting stream")
                    eShareRepository.startStreamFromHMD(Surface(surfaceTexture!!), inputStream)
                },
                1000,
            )
        } ?: run {
            Log.e(_tag, "surfaceTexture is NULL", Exception())
        }
    }

    //endregion

    //region SurfaceTextureListener

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

    //endregion

    fun startEshareConnection() = viewModelScope.launch(Dispatchers.IO) {
        delay(1000)

        // Request to start eShare session
        eShareRepository.startEshareConnection()

        connectionTimer = startConnectionTimer()
    }

    fun cancelEshareConnection() {
        stopConnectionTimer()

        eShareRepository.cancelEshareConnection()
    }

    fun onCancelButtonClicked(navController: NavController) {
        Log.i(_tag, "onCancelButtonClicked")
        wasStoppedByMobile = true
        cancelEshareConnection()

        navController.popBackStack()
    }

    fun upButtonPress() = eShareRepository.writeUpButtonPress()

    fun downButtonPress() = eShareRepository.writeDownButtonPress()

    fun volUpButtonPress() = eShareRepository.writeVolumeUpButtonPress()

    fun volDownButtonPress() = eShareRepository.writeVolumeDownButtonPress()

    fun modeButtonPress() = eShareRepository.writeModeButtonPress()

    fun menuButtonPress() = eShareRepository.writeMenuButtonPress()

    fun finderButtonPress() = eShareRepository.writeFinderButtonPress()

    fun actionUpButtonPress() = eShareRepository.writeActionUpEvent()

    //region Navigation

    fun navigateToStoppedRoute(navController: NavController, reason: EShareStoppedReason? = null) {
        when (wasStoppedByMobile) {
            true -> gotoMainScreen(navController, EShareNavigation.ConnectedRoute)
            false -> {
                navController.navigate(
                    target = EShareNavigation.ConnectionStoppedRoute,
                    param = reason?.name
                )
            }
        }
    }

    fun navigateToUnableToConnectRoute(navController: NavController) =
        navController.navigate(EShareNavigation.UnableToConnectRoute)

    fun navigateToWifiDisabledRoute(navController: NavController) =
        navController.navigate(EShareNavigation.WifiDisabledRoute)

    fun navigateToWifiSetupRoute(navController: NavController) = navController.navigate(
        target = EShareNavigation.WifiSetupRoute
    )
    //endregion

    //region Internal implementation

    private var connectionTimer: Job? = null
        @Synchronized get
        @Synchronized set

    @Synchronized
    private fun updateConnectionState(state: EShareConnectionStatus) {
        if (state == EShareConnectionStatus.Connected)
            stopConnectionTimer()

        _uiState.update { it.copy(connectionState = state) }
    }

    @Synchronized
    private fun getConnectionState() = _uiState.value.connectionState

    private fun updateWifiRadioState(state: Boolean) = _uiState.update {
        it.copy(
            radioState = RadioState(
                isBtEnabled = it.radioState.isBtEnabled,
                isWifiEnabled = state
            )
        )
    }

    private fun startConnectionTimer() = viewModelScope.launch(Dispatchers.IO) {
        Log.e(_tag, "Eshare connection timer started ...")

        delay(CONNECTION_ESTABLISHING_TIMEOUT)

        if (getConnectionState() != EShareConnectionStatus.Connected) {
            Log.e(_tag, "Eshare connection timeout!")
            updateConnectionState(EShareConnectionStatus.Timeout)
        }
    }

    @Synchronized
    private fun stopConnectionTimer() {
        connectionTimer?.cancel()
        connectionTimer = null
        Log.w(_tag, "Eshare connection timer cancelled!")
    }
    //endregion

    companion object {
        private const val CONNECTION_ESTABLISHING_TIMEOUT = 60 * 1000L
    }
}
