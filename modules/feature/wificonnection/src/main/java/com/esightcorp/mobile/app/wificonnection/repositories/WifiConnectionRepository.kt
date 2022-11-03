package com.esightcorp.mobile.app.wificonnection.repositories

import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import com.esightcorp.mobile.app.networking.WifiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
private const val TAG = "WifiConnectionRepository"
class WifiConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    var isBluetoothConnected = eSightBleManager.checkIfConnected()
    private lateinit var wifiRepoListener: WifiConnectionRepoListener
    private val wifiModel = WifiModel(context)

    init {
        Log.e(TAG, "Init - is bluetooth currently connected? $isBluetoothConnected" )
//        wifiModel.startWifiScan()
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
            wifiRepoListener.onBluetoothNotConnected()
        }

    }

    fun registerListener(listener: WifiConnectionRepoListener){
        wifiRepoListener = listener
    }



}