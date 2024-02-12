/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtSearchingUiState
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class BtSearchingViewModel @Inject constructor(
    application: Application,
    private val btConnectionRepository: BtConnectionRepository,
) : AndroidViewModel(application) {
    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(BtSearchingUiState())
    val uiState: StateFlow<BtSearchingUiState> = _uiState.asStateFlow()
    private val btRepositoryListener = object : BluetoothConnectionRepositoryCallback {
        override fun scanStatus(isScanning: ScanningStatus) {
            Log.i(_tag, "Scan status received from Bluetooth Repository")
            updateBtSearchingState(isScanning)
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            Log.i(_tag, "deviceListReady: ")
            //Once we have a device, wait minimum 2 seconds before showing the list
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                updateBtSearchingState(ScanningStatus.Success)
            }, 2000)
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            _uiState.update { it.copy(isBtEnabled = enabled) }
        }
    }

    init {
        with(btConnectionRepository) {
            registerListener(btRepositoryListener)
            setupBtModelListener()
            resetBtDeviceList()
        }
    }

    fun triggerScan() {
        btConnectionRepository.triggerBleScan()
    }

    private fun updateBtSearchingState(status: ScanningStatus) {
        when (status) {
            ScanningStatus.Failed -> {
                Log.d(_tag, "updateBtSearchingState: ${ScanningStatus.Failed}")
            }

            ScanningStatus.InProgress -> {
                Log.d(_tag, "updateBtSearchingState: ${ScanningStatus.InProgress}")
            }

            ScanningStatus.Success -> {
                Log.d(_tag, "updateBtSearchingState: ${ScanningStatus.Success}")
            }

            ScanningStatus.Unknown -> {
                Log.d(_tag, "updateBtSearchingState: ${ScanningStatus.Unknown}")
            }

            ScanningStatus.Cancelled -> {
                Log.d(_tag, "updateBtSearchingState: ${ScanningStatus.Cancelled}")
            }
        }

        _uiState.update {
            it.copy(isScanning = status)
        }
    }

    fun onCancelButtonClicked(navController: NavController) {
        //navigate back to the 'no devices connected screen'
        navController.navigate(BtConnectionNavigation.IncomingRoute)

        //cleanup bluetooth scanning
        btConnectionRepository.cancelBleScan()
    }

    fun onScanSuccess(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.ScanResultRoute)
    }

    fun navigateToBtDisabled(navController: NavController) = with(navController) {
        navigate(target = BtConnectionNavigation.BtDisabledScreen)
    }
}
