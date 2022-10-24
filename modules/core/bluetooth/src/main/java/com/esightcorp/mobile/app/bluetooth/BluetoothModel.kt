package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.lang.reflect.Method
import com.esightcorp.mobile.app.bluetooth.BleService.LocalBinder

private const val TAG = "BluetoothModel"

class BluetoothModel constructor(
    val context: Context
){

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    private var bleService: BleService? = null
    private var connected = false
    val connectionStatusMap = hashMapOf<BluetoothDevice, Boolean>()
    val bleScanResult = mutableListOf<BluetoothDevice>()
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    private val SCAN_PERIOD: Long = 20000 //20 seconds, what we had in e4
    lateinit var bluetoothModelListener: BluetoothModelListener


    /**
     * Service lifecycle callback
     */
    private val serviceConnection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            bleService = (service as LocalBinder).getService()
            bleService?.let {bluetooth ->
                if(!bluetooth.initialize()){
                    Log.e(TAG, "onServiceConnected: Unable to initialize bluetooth" )
                }
                //perform device connection
                Log.d(TAG, "onServiceConnected: Perform device connection")
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            bleService = null
            context.unregisterReceiver(gattUpdateReceiver)
        }
    }

    /**
     * receiver to capture changes to the connection state
     */
    private val gattUpdateReceiver: BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action){
                BleService.ACTION_GATT_CONNECTED -> {
                    connected = true
                    Log.e(TAG, "onReceive: CONNECTED" )
                }
                BleService.ACTION_GATT_DISCONNECTED -> {
                    connected = false
                    Log.e(TAG, "onReceive: DISCONNECTED" )
                }
            }

        }
    }

    init {
        val gattServiceIntent = Intent(context, BleService::class.java)
        context.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
    }

    /**
     * Listener coming in from view model, should be used to send data back to respository
     */
    fun registerListener(btModelInterface: BluetoothModelListener) {
        this.bluetoothModelListener = btModelInterface

    }

    /**
     * Scan and then trigger callback to repository when ready
     */
    fun triggerBleScan(){
        scanLeDevices()
        mapBleScanResultToDeviceAndConnectionStatus()
    }


    /**
     * Takes the result of the ble scan [bleScanResult] and adds the current connection status to it for the ui
     */
    @SuppressLint("MissingPermission")
    private fun mapBleScanResultToDeviceAndConnectionStatus(){
        bleScanResult.forEach { device ->
            Log.d("TAG", "logPairedDevices: Device name: ${device.name} ")
            Log.d("TAG", "logPairedDevices: hardware address ${device.address}")
            val pair = isConnected(device)
            connectionStatusMap[pair.first] = pair.second
        }
        bluetoothModelListener.mapOfDevicesReady(connectionStatusMap)
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


    /**
     * Used to scan for ble devices
     * uses the 'scanning' boolean
     */
    @SuppressLint("MissingPermission")
    private fun scanLeDevices() {
        if (!scanning) {
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
                bluetoothModelListener.onScanFinished()
            }, SCAN_PERIOD)
            scanning = true
            bluetoothModelListener.onScanStarted()
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            bluetoothModelListener.onScanFailed(-1)
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    /**
     * Callback for Ble Scan, this will trigger flows based on what it finds
     */
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            bluetoothModelListener.onScanFailed(errorCode)
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
                        bluetoothModelListener.onBleDeviceFound(result)
                    }
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            if (results != null) {
                bluetoothModelListener.onBatchScanResults(results)
            }
        }

    }

    fun connectToDevice(device: BluetoothDevice){
        bleService?.connect(device.address)
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter?{
        return IntentFilter().apply {
            addAction(BleService.ACTION_GATT_CONNECTED)
            addAction(BleService.ACTION_GATT_DISCONNECTED)
        }
    }
}