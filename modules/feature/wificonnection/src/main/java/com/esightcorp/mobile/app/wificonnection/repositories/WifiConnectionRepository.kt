package com.esightcorp.mobile.app.wificonnection.repositories

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import com.esightcorp.mobile.app.networking.WifiCache
import com.esightcorp.mobile.app.networking.WifiModel
import com.esightcorp.mobile.app.networking.WifiModelListener
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "WifiConnectionRepository"

class WifiConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val wifiModel = WifiModel(context)
    private val networkList: MutableList<ScanResult> = mutableListOf()
    private lateinit var selectedNetwork: ScanResult
    private lateinit var networkScanListener: WifiNetworkScanListener
    private lateinit var connectionListener: WifiConnectionListener
    private val wifiModelListener = object : WifiModelListener {
        override fun onWifiNetworkFound(result: ScanResult) {
            Log.e(TAG, "onWifiNetworkFound: ${result.SSID}")
            networkList.add(result)
            getSafeNetworkScanListener()?.onNetworkListUpdated(networkList)
        }

        override fun onNetworkConnected() {
            getSafeConnectionListener()?.onWifiConnected(true)
        }

        override fun onNetworkConnectionError() {
            Log.e(TAG, "onNetworkConnectionError: ", )
            getSafeConnectionListener()?.onNetworkConnectionError()
        }

        override fun onScanFailed() {
            Log.e(TAG, "onScanFailed: ")
            getSafeNetworkScanListener()?.onScanStatusUpdated(ScanningStatus.Failed)
        }

        override fun onWifiDisabled() {
            Log.i(TAG, "onWifiDisabled: ")
            getSafeConnectionListener()?.onWifiStatusUpdate(false)
            getSafeNetworkScanListener()?.onWifiStatusUpdate(false)
        }

        override fun onBluetoothDisconnected() {
            Log.i(TAG, "onBluetoothDisconnected: ")
            getSafeNetworkScanListener()?.onBluetoothStatusUpdate(false)
            getSafeConnectionListener()?.onBluetoothStatusUpdate(false)
        }

        override fun AlreadyConnectedToWifi() {
            Log.i(TAG, "AlreadyConnectedToWifi: still need to figure this one out")
        }

        override fun onErrorTest() {
            Log.i(TAG, "onErrorTest: ")
            getSafeConnectionListener()?.onWifiConnectionTest()
        }

        override fun onErrorWifiConnectionTimeout() {
            Log.i(TAG, "onErrorWifiConnectionTimeout: ")
            getSafeConnectionListener()?.onWifiConnectionTimeout()
        }

        override fun onErrorWifiInvalidPassword() {
            Log.i(TAG, "onErrorWifiInvalidPassword: ")
            getSafeConnectionListener()?.onWifiInvalidPassword()
        }

        override fun onErrorWifiWPALessThan8() {
            Log.i(TAG, "onErrorWifiWPALessThan8: ")
            getSafeConnectionListener()?.onWifiWPALessThan8()
        }

        override fun onWifiEnabled() {
            getSafeConnectionListener()?.onWifiStatusUpdate(true)
            getSafeNetworkScanListener()?.onWifiStatusUpdate(true)
        }

        override fun onGoWifiDisabled() {
            getSafeConnectionListener()?.onGoWifiDisabled()
        }

        override fun onPlatformError() {
            getSafeConnectionListener()?.onPlatformError()
        }

        override fun onNetworkNotFound() {
            getSafeConnectionListener()?.onWifiNetworkNotFound()
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            getSafeNetworkScanListener()?.onScanStatusUpdated(status)
        }
    }

    init {
        setupWifiModelListener()
    }

    private fun getSafeNetworkScanListener(): WifiNetworkScanListener?{
        return (if (this::networkScanListener.isInitialized) {networkScanListener} else {null})
    }

    private fun getSafeConnectionListener(): WifiConnectionListener?{
        return (if (this::connectionListener.isInitialized) {connectionListener} else {null})
    }


    fun sendWifiCreds(pwd: String, type: String) {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()
                    ?.sendWifiCreds(WifiCache.credentials.getNetwork().SSID.toString(), pwd, type)
            } catch (exception: NullPointerException) {
                Log.e(TAG, "sendWifiCreds: BleService has not been initialized ", exception)
            } catch (exception: UninitializedPropertyAccessException) {
                Log.e(TAG, "sendWifiCreds: BleService has not been initialized ", exception)
            }
        } else {
            Log.d(TAG, "sendWifiCreds: No bt connection")
            getSafeConnectionListener()?.onBluetoothStatusUpdate(eSightBleManager.checkIfConnected())
            getSafeNetworkScanListener()?.onBluetoothStatusUpdate(eSightBleManager.checkIfConnected())
        }

    }

    fun startWifiScan() {
        wifiModel.startWifiScan()
    }

    fun getCurrentWifiFlow(): WifiCache.WifiFlow{
        return WifiCache.getWifiFlow()
    }

    private fun setupWifiModelListener() {
        wifiModel.registerListener(wifiModelListener)
    }

    fun getCachedWifiList() {
        getSafeNetworkScanListener()?.onNetworkListUpdated(WifiCache.getNetworkList())
    }

    fun setSelectedNetwork(network: ScanResult) {
        WifiCache.selectNetwork(network)
        wifiModel.stopWifiScan()
    }

    fun getSelectedNetwork(): ScanResult {
        return WifiCache.credentials.getNetwork()
    }
    fun getCurrentPassword(): String{
        return WifiCache.credentials.getPassword()
    }
    fun getCurrentSecurityType(): String{
        return WifiCache.credentials.getWifiType()
    }

    fun setWifiType(type: String){
        WifiCache.credentials.setWifiType(type)
    }

    fun registerListener(listener: WifiNetworkScanListener) {
        Log.d(TAG, "registerListener: $listener")
        this.networkScanListener = listener
        this.networkScanListener.onWifiStatusUpdate(isWifiEnabled())
        this.networkScanListener.onBluetoothStatusUpdate(eSightBleManager.checkIfConnected())
    }
    private fun isWifiEnabled(): Boolean{
        return wifiModel.isWifiEnabled()
    }

    fun registerListener(listener: WifiConnectionListener){
        Log.d(TAG, "registerListener: ")
        this.connectionListener = listener
        this.connectionListener.onWifiStatusUpdate(isWifiEnabled())
        this.connectionListener.onBluetoothStatusUpdate(eSightBleManager.checkIfConnected())
    }

    fun unregisterListener(listener: WifiConnectionListener) {
//        TODO("Not yet implemented")
        Log.e(TAG, "unregisterListener: ", )
    }

    fun setWifiFlow(flow: String){
        wifiModel.setWifiFlow(flow)
    }

    fun setWifiPassword(pwd: String){
        wifiModel.setWifiPassword(pwd)
    }

    fun getQrString():String{
        return wifiModel.getQrString()
    }

}