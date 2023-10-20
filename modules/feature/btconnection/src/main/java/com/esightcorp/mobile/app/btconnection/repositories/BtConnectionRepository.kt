package com.esightcorp.mobile.app.btconnection.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "BtConnectionRepository"

class BtConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
){
    private val bluetoothModel: BluetoothModel
    private lateinit var bluetoothConnectionRepositoryCallback: BluetoothConnectionRepositoryCallback


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
            Log.i(TAG, "onScanStarted: ")
            scanStatus(com.esightcorp.mobile.app.utils.ScanningStatus.InProgress)
        }

        override fun onScanFinished() {
            Log.d(TAG, "onScanFinished: ")
            scanStatus(com.esightcorp.mobile.app.utils.ScanningStatus.Success)
        }

        override fun onScanCancelled() {
            scanStatus(ScanningStatus.Cancelled)
        }

        override fun onDeviceDisconnected(device: BluetoothDevice) {
            deviceConnected(device, false)
        }

        override fun onDeviceConnected(device: BluetoothDevice) {
            deviceConnected(device, true)
        }

        override fun onConnectionStateQueried(state: Boolean) {
            Log.i(TAG, "onConnectionStateQueried: $state")
        }

        override fun onBluetoothEnabled() {
            checkBtEnabledStatus()
        }

        override fun onBluetoothDisabled() {
            checkBtEnabledStatus()
        }

        override fun onBluetoothStateQueried(state: Boolean) {
            Log.i(TAG, "onBluetoothStateQueried: $state")
        }

    }

    /**
     * First constructor here is init{}, as don't have any other constructors here.
     */
    init {
        bluetoothModel = BluetoothModel(context)
    }

    fun checkBtEnabledStatus(){
        if(this::bluetoothConnectionRepositoryCallback.isInitialized){
            Log.d(TAG, "checkBtEnabledStatus: ")
            bluetoothConnectionRepositoryCallback.onBtStateUpdate(eSightBleManager.checkIfEnabled())
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

    /**
     * Cancel a scan if one is running
     */
    fun cancelBleScan(){
        bluetoothModel.stopScan()
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
                bluetoothConnectionRepositoryCallback.onBtStateUpdate(eSightBleManager.checkIfEnabled())
                return
            }
        }
        bluetoothConnectionRepositoryCallback.deviceListReady(strippedList)
    }


    /**
     * how we communicate between repo and viewmodel
     */
    fun registerListener(listener: BluetoothConnectionRepositoryCallback){
        Log.d(TAG, "registerListener: ")
        this.bluetoothConnectionRepositoryCallback = listener
        this.checkBtEnabledStatus()
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
        if(this::bluetoothConnectionRepositoryCallback.isInitialized){
            bluetoothConnectionRepositoryCallback.scanStatus(isScanning)
        }
    }

    @SuppressLint("MissingPermission")
    private fun deviceConnected(device: BluetoothDevice, connected: Boolean){
        Log.d(TAG, "onDeviceConnected: ${device.name}")
        if(this::bluetoothConnectionRepositoryCallback.isInitialized){
            bluetoothConnectionRepositoryCallback.onDeviceConnected(device, connected)
        }
    }

    fun resetBtDeviceList(){
        eSightBleManager.resetDeviceList()
    }





}