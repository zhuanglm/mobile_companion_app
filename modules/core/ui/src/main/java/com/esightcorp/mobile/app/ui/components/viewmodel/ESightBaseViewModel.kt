/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.ui.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class ESightBaseViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    fun gotoMainScreen(
        nav: NavController,
        popUntil: Navigation? = null,
        popIncluded: Boolean = true,
    ) = with(nav) {
        navigate(HomeNavigation.FirstScreenRoute, popUntil, popIncluded)
    }

    fun gotoEsightSupportSite() = with(application.applicationContext) {
        openExternalUrl(getString(com.esightcorp.mobile.app.ui.R.string.url_esight_support))
    }
}
