package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.runInterruptible
import java.lang.reflect.Method

class BluetoothModel constructor(
    val context: Context
) : IBluetoothModel {

    val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter

    @SuppressLint("MissingPermission")
    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
    val connectionStatusMap = hashMapOf<BluetoothDevice, Boolean>()
    val bleScanResult = mutableListOf<BluetoothDevice>()
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    private val SCAN_PERIOD: Long = 20000 //20 seconds, what we had in e4
    lateinit var iBluetoothModel: IBluetoothModel

    @SuppressLint("MissingPermission")
    override fun getPairedDevicesAndConnectionStatus(): HashMap<BluetoothDevice, Boolean> {
        scanLeDevices()
        bleScanResult.forEach { device ->
            Log.d("TAG", "logPairedDevices: Device name: ${device.name} ")
            Log.d("TAG", "logPairedDevices: hardware address ${device.address}")
            val pair = isConnected(device)
            connectionStatusMap[pair.first] = pair.second
        }
        Log.d("TAG", "getPairedDevicesAndConnectionStatus: $connectionStatusMap")
        return connectionStatusMap
    }


    fun registerListener(btModelInterface: IBluetoothModel) {
        this.iBluetoothModel = btModelInterface

    }

    override fun onBleDeviceFound(result: ScanResult) {
        TODO("Not yet implemented")
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        TODO("Not yet implemented")
    }

    override fun onScanFailed(error: Int) {
    }

    override fun onScanStarted() {
    }

    override fun onScanFinished() {
    }

    private fun isConnected(device: BluetoothDevice): Pair<BluetoothDevice, Boolean> {
        var connectionStatus = false
        try {
            val m: Method = device.javaClass.getMethod("isConnected")
            connectionStatus = m.invoke(device) as Boolean
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
        return Pair(device, connectionStatus)
    }

    @SuppressLint("MissingPermission")
    private fun scanLeDevices() {
        if (!scanning) {
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
                iBluetoothModel.onScanFinished()
            }, SCAN_PERIOD)
            scanning = true
            iBluetoothModel.onScanStarted()
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            onScanFailed(-1)
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            iBluetoothModel.onScanFailed(errorCode)
        }

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result != null) {
                if ((result.device != null) && !bleScanResult.contains(result.device)) {
                    if(result.device.name != null){
                        //TODO: Add check for eSight specific ble device
                        Log.d("TAG", "onScanResult: ${result.device}")
                        bleScanResult.add(result.device)
                        iBluetoothModel.onBleDeviceFound(result)
                    }
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            if (results != null) {
                iBluetoothModel.onBatchScanResults(results)
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice){
        device.connectGatt(context, true, gattCallback)
    }

    val gattCallback= object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.d("TAG", "onConnectionStateChange: gatt: $gatt, $status, $newState")
        }
    }


    //      TODO: Figure out if we need this method call
    @SuppressLint("MissingPermission")
    override fun isBluetoothCurrentlyConnected(): Boolean {
        //var boolLogic = false
        val stateList = listOf<Int>(
            BluetoothProfile.A2DP,
            BluetoothProfile.GATT,
            BluetoothProfile.GATT_SERVER,
            BluetoothProfile.HID_DEVICE,
            BluetoothProfile.HAP_CLIENT
        )
        stateList.forEach { profile ->
            val state = bluetoothAdapter.getProfileConnectionState(profile)
            Log.d("TAG", "isBluetoothCurrentlyConnected: State: ${profile} : ${state}")

        }


        return false
    }


}