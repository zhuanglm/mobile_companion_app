/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BleAction
import com.esightcorp.mobile.app.bluetooth.BleService
import com.esightcorp.mobile.app.bluetooth.WifiConnectionStatus
import com.esightcorp.mobile.app.bluetooth.addAction
import com.esightcorp.mobile.app.networking.sockets.CreateSocketListener
import com.esightcorp.mobile.app.networking.sockets.InputStreamListener
import com.esightcorp.mobile.app.networking.sockets.SocketManager
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.networking.storage.eShareCache
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.utils.safeRegisterReceiver
import com.esightcorp.mobile.app.utils.safeUnregisterReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.NetworkInterface
import java.net.ServerSocket
import java.util.Collections
import kotlin.random.Random

@SuppressLint("UnspecifiedRegisterReceiverFlag")
class WifiModel(
    val context: Context
) {
    private val _tag = this.javaClass.simpleName

    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            processScanResult(intent)
        }
    }
    private var isScanningStopped: Boolean = true
        @Synchronized get
        @Synchronized set

    private val wifiStateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != WifiManager.WIFI_STATE_CHANGED_ACTION) return

            when (intent.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )) {
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
    private val wifiStateIntentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)

    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BleService.ACTION_DATA_AVAILABLE -> {
                    when (intent.data.toString()) {
                        else -> {
                            Log.i(_tag, "onReceive: ${intent.data.toString()}")
                        }
                    }

                    val exData = intent.extras?.getString(BleService.EXTRA_DATA)
                    when (WifiConnectionStatus.from(exData)) {
                        WifiConnectionStatus.WIFI_STATUS_ERROR,
                        WifiConnectionStatus.WIFI_STATUS_NONE -> listener?.alreadyConnectedToWifi(
                            false
                        )

                        else -> listener?.alreadyConnectedToWifi(true)
                    }
                }

                BleAction.GATT_DISCONNECTED.actionName() -> {
                    Log.e(_tag, "onReceive: DISCONNECTED")
                    listener?.onBluetoothDisconnected()
                }

                BleService.ACTION_WIFI_CONNECTED -> {
                    Log.e(_tag, "onReceive: WIFI CONNECTED ")
                    listener?.onNetworkConnected()
                }

                BleService.ACTION_WIFI_ERROR -> {
                    Log.e(_tag, "onReceive: WIFI_ERROR")
                    listener?.onNetworkConnectionError()
                }

                BleService.ACTION_ERROR -> {
                    Log.e(_tag, "onReceive: ERROR  " + intent.data.toString())
                    when (intent.data.toString()) {
                        ERROR_PLATFORM -> {
                            Log.e(_tag, "onReceive: ERROR_PLATFORM")
                            listener?.onPlatformError()
                        }

                        ERROR_WIFI_NETWORK_NOT_FOUND -> {
                            Log.e(_tag, "onReceive: ERROR_WIFI_NETWORK_NOT_FOUND")
                            listener?.onNetworkNotFound()
                        }

                        ERROR_WIFI_DISABLED -> {
                            Log.e(_tag, "onReceive: ERROR_WIFI_DISABLED")
                            listener?.onGoWifiDisabled()
                        }

                        ERROR_BLUETOOTH_DISABLED -> {
                            Log.e(_tag, "onReceive: ERROR_BLUETOOTH_DISABLED")
                            listener?.onBluetoothDisconnected()
                        }

                        ERROR_WIFI_WPA_LESS_THAN_8 -> {
                            Log.e(_tag, "onReceive: ERROR_WIFI_WPA_LESS_THAN_8")
                            listener?.onErrorWifiWPALessThan8()
                        }

                        ERROR_WIFI_INVALID_PASSWORD -> {
                            Log.e(_tag, "onReceive: ERROR_WIFI_INVALID_PASSWORD")
                            listener?.onErrorWifiInvalidPassword()
                        }

                        ERROR_WIFI_CONNECTION_TIMEOUT -> {
                            Log.e(_tag, "onReceive: ERROR_WIFI_CONNECTION_TIMEOUT")
                            listener?.onErrorWifiConnectionTimeout()
                        }

                        ERROR_TEST -> {
                            Log.e(_tag, "onReceive: ERROR_TEST")
                            listener?.onErrorTest()
                        }

                        ERROR_NONE -> {
                            Log.e(
                                _tag,
                                "onReceive: ${intent.data.toString()}  <<<<------>>>> Error Characteristic has been reset. "
                            )
                        }
                    }

                }
            }
        }
    }
    private val makeWifiBleIntentFilter = IntentFilter().apply {
        addAction(BleService.ACTION_DATA_AVAILABLE)
        addAction(BleService.ACTION_WIFI_ERROR)
        addAction(BleService.ACTION_WIFI_CONNECTED)
        addAction(BleService.ACTION_ERROR)

        addAction(BleAction.GATT_DISCONNECTED)
    }

    private val makeWifiIntentFilter: IntentFilter = IntentFilter().apply {
        this.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    }
    private var listener: WifiModelListener? = null

    init {
        context.safeRegisterReceiver(gattUpdateReceiver, makeWifiBleIntentFilter)
        context.safeRegisterReceiver(wifiStateChangeReceiver, wifiStateIntentFilter)
    }

    @Synchronized
    fun startWifiScan() {
        Log.i(_tag, "startWifiScan - $this")

        context.safeRegisterReceiver(wifiScanReceiver, makeWifiIntentFilter)

        val isScanStarted = wifiManager.startScan()
        listener?.onScanStatusUpdated(ScanningStatus.InProgress)

        isScanningStopped = !isScanStarted

        if (isScanningStopped)
            scanFailure()
    }

    @SuppressLint("MissingPermission")
    private fun scanSuccess() {
        val results = wifiManager.scanResults
        listener?.onScanStatusUpdated(ScanningStatus.Success)
        for (result in results) {
            if (result.ssidName() != "" && (result.frequency in 2201..2499)) {
                WifiCache.addNetworkToNetworkList(result)
                listener?.onWifiNetworkFound(result)
            }
        }
    }

    fun registerListener(listener: WifiModelListener) {
        this.listener = listener
    }

    private fun scanFailure() {
        Log.e(_tag, "scanFailure: Model call")
        listener?.onScanStatusUpdated(ScanningStatus.Failed)
    }

    @Synchronized
    fun stopWifiScan() {
        isScanningStopped = true

        context.safeUnregisterReceiver(wifiScanReceiver)
        Log.i(_tag, "stopWifiScan - receiver unregistered")
    }

    @Synchronized
    private fun processScanResult(intent: Intent) {
        if (isScanningStopped) return

        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        Log.w(_tag, "wifiScanReceiver - exResultUpdated success: $success - $this")

        when (success) {
            true -> scanSuccess()
            false -> scanFailure()
        }
    }

    fun isWifiEnabled(): Boolean {
        Log.i(_tag, "isWifiEnabled: ${wifiManager.isWifiEnabled}")
        return wifiManager.isWifiEnabled
    }

    fun setWifiFlow(flow: String?) {
        when (flow?.lowercase()) {
            WifiNavigation.ScanningRoute.PARAM_BLUETOOTH -> {
                Log.i(_tag, "setWifiFlow: Bluetooth")
                WifiCache.setWifiFlow(WifiCache.WifiFlow.BluetoothFlow)
            }

            WifiNavigation.ScanningRoute.PARAM_QR -> {
                Log.i(_tag, "setWifiFlow: QR")
                WifiCache.setWifiFlow(WifiCache.WifiFlow.QrFlow)
            }

            else -> {
                Log.e(_tag, "setWifiFlow: Unknown")
                WifiCache.setWifiFlow(WifiCache.WifiFlow.NotInUse)
            }
        }
    }

    fun setWifiPassword(password: String) {
        WifiCache.credentials.setPassword(password)
    }

    fun getQrString() = when (val nwName = WifiCache.credentials.getSSID()) {
        null -> null
        else -> "WIFI:S:${nwName};T:${WifiCache.credentials.getWifiType().typeString};P:${WifiCache.credentials.getPassword()};;"
    }

    fun openSocket(
        createSocketListener: CreateSocketListener,
        inputStreamListener: InputStreamListener,
    ) {
        getMyIpAddress()?.let { eShareCache.setIpAddress(it) }
        getPortToConnectOn { port ->
            if (port != null) {
                // Use the port
                eShareCache.setPort(port)
                SocketManager.connect(
                    port = port,
                    createSocketListener = createSocketListener,
                    inputStreamListener = inputStreamListener
                )
            } else {
                // Handle the case where no port was found
                Log.e(_tag, "connectToEshare: No port found")

            }
        }
    }

    private fun getRandomPortFromRange(start: Int = 49152, end: Int = 65535): Int {
        return Random.nextInt(start, end + 1)
    }

    private suspend fun isPortAvailable(port: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            ServerSocket(port).use {
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun findRandomAvailablePort(): Int? = withContext(Dispatchers.IO) {
        repeat(100) { // try up to 100 times
            val randomPort = getRandomPortFromRange()
            if (isPortAvailable(randomPort)) {
                return@withContext randomPort
            }
        }
        return@withContext null
    }


    private fun getPortToConnectOn(callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val port = findRandomAvailablePort()
            withContext(Dispatchers.Main) { // Switch back to the main thread to handle the result
                if (port != null) {
                    Log.i(_tag, "getPortToConnectOn: $port")
                    callback(port)
                } else {
                    Log.e(_tag, "getPortToConnectOn: Port is null")
                    callback(null)
                }
            }
        }
    }

    private fun getMyIpAddress(useIPv4: Boolean = true): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        // Check for IPv4 or IPv6 preference
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                // Remove prefix for IPv6 address
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr else sAddr.substring(0, delim)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
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

@Suppress("Deprecation")
fun ScanResult.ssidName(): String? {
    if (Build.VERSION.SDK_INT >= 33)
        return wifiSsid?.toString()

    return SSID
}
