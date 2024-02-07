/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoDevicesConnectedViewModel @Inject constructor(
    private val application: Application,
) : ESightBaseViewModel(application) {

    //region Navigation

    fun showFeedbackPage() = with(application.applicationContext) {
        openExternalUrl(getString(R.string.url_esight_feedback))
    }

    fun navigateToScanESight(nav: NavController) = with(nav) {
        navigate(BtConnectionNavigation.BtSearchingRoute)
    }

    fun navigateToSettings(nav: NavController) = nav.navigate(SettingsNavigation.IncomingRoute)


    fun showExternalUrl(urlId: Int) =
        with(application.applicationContext) { openExternalUrl(getString(urlId)) }

    fun openTerms(){
        showExternalUrl(R.string.url_esight_privacy_policy)
    }

    fun openPrivacyPolicy(){
        showExternalUrl(R.string.url_esight_privacy_policy)
    }

    //endregion

}
