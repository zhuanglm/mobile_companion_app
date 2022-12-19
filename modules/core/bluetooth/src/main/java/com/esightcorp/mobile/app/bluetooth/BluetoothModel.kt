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
import com.esightcorp.mobile.app.bluetooth.BleService.LocalBinder
import java.util.*

private const val TAG = "BluetoothModel"

@SuppressLint("MissingPermission")
class BluetoothModel constructor(
    val context: Context
){
    val bleScanResult = mutableListOf<BluetoothDevice>()
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    lateinit var bluetoothModelListener: BluetoothModelListener
    private val bleManager = eSightBleManager


    /**
     * Service lifecycle callback
     */
    private val serviceConnection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            bleManager.setupBleService((service as LocalBinder).getService())
            bleManager.getBleService()?.let {bluetooth ->
                if(!bluetooth.initialize()){
                    Log.e(TAG, "onServiceConnected: Unable to initialize bluetooth" )
                }
                //perform device connection
                Log.d(TAG, "onServiceConnected: Perform device connection")
            }
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            bleManager.resetBleService()
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
                    bleManager.setConnectedDevice(bleManager.getConnectedDevice()!!, true)
                    Log.e(TAG, "onReceive: CONNECTED" )
                    bluetoothModelListener.onDeviceConnected(bleManager.getConnectedDevice()!!)
                    bleManager.discoverServices()

                }
                BleService.ACTION_GATT_DISCONNECTED -> {
                    bleManager.resetConnectedDevice()
                    Log.e(TAG, "onReceive: DISCONNECTED" )
                }
                BleService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    bleManager.getBleService()?.getSupportedGattServices()?.forEach {
                        Log.d(TAG, "onReceive: ${it.uuid}")
                    }
//                    bleManager.getBleService()?.sendIpAndPort("192.168.0.1", "8889")
                    
                }
                BleService.ACTION_DATA_AVAILABLE -> {
                    Log.d(TAG, "onReceive DATA AVAILABLE: ${intent.extras}")
                }
            }

        }
    }

    init {
        val gattServiceIntent = Intent(context, BleService::class.java)
        bleManager.setupBluetoothManager(context)
        context.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())

    }

    /**
     * Listener coming in from view model, should be used to send data back to respository
     */
    fun registerListener(btModelInterface: BluetoothModelListener) {
        this.bluetoothModelListener = btModelInterface
        val connectedDeviceList = bleManager.bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)
        Log.d(TAG, "INIT BLUETOOTH MODEL: ${connectedDeviceList.toString()} ")
        if(connectedDeviceList.isNotEmpty()){
            bleManager.setConnectedDevice(connectedDeviceList[0], true)
            bluetoothModelListener.onDeviceConnected(bleManager.getConnectedDevice()!!)
        }

    }

    /**
     * Scan and then trigger callback to repository when ready
     */
    fun triggerBleScan(){
        scanLeDevices()
        getDeviceList()
    }


    /**
     * Takes the result of the ble scan [bleScanResult] and adds the current connection status to it for the ui
     */
    @SuppressLint("MissingPermission")
    fun getDeviceList(){
        bluetoothModelListener.listOfDevicesReady(bleScanResult)
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
                bleManager.bluetoothLeScanner.stopScan(leScanCallback)
                bluetoothModelListener.onScanFinished()
            }, SCAN_PERIOD)
            scanning = true
            bluetoothModelListener.onScanStarted()
            bleManager.bluetoothLeScanner.startScan(leScanCallback)
        } else {
            bluetoothModelListener.onScanFailed(-1)
            scanning = false
            bleManager.bluetoothLeScanner.stopScan(leScanCallback)
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
        val result = bleManager.getBleService()?.connect(device.address)
        if(result == true){
            bleManager.setConnectedDevice(device, false)
        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter?{
        return IntentFilter().apply {
            addAction(BleService.ACTION_GATT_CONNECTED)
            addAction(BleService.ACTION_GATT_DISCONNECTED)
            addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED)
        }
    }

    companion object{
        val PERFORM_ACTION_CONFIG_DESCRIPTOR_UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        private const val SCAN_PERIOD: Long = 20000 //20 seconds, what we had in e4

    }


}