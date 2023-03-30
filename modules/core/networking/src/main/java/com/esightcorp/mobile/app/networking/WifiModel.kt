package com.esightcorp.mobile.app.networking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BleService
import com.esightcorp.mobile.app.utils.ScanningStatus

private const val TAG = "WifiModel"

class WifiModel(
    val context: Context
) {

    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }
    private val wifiStateChangeReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                when (wifiState) {
                    WifiManager.WIFI_STATE_ENABLED -> {
                        // Do something when WiFi is enabled
                        listener?.onWifiEnabled()
                    }
                    WifiManager.WIFI_STATE_DISABLED -> {
                        // Do something when WiFi is disabled
                        listener?.onWifiDisabled()
                    }
                }
            }
        }
    }
    private val wifiStateIntentFilter  = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BleService.ACTION_GATT_CONNECTED -> {
                Log.e(TAG, "onReceive: CONNECTED")
                }
                BleService.ACTION_GATT_DISCONNECTED -> {
                    Log.e(TAG, "onReceive: DISCONNECTED")
                    listener?.onBluetoothDisconnected()
                }
                BleService.ACTION_WIFI_CONNECTED -> {
                    Log.e(TAG, "onReceive: WIFI CONNECTED ")
                    listener?.onNetworkConnected()
                }
                BleService.ACTION_WIFI_ERROR -> {
                    Log.e(TAG, "onReceive: WIFI_ERROR")
                    listener?.onNetworkConnectionError()
                }
                BleService.ACTION_ERROR -> {
                    Log.e(TAG, "onReceive: ERROR  " + intent.data.toString())
                    when(intent.data.toString()){
                        ERROR_PLATFORM -> {
                            Log.e(TAG, "onReceive: ERROR_PLATFORM")
                            listener?.onPlatformError()
                        }
                        ERROR_WIFI_NETWORK_NOT_FOUND -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_NETWORK_NOT_FOUND")
                            listener?.onNetworkNotFound()
                        }
                        ERROR_WIFI_DISABLED -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_DISABLED")
                            listener?.onGoWifiDisabled()
                        }
                        ERROR_BLUETOOTH_DISABLED -> {
                            Log.e(TAG, "onReceive: ERROR_BLUETOOTH_DISABLED")
                            listener?.onBluetoothDisconnected()
                        }
                        ERROR_WIFI_WPA_LESS_THAN_8 -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_WPA_LESS_THAN_8")
                            listener?.onErrorWifiWPALessThan8()
                        }
                        ERROR_WIFI_INVALID_PASSWORD -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_INVALID_PASSWORD")
                            listener?.onErrorWifiInvalidPassword()
                        }
                        ERROR_WIFI_CONNECTION_TIMEOUT -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_CONNECTION_TIMEOUT")
                            listener?.onErrorWifiConnectionTimeout()
                        }
                        ERROR_TEST -> {
                            Log.e(TAG, "onReceive: ERROR_TEST")
                            listener?.onErrorTest()
                        }
                        ERROR_NONE -> {
                            Log.e(TAG, "onReceive: ${intent.data.toString()}  <<<<------>>>> Error Characteristic has been reset. ")
                        }
                    }

                }

            }

        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            addAction(BleService.ACTION_GATT_CONNECTED)
            addAction(BleService.ACTION_GATT_DISCONNECTED)
            addAction(BleService.ACTION_WIFI_ERROR)
            addAction(BleService.ACTION_WIFI_CONNECTED)
            addAction(BleService.ACTION_ERROR)
        }
    }

    val makeWifiIntentFilter: IntentFilter = IntentFilter().apply {
        this.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    }
    private var listener: WifiModelListener? = null

    init {
        context.registerReceiver(wifiScanReceiver, makeWifiIntentFilter)
        context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        context.registerReceiver(wifiStateChangeReceiver, wifiStateIntentFilter)
    }

    fun startWifiScan() {
        val success = wifiManager.startScan()
        listener?.onScanStatusUpdated(ScanningStatus.InProgress)
        if (!success) {
            scanFailure()
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanSuccess() {
        val results = wifiManager.scanResults
        listener?.onScanStatusUpdated(ScanningStatus.Success)
        for (result in results) {
            if (result.SSID != "" && (result.frequency in 2201..2499)) {
                Log.d(TAG, "scanSuccess: ${result.SSID}")
                Log.i(TAG, "scanSuccess: ${result.frequency}")
                WifiCache.addNetworkToNetworkList(result)
                listener?.onWifiNetworkFound(result)
            }
        }
    }

    fun registerListener(listener: WifiModelListener) {
        this.listener = listener
    }

    private fun scanFailure() {
        Log.e(TAG, "scanFailure: Model call")
        listener?.onScanStatusUpdated(ScanningStatus.Failed)
    }

    fun stopWifiScan() {
        context.unregisterReceiver(wifiScanReceiver)
    }

    fun isWifiEnabled():Boolean{
       return wifiManager.isWifiEnabled
    }

    fun setWifiFlow(flow: String){
        when(flow.lowercase()){
            "bluetooth" -> {
                Log.i(TAG, "setWifiFlow: WIFI")
                WifiCache.setWifiFlow(WifiCache.WifiFlow.BluetoothFlow)
            }
            "qr" -> {
                Log.i(TAG, "setWifiFlow: QR")
                WifiCache.setWifiFlow(WifiCache.WifiFlow.QrFlow)
            }
            else -> {
                Log.e(TAG, "setWifiFlow: Unknown") }
        }
    }


    private companion object {
        /*
        Pulled error codes from MAG. TODO: Create a library for the constants that we use, would be super useful.
         */
        const val ERROR_PLATFORM = "ERROR_1"
        const val ERROR_WIFI_NETWORK_NOT_FOUND = "ERROR_WIFI_NETWORK_NOT_FOUND"
        const val ERROR_WIFI_DISABLED = "ERROR_WIFI_DISABLED"
        const val ERROR_BLUETOOTH_DISABLED = "ERROR_BLUETOOTH_DISABLED"
        const val ERROR_WIFI_WPA_LESS_THAN_8 = "ERROR_WIFI_WPA_LESS_THAN_8"
        const val ERROR_WIFI_INVALID_PASSWORD = "ERROR_WIFI_INVALID_PASSWORD"
        const val ERROR_WIFI_CONNECTION_TIMEOUT = "ERROR_WIFI_CONNECTION_TIMEOUT"
        const val ERROR_TEST = "ERROR_TEST"
        const val ERROR_NONE = "ERROR_NONE"
    }


}