package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtDisabledUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtDisabledViewModel  @Inject constructor(
    application: Application,
    val btConnectionRepository: BtConnectionRepository
): AndroidViewModel(application){
    private lateinit var navController: NavController

    private var _uiState = MutableStateFlow(BtDisabledUiState())
    val uiState: StateFlow<BtDisabledUiState> = _uiState.asStateFlow()


    fun setNavController(controller: NavController){
        this.navController = controller
    }

    fun onBackPressed(){
        if(this::navController.isInitialized){
            navController.navigate(BtConnectionScreens.NoDevicesConnectedRoute.route)
        }
    }

    fun updateBtEnabledState(state: Boolean){
        _uiState.update {
            it.copy(isBtEnabled = state)
        }
    }
}