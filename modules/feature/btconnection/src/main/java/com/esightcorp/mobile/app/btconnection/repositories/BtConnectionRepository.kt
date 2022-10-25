package com.esightcorp.mobile.app.btconnection.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "BtConnectionRepository"

class BtConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
){

    lateinit var deviceMap: HashMap<BluetoothDevice, Boolean>
    val bluetoothModel: BluetoothModel
    var bluetoothConnectionState = false
    lateinit var iBtConnectionRepository: IBtConnectionRepository


    /**
     * Interface to receive callbacks from BluetoothModel
     */
    val bluetoothModelListener = object : BluetoothModelListener {
        override fun isBluetoothCurrentlyConnected(): Boolean {
            TODO("Not yet implemented")
        }
        override fun mapOfDevicesReady(map: HashMap<BluetoothDevice, Boolean>) {
            deviceMap = map
        }
        @SuppressLint("MissingPermission")
        override fun onBleDeviceFound(result: ScanResult) {
            if (result.device.name != null) {
                Log.d("TAG", "onBleDeviceFound: ${result.device.name}")
                updateDeviceMap()
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            TODO("Not yet implemented")
        }

        override fun onScanFailed(error: Int) {
            scanStatus(ScanningStatus.Failed)

        }

        override fun onScanStarted() {
            scanStatus(ScanningStatus.InProgress)
        }

        override fun onScanFinished() {
            scanStatus(ScanningStatus.Success)
        }

    }

    /**
     * First constructor is init{}
     */
    init {
        bluetoothModel = BluetoothModel(context)
        bluetoothModel.registerListener(bluetoothModelListener)
    }


    /**
     * Triggers the BLE scan on the backend
     *
     */
    fun triggerBleScan(){
        Log.d(TAG, "updateDeviceMap: ")
        bluetoothModel.triggerBleScan()
    }

    fun updateDeviceMap(){
        bluetoothModel.mapBleScanResultToDeviceAndConnectionStatus()
    }




    /**
     * Strips out the bluetoothDevice object, and passes a map of <String, Boolean>
     *
     *     This needs to change, need the bt address when the user selects the device, can we do that at this level?
     */
    @SuppressLint("MissingPermission")
    fun getMapOfDevices(){
        val strippedMap = hashMapOf<String, Boolean>()
        deviceMap.forEach {
            strippedMap[it.key.name] = it.value
        }
        iBtConnectionRepository.deviceListReady(strippedMap)
    }

    /**
     * Checks if there is a device currently connected or not, if yes, return true
     */

    fun checkBtConnectionState(): Boolean {
        bluetoothConnectionState = deviceMap.containsValue(true)
        return bluetoothConnectionState
    }

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
        deviceMap.keys.forEach { key ->
            if(key.name.equals(device)){
                bluetoothModel.connectToDevice(key)
            }
        }
    }

    /**
     * Overridden here to be able to call from within the interface
     * gets map of devices once scanning is done
     */
    fun scanStatus(isScanning: ScanningStatus) {
        Log.d("TAG", "scanStatus: $isScanning")
        if(isScanning == ScanningStatus.Success){
            getMapOfDevices()
        }
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.scanStatus(isScanning)
        }
    }

    fun deviceListReady(deviceList: HashMap<String, Boolean>) {
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.deviceListReady(deviceList)
        }
    }




}