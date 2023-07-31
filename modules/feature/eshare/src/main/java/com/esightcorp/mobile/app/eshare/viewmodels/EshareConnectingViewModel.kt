package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.EshareConnectingUiState
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EshareConnectingViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository

): AndroidViewModel(application){

    private val TAG = "EshareConnectingViewMod"

    private var _uiState = MutableStateFlow(EshareConnectingUiState())
    val uiState: StateFlow<EshareConnectingUiState> = _uiState.asStateFlow()
    private val eshareRepositoryListener = object : EshareRepositoryListener{
        override fun onEshareStateChanged(state: eShareConnectionStatus) {
            TODO("Not yet implemented")
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
    }

//    init {
//        eShareRepository.setupEshareListener(eshareRepositoryListener)
//    }

    fun startEshareConnection(){
        eShareRepository.startEshareConnection()
    }

    fun onCancelClicked(){
        eShareRepository.cancelEshareConnection()
    }

    override fun onCleared() {
        super.onCleared()
    }


}