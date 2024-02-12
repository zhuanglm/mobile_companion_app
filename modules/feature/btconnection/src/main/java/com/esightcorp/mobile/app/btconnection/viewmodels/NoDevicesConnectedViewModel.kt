/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.util.Log
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import com.esightcorp.mobile.app.utils.permission.IPermissionManager
import com.esightcorp.mobile.app.utils.permission.PermissionManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoDevicesConnectedViewModel @Inject constructor(
    application: Application,
    private val btRepo: BtConnectionRepository,
) : ESightBaseViewModel(application),
    IPermissionManager by PermissionManagerImpl(application, btRepo.bluetoothPermissions) {

    private val _tag = this.javaClass.simpleName

//    private val _uiState = MutableStateFlow(BtUiState())
//    val uiState: StateFlow<BtUiState> = _uiState.asStateFlow()

    private var currentBtStage = BtStatusVerifyStage.UNKNOWN

    private val btRepoListener = object : BluetoothConnectionRepositoryCallback {
        override fun onBtStateUpdate(enabled: Boolean) {
            Log.i(_tag, "onBtStateUpdate - enabled: $enabled, stage: $currentBtStage")

            if (currentBtStage != BtStatusVerifyStage.CHECK_BT_HW_STATUS) return

//            when (enabled) {
//                false -> _uiState.update { it.copy(bluetoothState = BluetoothState.DISABLED) }
//
//                true -> {
//                    // Move to next stage checking permission
//                    currentBtStage = BtStatusVerifyStage.CHECK_BT_PERMISSION
//                    _uiState.update { it.copy(bluetoothState = null) }
//                }
//            }
        }
    }

    init {
        btRepo.registerListener(btRepoListener)
    }

    //region Bluetooth usage

    enum class BtStatusVerifyStage {
        UNKNOWN,

        CHECK_BT_HW_STATUS,
        CHECK_BT_PERMISSION,
    }

    private fun verifyBtStatus() {
        currentBtStage = BtStatusVerifyStage.CHECK_BT_HW_STATUS
        btRepo.checkBtEnabledStatus()
    }
    //endregion

    fun onScanEsightClicked(nav: NavController) {
        verifyBtStatus()

        if (currentBtStage != BtStatusVerifyStage.CHECK_BT_PERMISSION) return

        Log.i(_tag, "Bt is enabled, proceed checking permission ...")
    }

    //region Navigation

    fun navigateToBtDisabled(nav: NavController) = with(nav) {
        navigate(BtConnectionNavigation.BtDisabledScreen)
    }

    fun navigateToBtScanning(nav: NavController) = with(nav) {
        navigate(BtConnectionNavigation.BtSearchingRoute)
    }

    fun navigateToBtPermission(nav: NavController) = with(nav) {
        navigate(target = BtConnectionNavigation.BtPermissionRoute, popCurrent = true)
    }

    fun navigateToSettings(nav: NavController) = nav.navigate(SettingsNavigation.IncomingRoute)

    //endregion

}
