package com.esightcorp.mobile.app.btconnection.repositories

sealed class ScanningStatus{
    object Failed : ScanningStatus()
    object InProgress : ScanningStatus()
    object Success: ScanningStatus()
    object Unknown: ScanningStatus()
}
