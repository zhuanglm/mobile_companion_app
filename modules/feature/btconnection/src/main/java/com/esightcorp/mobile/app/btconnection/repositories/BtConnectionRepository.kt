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
    private val bluetoothModel: BluetoothModel
    private lateinit var iBtConnectionRepository: IBtConnectionRepository


    /**
     * Interface to receive callbacks from BluetoothModel
     */
    private val bluetoothModelListener = object : BluetoothModelListener {

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
        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.d(TAG, "onDeviceConnected: ${device.name}")
            deviceConnected(device, connected)
        }

        override fun onBluetoothStateChanged() {
            Log.d(TAG, "onBluetoothStateChanged: ")
            checkBtEnabledStatus()
        }

    }

    /**
     * First constructor is init{}
     */
    init {
        bluetoothModel = BluetoothModel(context)
    }

    fun checkBtEnabledStatus(){
        if(this::iBtConnectionRepository.isInitialized){
            Log.d(TAG, "checkBtEnabledStatus: ")
            iBtConnectionRepository.onBtStateUpdate(eSightBleManager.checkIfEnabled())
        }
    }

    fun setupBtModelListener(){
        eSightBleManager.setModelListener(bluetoothModelListener)
        bluetoothModel.checkForConnection()
    }


    /**
     * Triggers the BLE scan on the backend
     */
    fun triggerBleScan(){
        bluetoothModel.triggerBleScan()
    }

    fun getConnectedDevice(): BluetoothDevice?{
        return eSightBleManager.getConnectedDevice()
    }

    /**
     * Strips out the bluetoothDevice object, and passes a map of <String, Boolean>
     */
    @SuppressLint("MissingPermission")
    fun getMapOfDevices(){
        val strippedList = mutableListOf<String>()
        for (bluetoothDevice in eSightBleManager.getBleDeviceList()) {
            if(eSightBleManager.checkIfEnabled()){
                strippedList.add(bluetoothDevice.name)
            }else{
                iBtConnectionRepository.onBtStateUpdate(eSightBleManager.checkIfEnabled())
                return
            }
        }
        iBtConnectionRepository.deviceListReady(strippedList)
    }


    /**
     * how we communicate between repo and viewmodel
     */
    fun registerListener(listener: IBtConnectionRepository){
        Log.d(TAG, "registerListener: ")
        this.iBtConnectionRepository = listener
        this.iBtConnectionRepository.onBtStateUpdate(eSightBleManager.checkIfEnabled())

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
        if(isScanning == com.esightcorp.mobile.app.utils.ScanningStatus.Success){
            getMapOfDevices()
        }
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.scanStatus(isScanning)
        }
    }

    @SuppressLint("MissingPermission")
    private fun deviceConnected(device: BluetoothDevice, connected: Boolean){
        Log.d(TAG, "onDeviceConnected: ${device.name}")
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.onDeviceConnected(device, connected)
        }
    }

    fun resetBtDeviceList(){
        eSightBleManager.resetDeviceList()
    }




}