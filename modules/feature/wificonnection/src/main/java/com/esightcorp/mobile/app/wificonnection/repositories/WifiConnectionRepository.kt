package com.esightcorp.mobile.app.wificonnection.repositories

import android.content.Context
import android.net.wifi.ScanResult
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import com.esightcorp.mobile.app.networking.WifiModel
import com.esightcorp.mobile.app.networking.WifiModelListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
private const val TAG = "WifiConnectionRepository"
class WifiConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    var isBluetoothConnected = eSightBleManager.checkIfConnected()
    private lateinit var wifiRepoListener: WifiConnectionRepoListener
    private val wifiModel = WifiModel(context)
    private val networkList: MutableList<ScanResult> = mutableListOf()
    private val wifiModelListener = object: WifiModelListener{
        override fun onWifiNetworkFound(result: ScanResult) {
            Log.e(TAG, "onWifiNetworkFound: ${result.SSID}")
            networkList.add(result)
            wifiRepoListener.onNetworkListUpdated(networkList)
        }

        override fun onNetworkConnected() {
            TODO("Not yet implemented")
        }

        override fun onScanFailed() {
            TODO("Not yet implemented")
        }

    }

    init {
        Log.e(TAG, "Init - is bluetooth currently connected? $isBluetoothConnected" )
    }


    fun sendWifiCreds(ssid: String, pwd: String, type: String){
        if(isBluetoothConnected){
            try{
                eSightBleManager.getBleService()?.sendWifiCreds(ssid, pwd, type)
            }catch (exception:NullPointerException){
                Log.e(TAG, "sendWifiCreds: BleService has not been initialized ",exception )
            }
        }
        else{
            Log.d(TAG, "sendWifiCreds: No bt connection")
            wifiRepoListener.onBluetoothNotConnected()
        }

    }

    fun startWifiScan(){
        wifiModel.startWifiScan()
    }

    private fun setupWifiModelListener(){
        wifiModel.registerListener(wifiModelListener)
    }

    fun registerListener(listener: WifiConnectionRepoListener){
        wifiRepoListener = listener
        setupWifiModelListener()
    }



}