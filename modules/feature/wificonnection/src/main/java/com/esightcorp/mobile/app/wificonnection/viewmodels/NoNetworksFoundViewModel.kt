/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoNetworksFoundViewModel @Inject constructor(
    application: Application,
    private val wifiRepo: WifiConnectionRepository,
) : ESightBaseViewModel(application),
    WifiBleConnectionStateManager by WifiBleConnectionStateManagerImpl(wifiRepo) {

    init {
        wifiRepo.registerListener(
            object : WifiNetworkScanListener {
                override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
                    updateBleConnectionState(isConnected)
                }

                override fun onWifiStatusUpdate(status: Boolean) {}

                override fun onNetworkListUpdated(list: MutableList<ScanResult>) {}

                override fun onScanStatusUpdated(status: ScanningStatus) {}

                override fun onWifiAlreadyConnected(status: Boolean) {}
            },
        )
    }

    fun onBackPressed(navController: NavController) = navController.popBackStack()

    fun tryAgain(navController: NavController) {
        navController.navigate(
            target = WifiNavigation.ScanningRoute,
            param = when (wifiRepo.wifiFlow) {
                WifiCache.WifiFlow.QrFlow -> WifiNavigation.ScanningRoute.PARAM_QR
                else -> WifiNavigation.ScanningRoute.PARAM_BLUETOOTH
            }
        )
    }
}
