package com.esightcorp.mobile.app.btconnection.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.IBluetoothModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BtConnectionRepository @Inject constructor(
    @ApplicationContext context: Context
) : IBtConnectionRepository {

    var deviceMap: HashMap<BluetoothDevice, Boolean>
    val bluetoothModel: BluetoothModel
    var bluetoothConnectionState = false
    lateinit var iBtConnectionRepository: IBtConnectionRepository
    val bluetoothModelListener = object : IBluetoothModel {
        override fun isBluetoothCurrentlyConnected(): Boolean {
            TODO("Not yet implemented")
        }

        override fun getPairedDevicesAndConnectionStatus(): HashMap<BluetoothDevice, Boolean> {
            TODO("Not yet implemented")
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

    init {
        bluetoothModel = BluetoothModel(context)
        bluetoothModel.registerListener(bluetoothModelListener)
        deviceMap = bluetoothModel.getPairedDevicesAndConnectionStatus()
    }

    fun updateDeviceMap(){
        deviceMap = bluetoothModel.getPairedDevicesAndConnectionStatus()
    }

    @SuppressLint("MissingPermission")
    fun getMapOfDevices(){
        val strippedMap = hashMapOf<String, Boolean>()
        deviceMap.forEach {
            strippedMap[it.key.name] = it.value
        }
        iBtConnectionRepository.deviceListReady(strippedMap)
    }

    fun checkBtConnectionState(): Boolean {
        bluetoothConnectionState = deviceMap.containsValue(true)
        return bluetoothConnectionState
    }

    fun registerListener(listener: IBtConnectionRepository){
        this.iBtConnectionRepository = listener
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: String){
        deviceMap.keys.forEach { key ->
            if(key.name.equals(device)){
                bluetoothModel.connectToDevice(key)
            }
        }
    }

    override fun scanStatus(isScanning: ScanningStatus) {
        Log.d("TAG", "scanStatus: $isScanning")
        if(isScanning.equals(ScanningStatus.Success)){
            getMapOfDevices()
        }
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.scanStatus(isScanning)
        }
    }

    override fun deviceListReady(deviceList: HashMap<String, Boolean>) {
        if(this::iBtConnectionRepository.isInitialized){
            iBtConnectionRepository.deviceListReady(deviceList)
        }
    }




}