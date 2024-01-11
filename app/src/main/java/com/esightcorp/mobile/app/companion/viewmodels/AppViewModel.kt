package com.esightcorp.mobile.app.companion.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.companion.repositories.AppRepository
import com.esightcorp.mobile.app.companion.repositories.IAppRepoListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    application: Application,
    appRepo: AppRepository,
) : AndroidViewModel(application) {
    private val _tag = this.javaClass.simpleName

    private var navController: NavController? = null

    init {
        Log.w(_tag, "bleEventListener - AppViewModel: $appRepo")

        appRepo.appListener = object : IAppRepoListener {
            override fun onDisconnected() {
                // NOTE: StateFlow does not work here since when this callback is triggered,
                // the view is gone already!

                Log.e(_tag, "bleEventListener - onDisconnected, nav: $navController")
            }
        }
    }

    fun setNavController(nav: NavController) {
        navController = nav
    }
}
