/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.repositories

import android.content.Context
import android.net.wifi.ScanResult
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import com.esightcorp.mobile.app.networking.WifiModel
import com.esightcorp.mobile.app.networking.WifiModelListener
import com.esightcorp.mobile.app.networking.ssidName
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WifiConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val _tag = this.javaClass.simpleName

    private val wifiModel = WifiModel(context)
    private val networkList: MutableList<ScanResult> = mutableListOf()
    private var networkScanListener: WifiNetworkScanListener? = null
    private var connectionListener: WifiConnectionListener? = null

    //region WifiModelListener
    private val wifiModelListener = object : WifiModelListener {
        override fun onWifiNetworkFound(result: ScanResult) {
            Log.e(_tag, "onWifiNetworkFound: ${result.ssidName()}")
            networkList.add(result)
            networkScanListener?.onNetworkListUpdated(networkList)
        }

        override fun onNetworkConnected() {
            connectionListener?.onWifiConnected(true)
        }

        override fun onNetworkConnectionError() {
            Log.e(_tag, "onNetworkConnectionError: ")
            connectionListener?.onNetworkConnectionError()
        }

        override fun onScanFailed() {
            Log.e(_tag, "onScanFailed: ")
            networkScanListener?.onScanStatusUpdated(ScanningStatus.Failed)
        }

        override fun onWifiDisabled() {
            Log.i(_tag, "onWifiDisabled: ")
            connectionListener?.onWifiStatusUpdate(false)
            networkScanListener?.onWifiStatusUpdate(false)
        }

        override fun onBluetoothDisconnected() {
            Log.i(_tag, "onBluetoothDisconnected: ")
            networkScanListener?.onBleConnectionStatusUpdate(false)
            connectionListener?.onBleConnectionStatusUpdate(false)
        }

        override fun alreadyConnectedToWifi(status: Boolean) {
            Log.i(_tag, "AlreadyConnectedToWifi: $status")
            networkScanListener?.onWifiAlreadyConnected(status)
        }

        override fun onErrorTest() {
            Log.i(_tag, "onErrorTest: ")
            connectionListener?.onWifiConnectionTest()
        }

        override fun onErrorWifiConnectionTimeout() {
            Log.i(_tag, "onErrorWifiConnectionTimeout: ")
            connectionListener?.onWifiConnectionTimeout()
        }

        override fun onErrorWifiInvalidPassword() {
            Log.i(_tag, "onErrorWifiInvalidPassword: ")
            connectionListener?.onWifiInvalidPassword()
        }

        override fun onErrorWifiWPALessThan8() {
            Log.i(_tag, "onErrorWifiWPALessThan8: ")
            connectionListener?.onWifiWPALessThan8()
        }

        override fun onWifiEnabled() {
            connectionListener?.onWifiStatusUpdate(true)
            networkScanListener?.onWifiStatusUpdate(true)
        }

        override fun onGoWifiDisabled() {
            connectionListener?.onGoWifiDisabled()
        }

        override fun onPlatformError() {
            connectionListener?.onPlatformError()
        }

        override fun onNetworkNotFound() {
            connectionListener?.onWifiNetworkNotFound()
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            networkScanListener?.onScanStatusUpdated(status)
        }
    }
    //endregion

    init {
        wifiModel.registerListener(wifiModelListener)
    }

    //region Public interface

    val wifiFlow = WifiCache.getWifiFlow()
    val qrString = wifiModel.getQrString()
    val wifiCredentials = WifiCache.credentials

    fun sendWifiCreds(pwd: String, type: String) {
        when (eSightBleManager.checkIfConnected()) {
            false -> {
                Log.d(_tag, "sendWifiCreds: No bt connection")
                connectionListener?.onBleConnectionStatusUpdate(false)
                networkScanListener?.onBleConnectionStatusUpdate(false)
            }

            true -> {
                try {
                    eSightBleManager.getBleService()
                        ?.sendWifiCreds(WifiCache.credentials.getSSID()!!, pwd, type)
                } catch (exception: NullPointerException) {
                    Log.e(_tag, "sendWifiCreds: BleService has not been initialized ", exception)
                } catch (exception: UninitializedPropertyAccessException) {
                    Log.e(_tag, "sendWifiCreds: BleService has not been initialized ", exception)
                }
            }
        }
    }

    fun startWifiScan() = wifiModel.startWifiScan()
    fun cancelWifiScan() = wifiModel.stopWifiScan()

    fun readWifiConnectionStatus() {
        eSightBleManager.getConnectedBleService()?.readWifiConnectionStatus()
    }

    fun getCachedWifiList() {
        networkScanListener?.onNetworkListUpdated(WifiCache.getNetworkList())
    }

    fun setSelectedNetwork(network: ScanResult) {
        WifiCache.selectNetwork(network)
        wifiModel.stopWifiScan()
    }

    fun setWifiType(type: String) {
        WifiCache.credentials.setWifiType(type)
    }

    @Synchronized
    fun registerListener(listener: WifiNetworkScanListener) {
        Log.d(_tag, "registerListener: $listener")
        with(listener) {
            onWifiStatusUpdate(isWifiEnabled())
            onBleConnectionStatusUpdate(eSightBleManager.checkIfConnected())

            networkScanListener = this
        }
    }

    @Synchronized
    fun registerListener(listener: WifiConnectionListener) {
        Log.d(_tag, "registerListener: $listener")
        with(listener) {
            onWifiStatusUpdate(isWifiEnabled())
            onBleConnectionStatusUpdate(eSightBleManager.checkIfConnected())

            connectionListener = this
        }
    }

    fun unregisterListener(listener: WifiConnectionListener) {
//        TODO("Not yet implemented")
        Log.e(_tag, "unregisterListener: ")
    }

    @Synchronized
    fun setWifiFlow(flow: String?) {
        wifiModel.setWifiFlow(flow)
    }

    fun setWifiPassword(pwd: String) {
        wifiModel.setWifiPassword(pwd)
    }

    //endregion

    //region Private implementation
    private fun isWifiEnabled() = wifiModel.isWifiEnabled()
    //endregion
}
