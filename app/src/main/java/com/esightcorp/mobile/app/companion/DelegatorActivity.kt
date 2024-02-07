/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.companion

import android.app.ActivityManager
import android.app.ApplicationExitInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.companion.navigation.toplevel.TopLevelNavigation
import com.esightcorp.mobile.app.ui.KEY_IS_ORIENTATION_CHANGING
import com.esightcorp.mobile.app.ui.components.theme.Mobile_companion_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DelegatorActivity : ComponentActivity() {
    private val _tag = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
//        Log.w(_tag, "onCreate - input intent: $intent")
        val processedBundle = preProcessSavedInstanceState(this, savedInstanceState)
        Log.d(_tag, "onCreate - Saved state: $processedBundle")
//        Log.w(_tag, "onCreate - updated intent: $intent")

        super.onCreate(processedBundle)

        setContent {
            CompanionApp()
        }
    }

    /**
     * Preprocessing the saved state bundle.
     * This helps in case the user revoke permission while the app is in background.
     */
    private fun preProcessSavedInstanceState(context: Context, savedState: Bundle?) =
        when (savedState) {
            null -> null

            else -> {
                val isOrientationChanging =
                    intent?.extractBooleanExtra(KEY_IS_ORIENTATION_CHANGING, false) ?: false
                Log.w(_tag, "->> isOrientationChanging: $isOrientationChanging")

                when {
                    // API >= 30
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        val latestExitReason =
                            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                                .getHistoricalProcessExitReasons(context.packageName, 0, 0)
                                .firstOrNull()
                        Log.w(_tag, "latestExitReason: $latestExitReason")

                        when (latestExitReason?.reason) {
                            ApplicationExitInfo.REASON_PERMISSION_CHANGE -> null
                            else -> savedState
                        }
                    }

                    // API < 30
                    else -> when (isOrientationChanging) {
                        true -> savedState

                        //TODO: any better way to use the `savedState` instead of always null like this???
                        else -> null
                    }
                }
            }
        }
}

/**
 * Get the key's value and remove it from the input intent
 */
private fun Intent?.extractBooleanExtra(key: String, defaultValue: Boolean): Boolean? {
    val value = this?.getBooleanExtra(key, defaultValue)
    this?.removeExtra(key)

    return value
}

@Composable
private fun CompanionApp() {
    Mobile_companion_appTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                TopLevelNavigation()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CompanionApp()
}
