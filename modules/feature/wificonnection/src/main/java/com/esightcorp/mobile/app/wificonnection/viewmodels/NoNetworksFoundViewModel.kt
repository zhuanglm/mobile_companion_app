package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.NoNetworksFoundUiState
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NoNetworksFoundViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
):AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(NoNetworksFoundUiState())
    val uiState: StateFlow<NoNetworksFoundUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController

    fun setNavController(navController: NavController){
        this.navController = navController
    }

    fun navigateHome(){
        if(this::navController.isInitialized){
            navController.navigate("home_first")
        }
    }

    fun tryAgain(){
        if(this::navController.isInitialized){
            navController.navigate(WifiConnectionScreens.SearchingForNetworkRoute.route + "/bluetooth")
        }
    }

}