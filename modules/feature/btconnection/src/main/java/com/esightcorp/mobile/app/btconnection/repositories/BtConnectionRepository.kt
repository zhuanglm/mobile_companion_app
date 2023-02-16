package com.esightcorp.mobile.app.btconnection.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "BtConnectionRepository"

class BtConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
){
    val bluetoothModel: BluetoothModel
    lateinit var iBtConnectionRepository: IBtConnectionRepository


    /**
     * Interface to receive callbacks from BluetoothModel
     */
    val bluetoothModelListener = object : BluetoothModelListener {
        override fun isBluetoothCurrentlyConnected(): Boolean {
            TODO("Not yet implemented")
        }

        override fun listOfDevicesUpdated() {
            Log.d(TAG, "listOfDevicesUpdated: ")
            getMapOfDevices()
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            Log.e(TAG, "onBatchScanResults: ")
        }

        override fun onScanFailed(error: Int) {
            scanStatus(com.esightcorp.mobile.app.utils.ScanningStatus.Failed)
            Log.e(TAG, "onScanFailed: Error code - $error")
        }

        override fun onScanStarted() {
            scanStatus(com.esightcorp.mobile.app.utils.ScanningStatus.InProgress)
        }

        override fun onScanFinished() {
            scanStatus(com.esightcorp.mobile.app.utils.ScanningStatus.Success)
        }

        @SuppressLint("MissingPermission")
        override fun onDeviceConnected(device: BluetoothDevice) {
            Log.d(TAG, "onDeviceConnected: ${device.name}")
            deviceConnected(device)
        }

    }

    /**
     * First constructor is init{}
     */
    init {
        bluetoothModel = BluetoothModel(context)
    }

    fun setupBtModelListener(){
        bluetoothModel.registerListener(bluetoothModelListener)
    }


    /**
     * Triggers the BLE scan on the backend
     *
     */
    fun triggerBleScan(){
        bluetoothModel.triggerBleScan()
    }

    /**
     * Strips out the bluetoothDevice object, and passes a map of <String, Boolean>
     *
     *     This needs to change, need the bt address when the user selects the device, can we do that at this level?
     */
    @SuppressLint("MissingPermission")
    fun getMapOfDevices(){
        Log.d(TAG, "getMapOfDevices: ")
        val strippedList = mutableListOf<String>()
        for (bluetoothDevice in eSightBleManager.getBleDeviceList()) {
            strippedList.add(bluetoothDevice.name)
        }
        Log.d(TAG, "getMapOfDevices: at this point we should call the listener")
        iBtConnectionRepository.deviceListReady(strippedList)
    }

    /**
     * Checks if there is a device currently connected or not, if yes, return true
     */

//    fun checkBtConnectionState(): Boolean {
//        bluetoothConnectionState = deviceMap.containsValue(true)
//        return bluetoothConnectionState
//    }

    /**
     * how we communicate between repo and viewmodel
     */
    fun registerListener(listener: IBtConnectionRepository){
        this.iBtConnectionRepository = listener
    }

    /**
     * If device is in device map, and we want to connect, lets push it to the model to connect
     * key  = the BluetoothDevice object you want to connect to
     */
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: String){
        eSightBleManager.getBleDeviceList().forEach { key ->
            if(key.name.equals(device)){
                bluetoothModel.connectToDevice(key)
            }
        }
    }

    /**
     * Overridden here to be able to call from within the interface
     * gets map of devices once scanning is done
     */
    private fun scanStatus(isScanning: com.esightcorp.mobile.app.utils.ScanningStatus) {
        Log.d("TAG", "scanStatus: $isScanning")
        if(isScanning == com.esightcorp.mobile.app.utils.ScanningStatus.Success){
            Log.d(TAG, "scanStatus: GET MAP OF DEVICES")
            getMapOfDevices()
        }
        if(this::iBtConnectionRepository.isInitialized){
            Log.d(TAG, "scanStatus: IS INITIALIZEd")
            iBtConnectionRepository.scanStatus(isScanning)
        }
    }
    @SuppressLint("MissingPermission")
    private fun deviceConnected(device: BluetoothDevice){
        Log.d(TAG, "onDeviceConnected: ${device.name}")
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.onDeviceConnected(device)
        }
    }




}