/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.esightcorp.mobile.app.utils.permission.PermissionUiState.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionManagerImpl(
    private val context: Context,
    private val permissions: ArrayList<String>,
) : IPermissionManager {

    private val _tag = this.javaClass.simpleName

    private val _uiState = MutableStateFlow(PermissionUiState())
    override val permissionUiState: StateFlow<PermissionUiState> = _uiState.asStateFlow()

    private var permissionLauncher: PermissionLauncher? = null
    private var activity: Activity? = null

    override fun registerPermissionLauncher(launcher: PermissionLauncher, activity: Activity?) {
        permissionLauncher = launcher
        this.activity = activity
    }

    override fun cleanUp() {
        activity = null
        permissionLauncher = null
        Log.w(_tag, "=>> CleanUp done")
    }

    override fun initPermissionCheck() {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (deniedPermissions.isEmpty()) {
            updateUiState(PermissionState.GRANTED)
            return
        }
        Log.w(_tag, deniedPermissions.toString("initPermissionCheck - deniedPermissions"))

        activity?.let {
            val rationalePermissions = permissions.filter { p ->
                ActivityCompat.shouldShowRequestPermissionRationale(it, p)
            }
            Log.w(_tag, rationalePermissions.toString("initPermissionCheck - rationalePermissions"))
            if (rationalePermissions.isNotEmpty()) {
                updateUiState(PermissionState.SHOW_RATIONALE)
                return
            }
        } ?: run {
            Log.e(_tag, "initPermissionCheck - Input activity is null, ignore rationale check!!!")
        }

        // First time request, launch system permission popup
        permissionLauncher?.launch(deniedPermissions.toTypedArray())
        Log.i(_tag, "->> permissionLauncher launched")
    }

    override fun onPermissionsUpdated(status: Map<String, Boolean>) {
        val revokedPermissions = status.filter { (_, grated) -> !grated }.map { it.key }
        Log.d(
            _tag,
            revokedPermissions.toString("onPermissionsUpdated - revokedPermissions")
        )

        when (revokedPermissions.isEmpty()) {
            true -> updateUiState(PermissionState.GRANTED)
            false -> updateUiState(PermissionState.SHOW_RATIONALE)
        }
    }

    override fun gotoAppSetting(launcherInf: ManagedActivityResultLauncher<Intent, ActivityResult>) =
        with(context) {
            launcherInf.launch(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.Builder().scheme("package").opaquePart(packageName).build()
                )
            )
        }

    override fun onAppSettingResult(result: ActivityResult) {
        Log.w(_tag, "->> onAppSettingResult - request latest permission state ...")
        initPermissionCheck()
    }

    private fun updateUiState(value: PermissionState?) = _uiState.update { it.copy(state = value) }
}

private fun List<String>.toString(message: String): String = when (isEmpty()) {
    true -> "$message -> empty"
    false -> "$message\n\t${joinToString(separator = "\n\t") { it }}"
}
