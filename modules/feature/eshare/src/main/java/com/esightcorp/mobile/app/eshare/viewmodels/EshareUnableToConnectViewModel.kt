package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.navigation.EshareScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class EshareUnableToConnectViewModel @Inject constructor(
    application: Application
):AndroidViewModel(application){

    fun onSetupHotspotPressed(navController: NavController){
        navController.navigate(EshareScreens.HotspotSetupRoute.route){
            popUpTo(EshareScreens.EshareUnableToConnectRoute.route){
                inclusive = true
            }
        }
    }



}