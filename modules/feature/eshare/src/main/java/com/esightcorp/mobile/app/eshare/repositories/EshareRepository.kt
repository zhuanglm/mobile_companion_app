package com.esightcorp.mobile.app.eshare.repositories

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.Surface
import com.esightcorp.mobile.app.bluetooth.BluetoothConnectionListener
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothRadioListener
import com.esightcorp.mobile.app.bluetooth.EshareBluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
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
    EshareBluetoothModelListener {

    private val _tag = this.javaClass.simpleName

    private val bluetoothModel: BluetoothModel
    private val wifiModel: WifiModel
    private lateinit var eShareRepositoryListener: EshareRepositoryListener

    //BluetoothModelListener overrides
    override fun onDeviceDisconnected(device: BluetoothDevice) {
        updateBluetoothDeviceDisconnected()
        bluetoothModel.unregisterEshareReceiver()
        bluetoothModel.unregisterHotspotReceiver()
    }

    override fun onDeviceConnected(device: BluetoothDevice) {
        // We should always be connected at this point
    }

    override fun onConnectionStateQueried(state: Boolean) {
        Log.i(_tag, "onConnectionStateQueried: $state")
    }

    override fun onBluetoothEnabled() {
        //this should always be enabled at this point
    }

    override fun onBluetoothDisabled() {
        updateBluetoothRadioDisabled()
    }

    override fun onBluetoothStateQueried(state: Boolean) {
        Log.i(_tag, "onBluetoothStateQueried: $state")
    }

    private val createSocketListener = object : CreateSocketListener {
        override fun onSocketCreated() {
            //send ip and port over bt
            if (eSightBleManager.checkIfConnected()) {
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

    init {
        bluetoothModel = BluetoothModel(context)
        eSightBleManager.setEshareBluetoothListener(this)
        wifiModel = WifiModel(context)
    }

    fun setupEshareListener(eshareRepositoryListener: EshareRepositoryListener) {
        this.eShareRepositoryListener = eshareRepositoryListener
        eShareRepositoryListener.onWifiStateChanged(wifiModel.isWifiEnabled())
    }

    fun startEshareConnection() {
        Log.w(_tag, "startEshareConnection: Lets get this thing going!!!")
        if (wifiModel.isWifiEnabled()) {
            bluetoothModel.registerEshareReceiver()
            wifiModel.openSocket(createSocketListener, inputStreamListener)
            updateEshareState(EShareConnectionStatus.Initiated)
        } else {
            eShareRepositoryListener.onWifiStateChanged(false)
        }
    }

    private fun checkIfEshareIsRunningAlready() {
        //check if eshare is running already
        try {
            eSightBleManager.getBleService()?.checkIfEshareIsRunning()
        } catch (e: Exception) {
            Log.e(_tag, "checkIfEshareIsRunningAlready: ", e)
        }
    }

    private fun writeStreamOutPortIpToHMD() {
        Log.i(_tag, "writeStreamOutPortIpToHMD")
        try {
            eSightBleManager.getBleService()?.sendPortAndIp(
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

    private fun updateBluetoothDeviceDisconnected() =
        eShareRepositoryListener.onBluetoothDeviceDisconnected()

    private fun updateBluetoothRadioDisabled() = eShareRepositoryListener.onBluetoothDisabled()

    fun startStreamFromHMD(surface: Surface, inputStream: InputStream) {
        Log.i(_tag, "startStreamFromHMD: ")
        SocketManager.startStreamingFromHMD(surface, inputStream, streamOutListener)
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

    fun cancelEshareConnection() {
        Log.i(_tag, "cancelEshareConnection: Never mind")
        bluetoothModel.unregisterEshareReceiver()
        eSightBleManager.getBleService()?.stopEshare()
    }

    fun startHotspotOnHMD() {
        Log.i(_tag, "startHotspotOnHMD")
        bluetoothModel.registerHotspotReceiver()
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.startHotspot(
                    ssid = HotspotCredentialGenerator.generateHotspotName(),
                    password = HotspotCredentialGenerator.generateHotspotPassword()
                )
            } catch (exception: NullPointerException) {
                Log.e(_tag, "startHotspotOnHMD: BleService has not been initialized ", exception)
            } catch (exception: UninitializedPropertyAccessException) {
                Log.e(_tag, "startHotspotOnHMD: BleService has not been initialized ", exception)
            }
        }
    }

    fun writeUpButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeUpButtonPress()
            } catch (exception: Exception) {
                Log.e(_tag, "writeUpButtonPress", exception)
            }
        }
    }

    fun writeDownButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeDownButtonPress()
            } catch (exception: Exception) {
                Log.e(_tag, "writeDownButtonPress", exception)
            }
        }

    }

    fun writeModeButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeModeButtonPress()
            } catch (exception: Exception) {
                Log.e(_tag, "writeDownButtonPress", exception)
            }
        }
    }

    fun writeMenuButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeMenuButtonPress()
            } catch (exception: Exception) {
                Log.e(_tag, "writeMenuButtonPress", exception)
            }
        }
    }

    fun writeVolumeUpButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeVolumeUpButtonPress()
            } catch (exception: Exception) {
                Log.e(_tag, "writeVolumeUpButtonPress", exception)
            }
        }
    }

    fun writeVolumeDownButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeVolumeDownButtonPress()
            } catch (exception: Exception) {
                Log.e(_tag, "writeVolumeDownButtonPress", exception)
            }
        }
    }

    fun writeFinderButtonPress() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeFinderButtonPressDownEvent()
            } catch (exception: Exception) {
                Log.e(_tag, "writeFinderButtonPress", exception)
            }
        }
    }

    fun writeActionUpEvent() {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeActionUpEvent()
            } catch (exception: Exception) {
                Log.e(_tag, "writeActionUpEvent", exception)
            }
        }
    }


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

    //endregion
}
