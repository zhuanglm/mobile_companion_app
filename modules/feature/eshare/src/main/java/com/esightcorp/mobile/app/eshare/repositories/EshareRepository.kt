package com.esightcorp.mobile.app.eshare.repositories
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.view.Surface
import androidx.compose.animation.core.updateTransition
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.EshareBluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import com.esightcorp.mobile.app.eshare.state.EshareConnectingUiState
import com.esightcorp.mobile.app.networking.SystemStatusListener
import com.esightcorp.mobile.app.networking.WifiModel
import com.esightcorp.mobile.app.networking.WifiModelListener
import com.esightcorp.mobile.app.networking.sockets.CreateSocketListener
import com.esightcorp.mobile.app.networking.sockets.InputStreamListener
import com.esightcorp.mobile.app.networking.sockets.SocketManager
import com.esightcorp.mobile.app.networking.storage.eShareCache
import com.esightcorp.mobile.app.networking.streaming.StreamOutListener
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class EshareRepository @Inject constructor(
    @ApplicationContext context: Context
): BluetoothModelListener, SystemStatusListener, EshareBluetoothModelListener {

    private val TAG = "EshareRepository"
    private val bluetoothModel: BluetoothModel
    private val wifiModel: WifiModel
    private lateinit var eShareRepositoryListener: EshareRepositoryListener
    private var state: eShareConnectionStatus = eShareConnectionStatus.Unknown
    override fun listOfDevicesUpdated() {
        TODO("Not yet implemented")
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        TODO("Not yet implemented")
    }

    override fun onScanFailed(error: Int) {
        TODO("Not yet implemented")
    }

    override fun onScanStarted() {
        TODO("Not yet implemented")
    }

    override fun onScanFinished() {
        TODO("Not yet implemented")
    }

    override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onBluetoothStateChanged() {
        TODO("Not yet implemented")
    }

    private val createSocketListener = object: CreateSocketListener{
        override fun onSocketCreated() {
            //send ip and port over bt
            if (eSightBleManager.checkIfConnected()) {
                try {
                    eSightBleManager.getBleService()
                        ?.sendPortAndIp(
                            port = eShareCache.getPort(),
                            ip = eShareCache.getIpAddress()
                        )
                } catch (exception: NullPointerException) {
                    Log.e(TAG, "sendPortAndIp: BleService has not been initialized ", exception)
                } catch (exception: UninitializedPropertyAccessException) {
                    Log.e(TAG, "sendPortAndIp: BleService has not been initialized ", exception)
                }
            } else {
                Log.d(TAG, "sendPortAndIp: No bt connection")
            }
        }

        override fun onSocketClosed() {
            Log.i(TAG, "onSocketClosed: ")
            updateEshareState(eShareConnectionStatus.Disconnected)
        }

        override fun onSocketError() {
            TODO("Not yet implemented")
        }
    }

    private val inputStreamListener = object : InputStreamListener{
        override fun onInputStreamCreated(inputStream: InputStream) {
            //start reading from input stream
            Log.i(TAG, "onInputStreamCreated: ")
            updateEshareState(eShareConnectionStatus.Connected)
            updateInputStream(inputStream)
        }

        override fun onInputStreamClosed() {
            Log.i(TAG, "onInputStreamClosed: ")
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
        eShareRepositoryListener = eshareRepositoryListener
    }
    
    fun startEshareConnection(){
        Log.i(TAG, "startEshareConnection: Lets get this thing going!!! ")
        if(wifiModel.isWifiEnabled()){
            wifiModel.connectToEshare(createSocketListener, inputStreamListener)
            updateEshareState(eShareConnectionStatus.Initiated)
        }

    }

    private fun updateEshareState(state: eShareConnectionStatus){
        this.state = state
        if(eShareRepositoryListener != null){
            eShareRepositoryListener.onEshareStateChanged(state)
        }
    }

    private fun updateInputStream(inputStream: InputStream){
        if(eShareRepositoryListener != null){
            eShareRepositoryListener.onInputStreamCreated(inputStream)
        }
    }

    fun startStreamFromHMD(surface: Surface, inputStream: InputStream){
        Log.i(TAG, "startStreamFromHMD: ")
        SocketManager.startStreamingFromHMD(surface, inputStream, streamOutListener)

    }

    private val streamOutListener = object : StreamOutListener{
        override fun onConnectionEstablished() {
            Log.d(TAG, "onConnectionEstablished: ")
        }

        override fun onConnectionClosed() {
            Log.d(TAG, "onConnectionClosed: ")
            updateEshareState(eShareConnectionStatus.Disconnected)
        }

        override fun onConnectionError() {
            Log.d(TAG, "onConnectionError: ")
        }

        override fun onConnectionTimeout() {
            Log.d(TAG, "onConnectionTimeout: ")
        }
    }

    fun cancelEshareConnection(){
        Log.i(TAG, "cancelEshareConnection: Nevermind ")

    }

    fun startHotspotOnHMD(){
        Log.i(TAG, "startHotspotOnHMD: ")
        if(eSightBleManager.checkIfConnected()){
            try {
                eSightBleManager.getBleService()?.startHotspot(
                    ssid = HotspotCredentialGenerator.generateHotspotName(),
                    password = HotspotCredentialGenerator.generateHotspotPassword()
                )
            } catch (exception: NullPointerException) {
                Log.e(TAG, "startHotspotOnHMD: BleService has not been initialized ", exception)
            } catch (exception: UninitializedPropertyAccessException) {
                Log.e(TAG, "startHotspotOnHMD: BleService has not been initialized ", exception)
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

    override fun onEshareIpNotReachable() {
        updateEshareState(eShareConnectionStatus.IP_NOT_REACHABLE)
    }

    override fun onEshareAddrNotAvailable() {
        updateEshareState(eShareConnectionStatus.ADDR_NOT_AVAILABLE)
    }

    override fun onEshareBusy() {
        updateEshareState(eShareConnectionStatus.BUSY)
    }

}