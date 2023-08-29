package com.esightcorp.mobile.app.networking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BleService
import com.esightcorp.mobile.app.networking.sockets.CreateSocketListener
import com.esightcorp.mobile.app.networking.sockets.InputStreamListener
import com.esightcorp.mobile.app.networking.sockets.SocketManager
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.networking.storage.eShareCache
import com.esightcorp.mobile.app.utils.ScanningStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.NetworkInterface
import java.net.ServerSocket
import java.util.Collections
import kotlin.random.Random

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
        Log.i(TAG, "startWifiScan: ")
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
        try {
            context.unregisterReceiver(wifiScanReceiver)
        }catch (e: Exception){
            Log.e(TAG, "stopWifiScan: ${e.message}")
        }
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

    fun setWifiPassword(password: String){
        WifiCache.credentials.setPassword(password)
    }

    fun getQrString():String{
        return "WIFI:S:${WifiCache.credentials.getNetwork().SSID};T:${WifiCache.credentials.getWifiType()};P:${WifiCache.credentials.getPassword()};;"
    }

    fun connectToEshare(createSocketListener: CreateSocketListener, inputStreamListener: InputStreamListener){
        getMyIpAddress()?.let { eShareCache.setIpAddress(it) }
        getPortToConnectOn { port ->
            if (port != null) {
                // Use the port
                eShareCache.setPort(port)
                SocketManager.connect(port = port, createSocketListener =  createSocketListener, inputStreamListener = inputStreamListener)
            } else {
                // Handle the case where no port was found
                Log.e(TAG, "connectToEshare: No port found")

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

    suspend fun findRandomAvailablePort(): Int? = withContext(Dispatchers.IO) {
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
                    Log.i(TAG, "getPortToConnectOn: $port")
                    callback(port)
                } else {
                    Log.e(TAG, "getPortToConnectOn: Port is null")
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