/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.repositories

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.Surface
import com.esightcorp.mobile.app.bluetooth.BluetoothConnectionListener
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothRadioListener
import com.esightcorp.mobile.app.bluetooth.EshareBluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.HotspotModelListener
import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.bluetooth.WifiConnectionStatus
import com.esightcorp.mobile.app.networking.SystemStatusListener
import com.esightcorp.mobile.app.networking.WifiModel
import com.esightcorp.mobile.app.networking.sockets.CreateSocketListener
import com.esightcorp.mobile.app.networking.sockets.InputStreamListener
import com.esightcorp.mobile.app.networking.sockets.SocketManager
import com.esightcorp.mobile.app.networking.storage.eShareCache
import com.esightcorp.mobile.app.networking.streaming.StreamOutListener
import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class EshareRepository @Inject constructor(
    @ApplicationContext context: Context
) : BluetoothRadioListener, BluetoothConnectionListener, SystemStatusListener,
    EshareBluetoothModelListener, HotspotModelListener {

    private val _tag = this.javaClass.simpleName

    private val bluetoothModel: BluetoothModel
    private val wifiModel: WifiModel
    private lateinit var eShareRepositoryListener: EshareRepositoryListener
    private var deviceDisconnectListener: (() -> Unit)? = null

    private var lastHotspotStatus: HotspotStatus? = null

    init {
        bluetoothModel = BluetoothModel(context)
        with(bluetoothModel.bleManager) {
            setEshareBluetoothListener(this@EshareRepository)
            setBluetoothConnectionListener(this@EshareRepository)
            hotspotListener = this@EshareRepository
        }
        wifiModel = WifiModel(context)
    }

    //region Public interface

    data class HotspotCredential(val ssid: String, val password: String)

    fun setupEshareListener(eshareRepositoryListener: EshareRepositoryListener) {
        this.eShareRepositoryListener = eshareRepositoryListener
        eShareRepositoryListener.onWifiStateChanged(wifiModel.isWifiEnabled())
    }

    fun setupBtDisconnectListener(listener: () -> Unit) {
        deviceDisconnectListener = listener
    }

    @Synchronized
    fun startEshareConnection() {
        Log.w(_tag, "startEshareConnection: Lets get this thing going!!!")
        when (wifiModel.isWifiEnabled()) {
            false -> eShareRepositoryListener.onWifiStateChanged(false)

            true -> with(bluetoothModel) {
                registerEshareReceiver()
                bleService?.readWifiConnectionStatus()
            }
        }
    }

    @Synchronized
    fun cancelEshareConnection() {
        Log.i(_tag, "cancelEshareConnection: Never mind")
        with(bluetoothModel) {
            unregisterEshareReceiver()
            bleService?.stopEshare()
        }
    }

    fun startStreamFromHMD(surface: Surface, inputStream: InputStream) {
        Log.i(_tag, "startStreamFromHMD: ")
        SocketManager.startStreamingFromHMD(surface, inputStream, streamOutListener)
    }

    fun startHotspotOnHMD(credential: HotspotCredential) {
        Log.i(_tag, "startHotspotOnHMD")
        with(bluetoothModel) {
            registerHotspotReceiver()

            try {
                bleService?.startHotspot(credential.ssid, credential.password)
            } catch (exception: NullPointerException) {
                Log.e(_tag, "startHotspotOnHMD: BleService has not been initialized ", exception)
            } catch (exception: UninitializedPropertyAccessException) {
                Log.e(_tag, "startHotspotOnHMD: BleService has not been initialized ", exception)
            }
        }
    }

    fun genHotspotCredential() =
        when (val shortName = bluetoothModel.bleManager.getShortDeviceName()) {
            null -> null
            else -> HotspotCredential(
                shortName,
                HotspotCredentialGenerator.generateHotspotPassword()
            )
        }

    //region Remote controller

    fun writeUpButtonPress() {
        try {
            bluetoothModel.bleService?.writeUpButtonPress()
        } catch (exception: Exception) {
            Log.e(_tag, "writeUpButtonPress", exception)
        }
    }

    fun writeDownButtonPress() {
        try {
            bluetoothModel.bleService?.writeDownButtonPress()
        } catch (exception: Exception) {
            Log.e(_tag, "writeDownButtonPress", exception)
        }
    }

    fun writeModeButtonPress() {
        try {
            bluetoothModel.bleService?.writeModeButtonPress()
        } catch (exception: Exception) {
            Log.e(_tag, "writeDownButtonPress", exception)
        }
    }

    fun writeMenuButtonPress() {
        try {
            bluetoothModel.bleService?.writeMenuButtonPress()
        } catch (exception: Exception) {
            Log.e(_tag, "writeMenuButtonPress", exception)
        }
    }

    fun writeVolumeUpButtonPress() {
        try {
            bluetoothModel.bleService?.writeVolumeUpButtonPress()
        } catch (exception: Exception) {
            Log.e(_tag, "writeVolumeUpButtonPress", exception)
        }
    }

    fun writeVolumeDownButtonPress() {
        try {
            bluetoothModel.bleService?.writeVolumeDownButtonPress()
        } catch (exception: Exception) {
            Log.e(_tag, "writeVolumeDownButtonPress", exception)
        }
    }

    fun writeFinderButtonPress() {
        try {
            bluetoothModel.bleService?.writeFinderButtonPressDownEvent()
        } catch (exception: Exception) {
            Log.e(_tag, "writeFinderButtonPress", exception)
        }
    }

    fun writeActionUpEvent() {
        try {
            bluetoothModel.bleService?.writeActionUpEvent()
        } catch (exception: Exception) {
            Log.e(_tag, "writeActionUpEvent", exception)
        }
    }
    //endregion

    //endregion

    //region BluetoothConnectionListener
    override fun onDeviceDisconnected(device: BluetoothDevice?) {
        deviceDisconnectListener?.invoke()

        bluetoothModel.unregisterEshareReceiver()
        bluetoothModel.unregisterHotspotReceiver()
    }
    //endregion

    //region BluetoothRadioListener
    override fun onBluetoothDisabled() {
        updateBluetoothRadioDisabled()
    }
    //endregion

    //region SystemStatusListener
    override fun onScanStatusUpdated(status: ScanningStatus) {
        TODO("Not yet implemented")
    }

    override fun onWifiDisabled() {
        TODO("Not yet implemented")
    }

    override fun onBluetoothDisconnected() {
        TODO("Not yet implemented")
    }

    override fun onWifiEnabled() {
        TODO("Not yet implemented")
    }

    override fun onGoWifiDisabled() {
        TODO("Not yet implemented")
    }

    //endregion

    //region HotspotListener
    @Synchronized
    override fun onHotspotStatusChanged(status: HotspotStatus?) {
        lastHotspotStatus = status
        eShareRepositoryListener.onHotspotStateChanged(status)
    }

    //endregion

    //region EShare callback

    override fun onEshareReady() {
        Log.i(_tag, "onEshareReady: repo")
        writeStreamOutPortIpToHMD()
    }

    override fun onEshareIpNotReachable() {
        updateEshareState(EShareConnectionStatus.IpNotReachable)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onEshareAddrNotAvailable() {
        updateEshareState(EShareConnectionStatus.AddressNotAvailable)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onEshareBusy() {
        updateEshareState(EShareConnectionStatus.Busy)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onEshareStopped() {
        updateEshareState(EShareConnectionStatus.Disconnected)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onUserCancelled() {
        updateEshareState(EShareConnectionStatus.ReceivedUserRejection)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onWifiConnectionStatusChanged(data: String?) {
        val status = WifiConnectionStatus.from(data)

        val proceedCreatingSocket = when (status) {
            // When Wifi error or not configure, need to combine with hotspot status
            WifiConnectionStatus.WIFI_STATUS_ERROR,
            WifiConnectionStatus.WIFI_STATUS_NONE -> when (lastHotspotStatus) {
                HotspotStatus.ENABLED -> true
                else -> false
            }

            // Wifi status is good, proceed creating socket
            else -> true
        }
        Log.w(
            _tag,
            "onWifiConnectionStatusChanged - value: $status --> proceedCreatingSocket: $proceedCreatingSocket"
        )

        when (proceedCreatingSocket) {
            true -> wifiModel.openSocket(createSocketListener, inputStreamListener)
            false -> updateEshareState(EShareConnectionStatus.RequireSetupWifi)
        }
    }

    //endregion

    //region Internal implementation

    private fun checkIfEshareIsRunningAlready() {
        //check if eshare is running already
        try {
            bluetoothModel.bleService?.checkIfEshareIsRunning()
        } catch (e: Exception) {
            Log.e(_tag, "checkIfEshareIsRunningAlready: ", e)
        }
    }

    private fun writeStreamOutPortIpToHMD() {
        Log.i(_tag, "writeStreamOutPortIpToHMD")
        try {
            bluetoothModel.bleService?.sendPortAndIp(
                port = eShareCache.getPort(), ip = eShareCache.getIpAddress()
            )
        } catch (exception: NullPointerException) {
            Log.e(_tag, "sendPortAndIp: BleService has not been initialized ", exception)
        } catch (exception: UninitializedPropertyAccessException) {
            Log.e(_tag, "sendPortAndIp: BleService has not been initialized ", exception)
        }
    }

    private fun updateEshareState(state: EShareConnectionStatus) =
        eShareRepositoryListener.onEshareStateChanged(state)

    private fun updateInputStream(inputStream: InputStream) =
        eShareRepositoryListener.onInputStreamCreated(inputStream)

    private fun updateBluetoothRadioDisabled() = eShareRepositoryListener.onBluetoothDisabled()

    private val createSocketListener = object : CreateSocketListener {
        override fun onSocketCreated() {
            //send ip and port over bt
            if (bluetoothModel.bleManager.checkIfConnected()) {
                checkIfEshareIsRunningAlready()
            } else {
                Log.d(_tag, "sendPortAndIp: No bt connection")
            }
        }

        override fun onSocketClosed() {
            Log.i(_tag, "onSocketClosed")
            updateEshareState(EShareConnectionStatus.Disconnected)
        }

        override fun onSocketError() {
            Log.e(_tag, "onSocketError")
            updateEshareState(EShareConnectionStatus.Disconnected)
        }
    }

    private val inputStreamListener = object : InputStreamListener {
        override fun onInputStreamCreated(inputStream: InputStream) {
            //start reading from input stream
            Log.i(_tag, "onInputStreamCreated: ")
            updateEshareState(EShareConnectionStatus.Connected)
            updateInputStream(inputStream)
        }

        override fun onInputStreamClosed() {
            Log.i(_tag, "onInputStreamClosed: ")
            //close socket
        }

        override fun onInputStreamError() {
            //close socket
        }
    }

    private val streamOutListener = object : StreamOutListener {
        override fun onConnectionEstablished() {
            Log.d(_tag, "onConnectionEstablished")
            updateEshareState(EShareConnectionStatus.Connected)
        }

        override fun onConnectionClosed() {
            Log.d(_tag, "onConnectionClosed")
            updateEshareState(EShareConnectionStatus.Disconnected)
        }

        override fun onConnectionError() {
            Log.d(_tag, "onConnectionError")

        }

        override fun onConnectionTimeout() {
            Log.d(_tag, "onConnectionTimeout")
            updateEshareState(EShareConnectionStatus.Timeout)
        }
    }

    //endregion
}
