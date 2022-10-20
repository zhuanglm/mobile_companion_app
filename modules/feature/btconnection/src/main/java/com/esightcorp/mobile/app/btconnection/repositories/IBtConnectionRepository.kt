package com.esightcorp.mobile.app.btconnection.repositories

interface IBtConnectionRepository {
    fun scanStatus(isScanning: ScanningStatus)
    fun deviceListReady(deviceList: HashMap<String, Boolean>)
}