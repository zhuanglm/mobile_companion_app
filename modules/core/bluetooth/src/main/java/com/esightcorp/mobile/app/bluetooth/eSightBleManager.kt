package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.util.Log

object eSightBleManager {
    private val _tag = this.javaClass.simpleName

    private const val DEVICE_NAME_CRITERION = "eGo"


    lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var bleService: BleService? = null
    private var bleConnectionStatus = false
    private var connectedDevice: BluetoothDevice? = null
    private var bleDeviceList: MutableList<BluetoothDevice> = mutableListOf()
    private var modelListener: BluetoothModelListener? = null
    private var eshareBluetoothListener: EshareBluetoothModelListener? = null
    private var btConnectionListener: BluetoothConnectionListener? = null
    private var btRadioListener: BluetoothRadioListener? = null

    var hotspotListener: HotspotModelListener? = null

    fun setupBluetoothManager(context: Context) {
        if (!this::bluetoothManager.isInitialized) {
            this.bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }
        if (!this::bluetoothAdapter.isInitialized) {
            this.bluetoothAdapter = bluetoothManager.adapter

        }
        if (!this::bluetoothLeScanner.isInitialized) {
            this.bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    fun setModelListener(listener: BluetoothModelListener) {
        this.modelListener = listener
    }

    fun getModelListener(): BluetoothModelListener? {
        return modelListener
    }

    fun setEshareBluetoothListener(listener: EshareBluetoothModelListener) {
        this.eshareBluetoothListener = listener
    }

    fun getEshareBluetoothListener(): EshareBluetoothModelListener? {
        return eshareBluetoothListener
    }

    fun setBluetoothConnectionListener(listener: BluetoothConnectionListener) {
        this.btConnectionListener = listener
    }

    fun setBluetoothRadioListener(listener: BluetoothRadioListener) {
        this.btRadioListener = listener
    }

    fun getBluetoothConnectionListener(): BluetoothConnectionListener? {
        return btConnectionListener
    }

    fun getBluetoothRadioListener(): BluetoothRadioListener? {
        return btRadioListener
    }


    /**
     * returns true when device is added to the device list
     * returns false when device is already in the device list OR when device does not contain part of our bt name
     * TODO: Confirm the bluetooth name with Product
     * BT name shall always include eGo
     * BT name is set in Mobile API Gateway if a reference to what the current one is, is needed.
     */
    @SuppressLint("MissingPermission")
    @Synchronized
    fun addToBleDeviceList(device: BluetoothDevice): Boolean {
        return if (bleDeviceList.contains(device) || !device.name.contains(DEVICE_NAME_CRITERION)) {
            false
        } else {
            Log.d(_tag, "addToBleDeviceList: ${device.name}")
            this.bleDeviceList.add(device)
            true
        }
    }

    @SuppressLint("MissingPermission")
    fun getBleDeviceList(): MutableList<BluetoothDevice> {
        return this.bleDeviceList
    }

    /*
    Currently we set status to false when we try to connect to the device passed.
    Once we ACTUALLY connect, it flips to true.
     */
    @Synchronized
    fun setConnectedDevice(device: BluetoothDevice, status: Boolean) {
        Log.d(_tag, "setConnectedDevice: $status")
        this.connectedDevice = device
        this.bleConnectionStatus = status
    }

    @Synchronized
    fun resetConnectedDevice() {
        Log.d(_tag, "resetConnectedDevice: ")
        this.connectedDevice = null
        this.bleConnectionStatus = false
    }

    @Synchronized
    fun resetDeviceList() {
        this.bleDeviceList = mutableListOf()
    }

    @Synchronized
    fun getConnectedDevice(): BluetoothDevice? {
        return this.connectedDevice
    }

    fun setupBleService(service: BleService) {
        this.bleService = service
    }

    fun resetBleService() {
        this.bleService = null
    }

    fun getBleService(): BleService? {
        return this.bleService
    }

    fun checkIfConnected(): Boolean = bleConnectionStatus.also {
//        Log.d(_tag, "checkIfConnected: $it")
    }

    fun checkIfEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    @SuppressLint("MissingPermission")
    fun getShortDeviceName() = getConnectedDevice()?.name?.replace("$DEVICE_NAME_CRITERION-", "")
}
