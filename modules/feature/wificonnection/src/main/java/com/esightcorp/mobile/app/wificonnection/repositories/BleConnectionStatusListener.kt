package com.esightcorp.mobile.app.wificonnection.repositories

interface BleConnectionStatusListener {
    /**
     * Callback to update BLE remote device connection status
     *
     * @param isConnected
     *   - `true`: remote BLE device connected
     *   - `false`: remote BLE device disconnected
     */
    fun onBleConnectionStatusUpdate(isConnected: Boolean) {}
}
