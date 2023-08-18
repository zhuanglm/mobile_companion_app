package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.navigation.EshareScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class EshareConnectionStoppedViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {





    init {

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun navigateToNoDevicesConnectedScreen(navController: NavController){
        navController.popBackStack("home_first", false)
    }




}