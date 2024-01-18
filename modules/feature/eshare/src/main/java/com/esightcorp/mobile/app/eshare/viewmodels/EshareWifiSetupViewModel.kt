package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EshareWifiSetupViewModel @Inject constructor(
    application: Application,
    eshareRepo: EshareRepository,
) : EshareViewModel(application, eshareRepo) {

    init {
        initialize()
    }

    fun gotoWifiSetup(navController: NavController) = with(navController) {
        navigate(
            target = WifiNavigation.ScanningRoute,
            param = WifiNavigation.ScanningRoute.PARAM_BLUETOOTH,
        )
    }
}
