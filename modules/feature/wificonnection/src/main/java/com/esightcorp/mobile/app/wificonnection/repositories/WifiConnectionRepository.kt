package com.esightcorp.mobile.app.wificonnection.repositories

import android.content.Context
import android.net.wifi.ScanResult
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
    private lateinit var wifiRepoListener: WifiConnectionRepoListener
    private val wifiModel = WifiModel(context)
    private val networkList: MutableList<ScanResult> = mutableListOf()
    private lateinit var selectedNetwork: ScanResult
    private val wifiModelListener = object: WifiModelListener{
        override fun onWifiNetworkFound(result: ScanResult) {
            Log.e(TAG, "onWifiNetworkFound: ${result.SSID}")
            networkList.add(result)
            Log.i(TAG, "onWifiNetworkFound: $wifiRepoListener")
            wifiRepoListener.onNetworkListUpdated(networkList)
        }

        override fun onNetworkConnected() {
            Log.i(TAG, "onNetworkConnected: $wifiRepoListener")
            wifiRepoListener.onWifiConnected(true)
        }

        override fun onNetworkConnectionError() {
            Log.i(TAG, "onNetwoorkConnectionError: $wifiRepoListener")
            wifiRepoListener.onWifiConnected(false)
        }

        override fun onScanFailed() {
            Log.i(TAG, "onScanFailed: $wifiRepoListener")
            Log.e(TAG, "onScanFailed: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(TAG, "onScanStatusUpdated: $wifiRepoListener")
            wifiRepoListener.onScanStatusUpdated(status)
        }
    }

    init {

    }


    fun sendWifiCreds(pwd: String, type: String){
        if(eSightBleManager.checkIfConnected()){
            try{
                eSightBleManager.getBleService()?.sendWifiCreds(WifiCache.credentials.getNetwork().SSID.toString(), pwd, type)
            }catch (exception:NullPointerException){
                Log.e(TAG, "sendWifiCreds: BleService has not been initialized ",exception )
            }catch (exception:UninitializedPropertyAccessException){
                Log.e(TAG, "sendWifiCreds: BleService has not been initialized ",exception )
            }
        }
        else{
            Log.d(TAG, "sendWifiCreds: No bt connection")
            wifiRepoListener.onBluetoothStatusUpdate(eSightBleManager.checkIfConnected())
        }

    }

    fun startWifiScan(){
        wifiModel.startWifiScan()
    }

    private fun setupWifiModelListener(){
        wifiModel.registerListener(wifiModelListener)
    }

    fun getCachedWifiList(){
        wifiRepoListener.onNetworkListUpdated(WifiCache.getNetworkList())
    }

    fun setSelectedNetwork(network: ScanResult){
        WifiCache.selectNetwork(network)
        wifiModel.stopWifiScan()
    }

    fun getSelectedNetwork():ScanResult{
        return WifiCache.credentials.getNetwork()
    }

    fun registerListener(listener: WifiConnectionRepoListener){
        Log.d(TAG, "registerListener: $listener")
        this.wifiRepoListener = listener
        if(!eSightBleManager.checkIfConnected()){
            Log.e(TAG, "Bluetooth is not currently connected." )
            wifiRepoListener.onBluetoothStatusUpdate(eSightBleManager.checkIfConnected())
        }
        setupWifiModelListener()
    }



}