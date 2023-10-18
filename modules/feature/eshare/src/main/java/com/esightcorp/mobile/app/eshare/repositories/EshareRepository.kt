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
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class EshareRepository @Inject constructor(
    @ApplicationContext context: Context
): BluetoothRadioListener, BluetoothConnectionListener, SystemStatusListener, EshareBluetoothModelListener {

    private val TAG = "EshareRepository"
    private val bluetoothModel: BluetoothModel
    private val wifiModel: WifiModel
    private lateinit var eShareRepositoryListener: EshareRepositoryListener
    private var state: eShareConnectionStatus = eShareConnectionStatus.Unknown

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
        Log.i(TAG, "onConnectionStateQueried: $state")
    }

    override fun onBluetoothEnabled() {
       //this should always be enabled at this point
    }

    override fun onBluetoothDisabled() {
        updateBluetoothRadioDisabled()
    }

    override fun onBluetoothStateQueried(state: Boolean) {
        Log.i(TAG, "onBluetoothStateQueried: $state")
    }

    private val createSocketListener = object: CreateSocketListener{
        override fun onSocketCreated() {
            //send ip and port over bt
            if (eSightBleManager.checkIfConnected()) {
                checkIfEshareIsRunningAlready()
            } else {
                Log.d(TAG, "sendPortAndIp: No bt connection")
            }
        }

        override fun onSocketClosed() {
            state = eShareConnectionStatus.Disconnected
            Log.i(TAG, "onSocketClosed: ")
        }

        override fun onSocketError() {
            state = eShareConnectionStatus.Disconnected
            Log.e(TAG, "onSocketError: ")
        }
    }

    private val inputStreamListener = object : InputStreamListener{
        override fun onInputStreamCreated(inputStream: InputStream) {
            //start reading from input stream
            Log.i(TAG, "onInputStreamCreated: ")
            state = eShareConnectionStatus.Connected
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
        this.eShareRepositoryListener = eshareRepositoryListener
        eShareRepositoryListener.onWifiStateChanged(wifiModel.isWifiEnabled())
    }
    
    fun startEshareConnection(){
        Log.i(TAG, "startEshareConnection: Lets get this thing going!!! ")
        if(wifiModel.isWifiEnabled()){
            bluetoothModel.registerEshareReceiver()
            wifiModel.openSocket(createSocketListener, inputStreamListener)
            state = eShareConnectionStatus.Initiated
            updateEshareState(eShareConnectionStatus.Initiated)
        }else{
            eShareRepositoryListener.onWifiStateChanged(false)
        }

    }

    private fun checkIfEshareIsRunningAlready(){
        //check if eshare is running already
        try{
            eSightBleManager.getBleService()?.checkIfEshareIsRunning()
        }catch (e: Exception){
            Log.e(TAG, "checkIfEshareIsRunningAlready: ", e )
        }
    }

    private fun writeStreamOutPortIpToHMD(){
        Log.i(TAG, "writeStreamOutPortIpToHMD: ")
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
    }




    private fun updateEshareState(state: eShareConnectionStatus){
        if(eShareRepositoryListener != null){
            eShareRepositoryListener.onEshareStateChanged(state)
        }
    }

    private fun updateInputStream(inputStream: InputStream){
        if(eShareRepositoryListener != null){
            eShareRepositoryListener.onInputStreamCreated(inputStream)
        }
    }

    private fun updateBluetoothDeviceDisconnected(){
        if(eShareRepositoryListener != null){
            eShareRepositoryListener.onBluetoothDeviceDisconnected()
        }
    }

    private fun updateBluetoothRadioDisabled(){
        if(eShareRepositoryListener != null){
            eShareRepositoryListener.onBluetoothDisabled()
        }
    }



    fun startStreamFromHMD(surface: Surface, inputStream: InputStream){
        Log.i(TAG, "startStreamFromHMD: ")
        SocketManager.startStreamingFromHMD(surface, inputStream, streamOutListener)

    }

    private val streamOutListener = object : StreamOutListener{
        override fun onConnectionEstablished() {
            Log.d(TAG, "onConnectionEstablished: ")
            updateEshareState(eShareConnectionStatus.Connected)
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
            updateEshareState(eShareConnectionStatus.Timeout)
        }
    }

    fun cancelEshareConnection(){
        Log.i(TAG, "cancelEshareConnection: Nevermind ")
        bluetoothModel.unregisterEshareReceiver()
    }

    fun startHotspotOnHMD(){
        Log.i(TAG, "startHotspotOnHMD: ")
        bluetoothModel.registerHotspotReceiver()
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

    fun writeUpButtonPress(longpress: Boolean){
        if(eSightBleManager.checkIfConnected()){
            try{
                eSightBleManager.getBleService()?.writeUpButtonPress(longpress)
            }catch (exception: Exception){
                Log.e(TAG, "writeUpButtonPress:", exception)
            }
        }
    }

    fun writeDownButtonPress(longpress: Boolean){
        if(eSightBleManager.checkIfConnected()){
            try{
                eSightBleManager.getBleService()?.writeDownButtonPress(longpress)
            }catch (exception: Exception){
                Log.e(TAG, "writeDownButtonPress:", exception)
            }
        }

    }
    fun writeModeButtonPress(longpress: Boolean){
        if(eSightBleManager.checkIfConnected()){
            try{
                eSightBleManager.getBleService()?.writeModeButtonPress(longpress)
            }catch (exception: Exception){
                Log.e(TAG, "writeDownButtonPress:", exception)
            }
        }

    }

    fun writeMenuButtonPress(longpress: Boolean) {
        if (eSightBleManager.checkIfConnected()) {
            try {
                eSightBleManager.getBleService()?.writeMenuButtonPress(longpress)
            } catch (exception: Exception) {
                Log.e(TAG, "writeMenuButtonPress:", exception)
            }
        }
    }

    fun writeVolumeUpButtonPress(longpress: Boolean){
        if(eSightBleManager.checkIfConnected()){
            try{
                eSightBleManager.getBleService()?.writeVolumeUpButtonPress(longpress)
            }catch (exception: Exception){
                Log.e(TAG, "writeVolumeUpButtonPress:", exception)
            }
        }

    }

    fun writeVolumeDownButtonPress(longpress: Boolean){
        if(eSightBleManager.checkIfConnected()){
            try{
                eSightBleManager.getBleService()?.writeVolumeDownButtonPress(longpress)
            }catch (exception: Exception){
                Log.e(TAG, "writeVolumeDownButtonPress:", exception)
            }
        }


    }

    fun writeFinderButtonPress(upEvent: Boolean){
        if(eSightBleManager.checkIfConnected()){
            try{
                if(upEvent){
                    eSightBleManager.getBleService()?.writeFinderButtonPressUpEvent()
                }else{
                    eSightBleManager.getBleService()?.writeFinderButtonPressDownEvent()
                }
            }catch (exception: Exception){
                Log.e(TAG, "writeFinderButtonPress:", exception)
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
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onEshareAddrNotAvailable() {
        updateEshareState(eShareConnectionStatus.ADDR_NOT_AVAILABLE)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onEshareBusy() {
        updateEshareState(eShareConnectionStatus.BUSY)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onEshareReady() {
        Log.i(TAG, "onEshareReady: repo")
        writeStreamOutPortIpToHMD()
    }

    override fun onEshareStopped() {
        updateEshareState(eShareConnectionStatus.Disconnected)
        bluetoothModel.unregisterEshareReceiver()
    }

    override fun onUserCancelled() {
        updateEshareState(eShareConnectionStatus.Disconnected)
        bluetoothModel.unregisterEshareReceiver()
    }


}