/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils.permission

import android.content.Context
import android.location.LocationManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocationServiceManagerImpl(
    private val context: Context,
) : ILocationServiceManager {
    private val _tag = this.javaClass.simpleName

    private val _locationServiceState = MutableStateFlow<Boolean?>(null)

    override val isLocationServiceEnabled: StateFlow<Boolean?>
        get() = _locationServiceState.asStateFlow()

    override fun verifyLocationServiceState() {
        val locationService = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        _locationServiceState.update { locationService.isLocationEnabled }
        Log.d(_tag, "verifyLocationServiceState -> ${_locationServiceState.value}")
    }
}
