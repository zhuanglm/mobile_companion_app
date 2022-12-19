package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import com.esightcorp.mobile.app.utils.ScanningStatus


object eSightBleManager {


    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var bleService: BleService? = null
    private var bleConnectionStatus = false
    private var connectedDevice: BluetoothDevice? = null
    private var scanningStatus: ScanningStatus = ScanningStatus.Unknown


    fun setupBluetoothManager(context: Context){
        this.bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        this.bluetoothAdapter = bluetoothManager.adapter
        this.bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    fun setScanningStatus(status: ScanningStatus){
        this.scanningStatus = status
    }

    fun setConnectedDevice(device: BluetoothDevice, status: Boolean){
        this.connectedDevice = device
        this.bleConnectionStatus = status
    }

    fun resetConnectedDevice() {
        this.connectedDevice = null
        this.bleConnectionStatus = false
    }

    fun getConnectedDevice():BluetoothDevice?{
        return this.connectedDevice
    }

    fun setupBleService(service: BleService){
        this.bleService = service
    }

    fun resetBleService(){
        this.bleService = null
    }

    fun getBleService():BleService?{
        return this.bleService
    }

    fun checkIfConnected():Boolean{
        return bleConnectionStatus
    }

    fun discoverServices(){
        bleService?.discoverServices()
    }









}