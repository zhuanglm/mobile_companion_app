/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils.permission

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import kotlinx.coroutines.flow.StateFlow

interface IPermissionManager {
    val permissionUiState: StateFlow<PermissionUiState>

    fun registerPermissionLauncher(launcher: PermissionLauncher, activity: Activity? = null)
    fun cleanUp()

    fun initPermissionCheck()

    fun onPermissionsUpdated(status: Map<String, Boolean>)

    fun gotoAppSetting(launcherInf: ManagedActivityResultLauncher<Intent, ActivityResult>)
    fun onAppSettingResult(result: ActivityResult)
}

typealias PermissionLauncher = ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>

data class PermissionUiState(
    val state: PermissionState? = null
) {
    enum class PermissionState {
        SHOW_RATIONALE,
        GRANTED,
    }
}
