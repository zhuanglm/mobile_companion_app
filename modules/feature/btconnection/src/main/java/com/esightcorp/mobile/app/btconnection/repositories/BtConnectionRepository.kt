package com.esightcorp.mobile.app.btconnection.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.utils.BleConnectionStatus
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BtConnectionRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val _tag = this.javaClass.simpleName

    private val bluetoothModel: BluetoothModel
    private lateinit var bluetoothConnectionRepositoryCallback: BluetoothConnectionRepositoryCallback

    /**
     * Interface to receive callbacks from BluetoothModel
     */
    private val bluetoothModelListener = object : BluetoothModelListener {
        override fun listOfDevicesUpdated() {
            Log.d(_tag, "listOfDevicesUpdated: ")
            getMapOfDevices()
        }

        override fun onScanFailed(error: Int) {
            scanStatus(ScanningStatus.Failed)
            Log.e(_tag, "onScanFailed: Error code - $error")
        }

        override fun onScanStarted() {
            Log.i(_tag, "onScanStarted: ")
            scanStatus(ScanningStatus.InProgress)
        }

        override fun onScanFinished() {
            Log.d(_tag, "onScanFinished: ")
            scanStatus(ScanningStatus.Success)
        }

        override fun onScanCancelled() {
            scanStatus(ScanningStatus.Cancelled)
        }

        override fun onDeviceDisconnected(device: BluetoothDevice) {
            updateDeviceConnectionStatus(device, false)
        }

        override fun onDeviceConnected(device: BluetoothDevice) {
            updateDeviceConnectionStatus(device, true)
        }

        override fun onBluetoothEnabled() {
            checkBtEnabledStatus()
        }

        override fun onBluetoothDisabled() {
            checkBtEnabledStatus()
        }
    }

    /**
     * First constructor here is init{}, as don't have any other constructors here.
     */
    init {
        bluetoothModel = BluetoothModel(context)
    }

    fun checkBtEnabledStatus() {
        val btEnabled: Boolean
        with(bluetoothModel.bleManager) {
            btEnabled = checkIfEnabled()
            Log.d(_tag, "checkBtEnabledStatus - enabled: $btEnabled")

            if (!btEnabled)
                resetConnectedDevice()
        }

        if (this::bluetoothConnectionRepositoryCallback.isInitialized) {
            bluetoothConnectionRepositoryCallback.onBtStateUpdate(btEnabled)
        }
    }

    @Synchronized
    fun setupBtModelListener() {
        with(bluetoothModel.bleManager) {
            setModelListener(bluetoothModelListener)

            // Forward connection status if needed
            getConnectedDevice()?.let { dev ->
                val isConnected = when (getConnectionStatus()) {
                    BleConnectionStatus.Connecting -> null
                    BleConnectionStatus.Connected -> true
                    else -> false
                }
                updateDeviceConnectionStatus(dev, isConnected)
            }
        }
    }

    /**
     * Triggers the BLE scan on the backend
     */
    fun triggerBleScan() {
        bluetoothModel.triggerBleScan()
    }

    /**
     * Cancel a scan if one is running
     */
    fun cancelBleScan() {
        bluetoothModel.stopScan()
    }

    fun getConnectedDevice(): BluetoothDevice? {
        return bluetoothModel.bleManager.getConnectedDevice()
    }

    /**
     * Strips out the bluetoothDevice object, and passes a map of <String, Boolean>
     */
    @SuppressLint("MissingPermission")
    fun getMapOfDevices() {
        val strippedList = mutableListOf<String>()
        with(bluetoothModel.bleManager) {
            getBleDeviceList().filter { it.name != null }.forEach { bluetoothDevice ->
                when (checkIfEnabled()) {
                    true -> strippedList.add(bluetoothDevice.name)
                    false -> {
                        bluetoothConnectionRepositoryCallback.onBtStateUpdate(false)
                        return
                    }
                }
            }
        }

        bluetoothConnectionRepositoryCallback.deviceListReady(strippedList)
    }

    /**
     * how we communicate between repo and viewmodel
     */
    fun registerListener(listener: BluetoothConnectionRepositoryCallback) {
        Log.d(_tag, "registerListener: ")
        this.bluetoothConnectionRepositoryCallback = listener
        this.checkBtEnabledStatus()
    }

    /**
     * If device is in device map, and we want to connect, lets push it to the model to connect
     * key  = the BluetoothDevice object you want to connect to
     */
    @SuppressLint("MissingPermission")
    @Synchronized
    fun connectToDevice(device: String) {
        bluetoothModel.bleManager.getBleDeviceList().forEach { key ->
            if (key.name.equals(device)) {
                bluetoothModel.connectToDevice(key)
            }
        }
    }

    fun disconnectToDevice(): Boolean = bluetoothModel.disconnectToDevice()

    /**
     * Overridden here to be able to call from within the interface
     * gets map of devices once scanning is done
     */
    private fun scanStatus(isScanning: ScanningStatus) {
        if (isScanning == ScanningStatus.Success) {
            getMapOfDevices()
        }
        if (this::bluetoothConnectionRepositoryCallback.isInitialized) {
            bluetoothConnectionRepositoryCallback.scanStatus(isScanning)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateDeviceConnectionStatus(device: BluetoothDevice, connected: Boolean?) {
        Log.d(_tag, "onDeviceConnected: ${device.name}, isConnected: $connected")
        if (this::bluetoothConnectionRepositoryCallback.isInitialized) {
            bluetoothConnectionRepositoryCallback.onDeviceConnected(device, connected)
        }
    }

    fun resetBtDeviceList() {
        bluetoothModel.bleManager.resetDeviceList()
    }
}
