package com.esightcorp.mobile.app.wificonnection

import com.google.accompanist.permissions.*

@ExperimentalPermissionsApi
fun PermissionStatus.isPermanentlyDenied():Boolean {
    return !shouldShowRationale && !isGranted
}