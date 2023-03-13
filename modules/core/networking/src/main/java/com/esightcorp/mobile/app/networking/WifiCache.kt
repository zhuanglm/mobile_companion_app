package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import android.util.Log

object WifiCache {

    private var networkList: MutableList<ScanResult> = mutableListOf()
    val credentials = WifiCredentials

    fun getNetworkList(): MutableList<ScanResult>{
        return networkList
    }

    fun setNetworkList(list: MutableList<ScanResult>){
        this.networkList = list
    }

    fun selectNetwork(network: ScanResult){
        Log.i("WifiCache", "selectNetwork: ${network.SSID}")
        this.credentials.setNetwork(network)
    }

    fun enterNetworkPassword(pwd: String){
        this.credentials.setPassword(pwd)
    }

    fun addNetworkToNetworkList(result: ScanResult):Boolean{
        Log.d("WifiCache", "addNetworkToNetworkList: " + result.SSID)
        Log.d("WifiCache", result.toString())
        return if(networkList.none { it.BSSID == result.BSSID }){
            this.networkList.add(result)
            true
        }else{
            false
        }
    }

}