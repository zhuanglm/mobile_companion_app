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
