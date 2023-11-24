package com.esightcorp.mobile.app.ui.components.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.ui.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class ESightBaseViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    fun gotoMainScreen(
        nav: NavController,
        popUntil: Navigation? = null,
        popIncluded: Boolean = true,
    ) = with(nav) {
        navigate(HomeNavigation.FirstScreenRoute, popUntil, popIncluded)
    }

}
