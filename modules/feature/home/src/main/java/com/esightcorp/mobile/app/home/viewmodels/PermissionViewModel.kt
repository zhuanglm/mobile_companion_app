/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.state.HomePermissionUiState
import com.esightcorp.mobile.app.home.state.HomePermissionUiState.RationaleReason
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.EnumSet
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {
    private val _tag = this.javaClass.simpleName

    val requiredPermissions: List<String>
        get() = arrayListOf<String>().apply {
            addAll(locationPermissions)
            addAll(bluetoothPermissions)
        }

    private val _uiState = MutableStateFlow(HomePermissionUiState())
    val uiState: StateFlow<HomePermissionUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalPermissionsApi::class)
    fun debugPermissionsState(states: MultiplePermissionsState) = with(states) {
        Log.i(
            _tag,
            "allGranted: ${allPermissionsGranted}\n" +
                    "shouldShowRationale: $shouldShowRationale\n" +
                    "revoked permissions:\n${
                        revokedPermissions.joinToString(separator = "\n") {
                            "\t${it.permission} (rationale: ${it.status.shouldShowRationale})"
                        }
                    }",
        )
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun verifyPermissionStates(states: MultiplePermissionsState) = with(states) {
        debugPermissionsState(states)

        when (allPermissionsGranted) {
            true -> _uiState.update {
                it.copy(allPermissionsGranted = true, rationaleReasons = null)
            }

            else -> when (shouldShowRationale) {
                true -> _uiState.update {
                    it.copy(
                        rationaleReasons = getRationaleReasons(revokedPermissions.map { p -> p.permission }),
                        allPermissionsGranted = null,
                    )
                }

                // Clean the state so that UI will show instruction screen
                false -> Unit
            }
        }
    }

    fun onPermissionsUpdated(status: Map<String, Boolean>) {
        val revokedPermissions = status.filter { (_, grated) -> !grated }.map { it.key }
        Log.d(_tag, "onPermissionsUpdated - revokedPermissions: $revokedPermissions")

        when (revokedPermissions.isEmpty()) {
            true -> _uiState.update { it.copy(allPermissionsGranted = true) }

            false -> _uiState.update {
                it.copy(
                    rationaleReasons = getRationaleReasons(revokedPermissions),
                    allPermissionsGranted = null,
                )
            }
        }
    }

    fun navigateToBluetoothConnection(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.IncomingRoute)
    }

    fun navigateToAppSettings(launcher: ActivityResultLauncher<Intent>) = with(application) {
        val uri = Uri.Builder()
            .scheme("package")
            .opaquePart(packageName)
            .build()
        launcher.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri))
    }

    //region Private implementation

    private val locationPermissions = listOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private val bluetoothPermissions = arrayListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(android.Manifest.permission.BLUETOOTH_SCAN)
            add(android.Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            add(android.Manifest.permission.BLUETOOTH)
            add(android.Manifest.permission.BLUETOOTH_ADMIN)
        }
    }

    private fun getRationaleReasons(revokedPermissions: List<String>?): EnumSet<RationaleReason> {
        val reasons = EnumSet.noneOf(RationaleReason::class.java)

        revokedPermissions?.let {
            if (revokedPermissions.any { locationPermissions.contains(it) })
                reasons.add(RationaleReason.FOR_LOCATION)

            if (revokedPermissions.any { bluetoothPermissions.contains(it) })
                reasons.add(RationaleReason.FOR_BLUETOOTH)
        }

        return reasons
    }
    //endregion
}
