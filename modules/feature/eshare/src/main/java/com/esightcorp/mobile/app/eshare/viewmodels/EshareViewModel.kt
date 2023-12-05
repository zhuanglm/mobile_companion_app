package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class EshareViewModel @Inject constructor(
    application: Application,
) : ESightBaseViewModel(application) {

    fun onSetupHotspotPressed(navController: NavController) =
        navController.navigate(EShareNavigation.HotspotSetupRoute)

    fun onRetryPressed(navController: NavController) =
        navController.navigate(EShareNavigation.IncomingRoute)

}
