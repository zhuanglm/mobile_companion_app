/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun ExecuteOnce(
    key: Any? = null,
    block: suspend CoroutineScope.() -> Unit,
) = when (key) {
    null -> LaunchedEffect(Unit, block)
    else -> LaunchedEffect(key1 = key, block)
}
