package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import com.esightcorp.mobile.app.ui.extensions.navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EshareUnableToConnectViewModel @Inject constructor(
    application: Application
) : ESightBaseViewModel(application) {

    fun onSetupHotspotPressed(navController: NavController) = with(navController) {
        navigate(EShareNavigation.HotspotSetupRoute, EShareNavigation.UnableToConnectRoute)
    }

}
