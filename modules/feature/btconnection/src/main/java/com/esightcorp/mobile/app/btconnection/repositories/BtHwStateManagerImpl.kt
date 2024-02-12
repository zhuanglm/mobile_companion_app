/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.repositories

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Manager to check Bluetooth hardware status (enabled/disabled)
 */
class BtHwStateManagerImpl(
    private val btRepo: BtConnectionRepository
) : IBtHwStateManager {

    private val _tag = this.javaClass.simpleName

    private val _uiState = MutableStateFlow(BtUiState())
    override val btUiState: StateFlow<BtUiState> = _uiState.asStateFlow()

    private var _enableCheck = false

    init {
        with(btRepo) {
            registerListener(
                object : BluetoothConnectionRepositoryCallback {
                    override fun onBtStateUpdate(enabled: Boolean) {
                        if (!_enableCheck) return

                        Log.w(_tag, "=>> Bluetooth status enabled: $enabled")
                        _uiState.update {
                            it.copy(
                                state = when (enabled) {
                                    true -> BtUiState.BtHwState.ENABLED
                                    else -> BtUiState.BtHwState.DISABLED
                                },
                            )
                        }
                    }
                },
            )

            setupBtModelListener()
        }
    }

    override fun checkBtHwState() {
        _enableCheck = true
        btRepo.checkBtEnabledStatus()
    }
}
