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
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    val requiredPermissions: List<String>
        get() = arrayListOf<String>().apply {
            add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            add(android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(android.Manifest.permission.BLUETOOTH_SCAN)
                add(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                add(android.Manifest.permission.BLUETOOTH)
                add(android.Manifest.permission.BLUETOOTH_ADMIN)
            }
        }

    fun navigateToBluetoothConnection(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.IncomingRoute)
    }

    fun navigateToAppSettings() {
        with(application) {
            val uri = Uri.Builder()
                .scheme("package")
                .opaquePart(packageName)
                .build()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }
}
