package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.juul.kable.State

private const val TAG = "eSightBleManager"
object eSightBleManager {


    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var bleService: BleService? = null
    private var bleConnectionStatus = false
    private var connectedDevice: BluetoothDevice? = null
    private var bleDeviceList: MutableList<BluetoothDevice> = mutableListOf()
    private var modelListener: BluetoothModelListener? = null


    fun setupBluetoothManager(context: Context){
        if(!this::bluetoothManager.isInitialized){
            this.bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }
        if(!this::bluetoothAdapter.isInitialized){
            this.bluetoothAdapter = bluetoothManager.adapter

        }
        if(!this::bluetoothLeScanner.isInitialized){
            this.bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    fun setModelListener(listener: BluetoothModelListener){
        this.modelListener = listener
    }

    fun getModelListener(): BluetoothModelListener?{
        return modelListener
    }


    /**
     * returns true when device is added to the device list
     * returns false when device is already in the device list OR when device does not contain part of our bt name
     *
     * BT name is set in Mobile API Gateway if a reference to what the current one is, is needed.
     */
    @SuppressLint("MissingPermission")
    fun addToBleDeviceList(device: BluetoothDevice):Boolean{
        return if(this.bleDeviceList.contains(device) /*|| !device.name.contains("eGo")*/){
            false;
        } else{
            Log.d(TAG, "addToBleDeviceList: ${device.name}")
            this.bleDeviceList.add(device)
            true;
        }
    }

    @SuppressLint("MissingPermission")
    fun getBleDeviceList(): MutableList<BluetoothDevice>{
        bleDeviceList.forEach {
            Log.d(TAG, "getBleDeviceList: ${it.name}")
        }
        return this.bleDeviceList
    }

    /*
    Currently we set status to false when we try to connect to the device passed.
    Once we ACTUALLY connect, it flips to true.
     */
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