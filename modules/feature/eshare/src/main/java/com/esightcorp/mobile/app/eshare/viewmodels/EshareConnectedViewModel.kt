package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.state.EshareConnectingUiState
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.Closeable
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "EshareConnectedViewMode"
@HiltViewModel
class EshareConnectedViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository

): AndroidViewModel(application), EshareRepositoryListener, SurfaceTextureListener {

    private var surfaceTexture: SurfaceTexture? = null
    var inputStream: InputStream? = null

    private var _uiState = MutableStateFlow(EshareConnectedUiState())
    val uiState: StateFlow<EshareConnectedUiState> = _uiState.asStateFlow()


    init {
        eShareRepository.setupEshareListener(this)
    }



    override fun onEshareStateChanged(state: eShareConnectionStatus) {
        Log.i(TAG, "onEshareStateChanged: $state")
        updateConnectionState(state)

    }

    private fun updateConnectionState(state: eShareConnectionStatus){
        _uiState.update { uiState ->
            uiState.copy(connectionState = state)
        }
    }

    override fun onEshareStateRequested(state: eShareConnectionStatus) {
        TODO("Not yet implemented")
    }

    override fun onBluetoothStateChanged(state: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onWifiStateChanged(state: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onInputStreamCreated(inputStream: InputStream) {
        Log.i(TAG, "onInputStreamCreated: ")
        if(surfaceTexture != null){
            Log.d(TAG, "onInputStreamCreated: surfaceTexture is not null")
            eShareRepository.startStreamFromHMD(Surface(surfaceTexture!!), inputStream)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureAvailable: ")
        this.surfaceTexture = surface

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
}


