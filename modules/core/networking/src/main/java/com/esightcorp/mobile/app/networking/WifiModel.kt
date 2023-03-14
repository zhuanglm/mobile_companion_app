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

    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }
    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BleService.ACTION_GATT_CONNECTED -> {
//                    bleManager.setConnectedDevice(bleManager.getConnectedDevice()!!, true)
                    Log.e(TAG, "onReceive: CONNECTED")
//                    bleManager.getModelListener()?.onDeviceConnected(bleManager.getConnectedDevice()!!, true)
//                    bleManager.discoverServices()

                }
                BleService.ACTION_GATT_DISCONNECTED -> {
                    Log.e(TAG, "onReceive: DISCONNECTED")
//                    bleManager.getConnectedDevice()?.let { bleManager.getModelListener()?.onDeviceConnected(it, false) }
//                    bleManager.resetConnectedDevice()
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
                        }
                        ERROR_WIFI_NETWORK_NOT_FOUND -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_NETWORK_NOT_FOUND")
                        }
                        ERROR_WIFI_DISABLED -> {
                            Log.e(TAG, "onReceive: ERROR_WIFI_DISABLED")
                            listener?.onWifiDisabled()
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
        Log.e(TAG, "scanFailure: ")
        listener?.onScanStatusUpdated(ScanningStatus.Failed)
    }

    fun stopWifiScan() {
        context.unregisterReceiver(wifiScanReceiver)
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