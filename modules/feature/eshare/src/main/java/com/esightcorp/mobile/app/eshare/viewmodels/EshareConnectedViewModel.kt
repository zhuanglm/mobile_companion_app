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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EshareConnectedViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository
) : ESightBaseViewModel(application), EshareRepositoryListener, SurfaceTextureListener,
    EShareRepoManager by EShareRepoManagerImpl(eShareRepository) {

    private val _tag = this.javaClass.simpleName

    private var surfaceTexture: SurfaceTexture? = null

    /**
     * The streaming latch conditions, which are:
     *   * The surface has been created
     *   * The screen has been rotated into landscape mode properly
     *     (also means that streaming input stream has been established successfully)
     */
    private var streamingLatch: CountDownLatch? = null

    private val _uiState = MutableStateFlow(EshareConnectedUiState())
    val uiState: StateFlow<EshareConnectedUiState> = _uiState.asStateFlow()
    private var wasStoppedByMobile: Boolean = false

    init {
        with(eShareRepository) {
            initialize()
            setupEshareListener(this@EshareConnectedViewModel)
        }

        configureBtConnectionListener {
            // Callback when disconnection happen
            _uiState.update { it.copy(isDeviceConnected = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        eShareRepository.cleanUp()
        Log.d(_tag, "=>> ##### onCleared #####")
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

        /**
         * Input stream is ready, now wait until the screen is in landscape mode
         */
        streamingLatch?.let { latch ->

            // Start a new thread to wait for streaming conditions ready
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(_tag, "streamingLatch await ...")
                    if (!latch.await(STREAMING_INITIALIZING_TIMEOUT, TimeUnit.MILLISECONDS)) {
                        // Timeout!
                        Log.e(_tag, "Waiting latch timeout, report streamingError!")
                        updateConnectionState(EShareConnectionStatus.StreamingError)
                        return@launch
                    }

                    Log.w(_tag, "Streaming ready, go go go ...")

                    surfaceTexture?.let {
                        eShareRepository.startStreamFromHMD(Surface(it), inputStream)
                    } ?: throw Exception("surfaceTexture is null!")
                } catch (ex: Throwable) {
                    Log.e(_tag, "bindSurfaceForStreaming failed!", ex)
                }
            }

        } ?: run {
            Log.e(
                _tag,
                "Something is wrong seriously ... streamingLatch is not initialized yet!",
                Exception()
            )
        }
    }

    //endregion

    //region SurfaceTextureListener

    @Synchronized
    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        this.surfaceTexture = surfaceTexture
        streamingLatch?.countDown()
        Log.i(_tag, "onSurfaceTextureAvailable: ($width, $height) -> $streamingLatch")
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(_tag, "onSurfaceTextureSizeChanged: ($width, $height)")
    }

    @Synchronized
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Log.e(_tag, "onSurfaceTextureDestroyed: ")
        surfaceTexture = null
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

        // Require 2 conditions
        // - The surface is created
        // - The screen is rotated to landscape
        streamingLatch = CountDownLatch(2)
        Log.w(_tag, "startEshareConnection - latch: $streamingLatch")

        connectionTimer = startConnectionTimer()
    }

    fun cancelEshareConnection() {
        stopConnectionTimer()

        eShareRepository.cancelEshareConnection()
        streamingLatch = null
    }

    /**
     * This function is triggered from the UI/screen to inform that the streaming is ready.
     * In this case, it means the screen has been completed rotated into landscape mode.
     */
    @Synchronized
    fun onStreamingReady() {
        streamingLatch?.countDown()
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

        /**
         * Timeout waiting for streaming ready
         */
        private const val STREAMING_INITIALIZING_TIMEOUT = 10000L
    }
}
