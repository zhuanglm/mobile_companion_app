/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.settings.repositories

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getApplicationVersion(): String? = try {
        val packInf = context.packageManager.getPackageInfo(context.packageName, 0)

        "${packInf.versionName} (${packInf.longVersionCode})"
    } catch (ex: Exception) {
        null
    }
}
