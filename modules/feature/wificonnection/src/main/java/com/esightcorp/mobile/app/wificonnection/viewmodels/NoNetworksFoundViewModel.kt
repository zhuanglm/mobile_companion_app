package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.wificonnection.state.NoNetworksFoundUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NoNetworksFoundViewModel @Inject constructor(
    application: Application,
) : ESightBaseViewModel(application) {

    private var _uiState = MutableStateFlow(NoNetworksFoundUiState())
    val uiState: StateFlow<NoNetworksFoundUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun navigateHome() {
        if (this::navController.isInitialized) {
            gotoMainScreen(navController)
        }
    }

    fun tryAgain() {
        if (this::navController.isInitialized) {
            navController.navigate(
                target = WifiNavigation.ScanningRoute,
                param = WifiNavigation.ScanningRoute.PARAM_BLUETOOTH,
            )
        }
    }

}