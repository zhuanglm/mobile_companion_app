package com.esightcorp.mobile.app.utils

sealed class ScanningStatus{
    object Failed : ScanningStatus()
    object InProgress : ScanningStatus()
    object Success: ScanningStatus()
    object Unknown: ScanningStatus()
    object Cancelled: ScanningStatus()
}
