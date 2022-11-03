package com.esightcorp.mobile.app.networking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log

private const val TAG = "WifiModel"

class WifiModel(
    val context: Context
) {

    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiScanReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if(success){
                scanSuccess()
            }else{
                scanFailure()
            }
        }
    }
    val makeWifiIntentFilter: IntentFilter = IntentFilter().apply {
        this.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    }

    init {
        context.registerReceiver(wifiScanReceiver, makeWifiIntentFilter)
    }

    fun startWifiScan(){
        val success = wifiManager.startScan()
        if(!success){
            scanFailure()
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanSuccess(){
        val results = wifiManager.scanResults
        for (result in results) {
            Log.d(TAG, "scanSuccess: ${result.BSSID}")
        }
    }

    private fun scanFailure(){
        Log.e(TAG, "scanFailure: " )

    }







}