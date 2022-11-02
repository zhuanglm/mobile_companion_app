package com.esightcorp.mobile.app.wificonnection.repositories

import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
private const val TAG = "WifiConnectionRepository"
class WifiConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    var isBluetoothConnected = eSightBleManager.checkIfConnected()

    init {
        Log.e(TAG, "Init - is bluetooth currently connected? $isBluetoothConnected" )
    }

    fun sendWifiCreds(ssid: String, pwd: String, type: String){
        try{
            eSightBleManager.getBleService()?.sendWifiCreds(ssid, pwd, type)
        }catch (exception:NullPointerException){
            Log.e(TAG, "sendWifiCreds: BleService has not been initialized ",exception )
        }

    }



}