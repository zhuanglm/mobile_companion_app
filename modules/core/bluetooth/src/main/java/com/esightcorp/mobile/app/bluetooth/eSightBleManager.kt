package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.utils.bluetooth.BleConnectionStatus

@SuppressLint("MissingPermission")
object eSightBleManager {
    private val _tag = this.javaClass.simpleName

    private const val DEVICE_NAME_CRITERION = "eGo"

    private var bluetoothManager: BluetoothManager? = null
    private var bleService: BleService? = null
    private var bleConnectionStatus: BleConnectionStatus = BleConnectionStatus.Unknown
    private var connectedDevice: BluetoothDevice? = null
    private var bleDeviceList: MutableList<BluetoothDevice> = mutableListOf()
    private var modelListener: BluetoothModelListener? = null
    private var eshareBluetoothListener: EshareBluetoothModelListener? = null
    private var btConnectionListener: BluetoothConnectionListener? = null

    var hotspotListener: HotspotModelListener? = null

    fun setupBluetoothManager(context: Context) = when (bluetoothManager) {
        null -> {
            bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }

        else -> {}
    }

    fun getLeScanner() = bluetoothManager?.adapter?.bluetoothLeScanner

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

    fun getBluetoothConnectionListener(): BluetoothConnectionListener? {
        return btConnectionListener
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
        var added = false

        do {
            if (!device.name.contains(DEVICE_NAME_CRITERION)) break

            if (bleDeviceList.find { it.address == device.address } != null) break

            added = bleDeviceList.add(device)
            Log.d(_tag, "addToBleDeviceList: ${device.name} ($device) --> $added")
        } while (false)

        return added
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
    fun setConnectedDevice(device: BluetoothDevice, status: BleConnectionStatus) {
        Log.d(_tag, "setConnectedDevice: $status")
        this.connectedDevice = device
        this.bleConnectionStatus = status
    }

    @Synchronized
    fun resetConnectedDevice() {
        Log.d(_tag, "resetConnectedDevice: ")
        this.connectedDevice = null
        this.bleConnectionStatus = BleConnectionStatus.Unknown
    }

    @Synchronized
    fun resetDeviceList() {
        this.bleDeviceList = mutableListOf()
    }

    @Synchronized
    fun getConnectedDevice(): BluetoothDevice? {
        return this.connectedDevice
    }

    @Synchronized
    fun setBleService(service: BleService?) {
        this.bleService = service
    }

    /**
     * Retrieve a BleService instance (as-is)
     *
     * @return service instance
     */
    @Synchronized
    fun getBleService(): BleService? {
        return bleService
    }

    /**
     * Retrieve a **connected** BleService if any
     *
     * @return
     *   * a BLE service instance (nullable) if a connection has been established
     *   * `null` if there is no active connection
     */
    @Synchronized
    fun getConnectedBleService() = when (checkIfConnected()) {
        false -> null
        true -> bleService
    }

    @Synchronized
    fun checkIfConnected(): Boolean {
        Log.d(_tag, "Bt device connected: $bleConnectionStatus")
        return bleConnectionStatus == BleConnectionStatus.Connected
    }

    @Synchronized
    fun getConnectionStatus() = bleConnectionStatus

    fun checkIfEnabled() = when (bluetoothManager) {
        null -> false
        else -> bluetoothManager?.adapter?.isEnabled ?: false
    }

    @SuppressLint("MissingPermission")
    fun getShortDeviceName() = getConnectedDevice()?.name?.replace("$DEVICE_NAME_CRITERION-", "")
}
