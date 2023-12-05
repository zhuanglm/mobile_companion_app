package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import javax.inject.Inject

class EshareWifiSetupViewModel @Inject constructor(
    application: Application,
) : EshareViewModel(application) {

    fun gotoWifiSetup(navController: NavController) = with(navController) {
        navigate(
            target = WifiNavigation.ScanningRoute,
            param = WifiNavigation.ScanningRoute.PARAM_BLUETOOTH,
        )
    }

}
