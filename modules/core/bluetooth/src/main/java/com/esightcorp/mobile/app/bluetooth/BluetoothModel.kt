package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BleService.LocalBinder
import com.esightcorp.mobile.app.utils.BleConnectionStatus
import java.util.*

@SuppressLint("MissingPermission", "UnspecifiedRegisterReceiverFlag")
class BluetoothModel(
    val context: Context
) {
    private val _tag = this.javaClass.simpleName

    private var scanning = false
        @Synchronized get
        @Synchronized set

    private val handler = Handler(Looper.getMainLooper())
    private val bleManager = eSightBleManager

    /**
     * Service lifecycle callback
     */
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            Log.w(_tag, "onServiceConnected ...")

            when (bleManager.getBleService()) {
                null -> {
                    val bleService = (service as LocalBinder).getService()
                    when (bleService.initialize()) {
                        false -> Log.e(_tag, "onServiceConnected: Unable to initialize bluetooth")
                        true -> bleManager.setupBleService(bleService)
                    }
                }

                else -> return
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.w(_tag, "onServiceDisconnected!!!")
            bleManager.resetBleService()
            context.unregisterReceiver(gattUpdateReceiver)
        }
    }

    fun shutdownReceivers() {
        try {
            context.unregisterReceiver(gattUpdateReceiver)
            context.unregisterReceiver(bluetoothStateReceiver)
        } catch (e: Exception) {
            Log.e(_tag, "shutdownReceivers: ${e.message}")
        }
    }

    /**
     * receiver to capture changes to the connection state
     */
    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @Synchronized
        override fun onReceive(context: Context?, intent: Intent) {
            when (val action = intent.action.toIAction()) {
                BleAction.GATT_CONNECTED -> {
                    Log.i(_tag, "onReceive - gattUpdateReceiver --> Action: $action")

                    bleManager.getConnectedDevice()?.let { dev ->
                        bleManager.setConnectedDevice(dev, BleConnectionStatus.Connected)
                        bleManager.getModelListener()?.onDeviceConnected(dev)
                        bleManager.getBluetoothConnectionListener()?.onDeviceConnected(dev)
                    }
                }

                BleAction.GATT_CONNECT_FAILED,
                BleAction.GATT_DISCONNECTED -> {
                    Log.e(_tag, "onReceive - gattUpdateReceiver --> Action: $action")

                    bleManager.getConnectedDevice()?.let {
                        bleManager.getModelListener()?.onDeviceDisconnected(it)
                        bleManager.getBluetoothConnectionListener()?.onDeviceDisconnected(it)
                    }
                    bleManager.resetConnectedDevice()
                }
            }
        }
    }

    private val eShareReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @Synchronized
        override fun onReceive(context: Context?, intent: Intent) {
            val exData = intent.extras?.getString(BleService.EXTRA_DATA)
            val actType = intent.action.toIAction()
            Log.i(_tag, "eShareReceiver - $actType, data: $exData")

            val eShareListener = bleManager.getEshareBluetoothListener()
            when (actType) {
                EShareAction.StatusChanged -> when (EShareStatus.from(exData)) {
                    EShareStatus.READY -> eShareListener?.onEshareReady()

                    EShareStatus.STOPPED -> eShareListener?.onEshareStopped()

                    EShareStatus.RUNNING -> eShareListener?.onEshareBusy()

                    // Handler other status???
                    else -> Log.e(_tag, "Not handling: $exData")
                }

                EShareAction.AddressNotAvailable -> eShareListener?.onEshareAddrNotAvailable()

                EShareAction.IpNotReachable -> eShareListener?.onEshareIpNotReachable()

                EShareAction.Busy -> eShareListener?.onEshareBusy()

                EShareAction.UserDenied -> eShareListener?.onUserCancelled()

                ESightBleAction.DataAvailable ->
                    eShareListener?.onWifiConnectionStatusChanged(exData)

                else -> Log.e(_tag, "Unknown action: ${intent.action}", Exception())
            }
        }
    }

    private val hotspotReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @Synchronized
        override fun onReceive(context: Context, intent: Intent) {
            val exData = intent.extras?.getString(BleService.EXTRA_DATA)
            val actType = intent.action.toIAction()
            Log.i(_tag, "hotspotReceiver - $actType, data: $exData")

            when (actType) {
                HotspotAction.StatusChanged -> {
                    bleManager.hotspotListener?.onHotspotStatusChanged(HotspotStatus.from(exData))
                }

                else -> {
                    Log.e(_tag, "Ignored status: $exData")
                }
            }
        }
    }

    private val bluetoothStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @Synchronized
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR
                    )) {
                        BluetoothAdapter.STATE_OFF -> {
                            // Bluetooth is disabled
                            Log.d(_tag, "onReceive: Bluetooth is disabled")
                            eSightBleManager.getModelListener()?.onBluetoothDisabled()
                        }

                        BluetoothAdapter.STATE_TURNING_OFF -> {
                            Log.d(_tag, "onReceive: Bluetooth is turning off ")
                            // Bluetooth is turning off
                        }

                        BluetoothAdapter.STATE_ON -> {
                            // Bluetooth is enabled
                            Log.d(_tag, "onReceive: Bluetooth is Enabled")
                            eSightBleManager.getModelListener()?.onBluetoothEnabled()
                        }

                        BluetoothAdapter.STATE_TURNING_ON -> {
                            Log.d(_tag, "onReceive: Bluetooth is turning on")
                            // Bluetooth is turning on
                        }
                    }
                }
            }
        }
    }
    private val btStateFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

    //need to handle the cleanup here... receivers are going bonkers
    init {
        val gattServiceIntent = Intent(context, BleService::class.java)
        bleManager.setupBluetoothManager(context)
        context.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                gattUpdateReceiver, makeGattUpdateIntentFilter(), Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        }
        context.registerReceiver(bluetoothStateReceiver, btStateFilter)
    }

    fun registerGattUpdateReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                gattUpdateReceiver, makeGattUpdateIntentFilter(), Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        }
    }

    fun unregisterGattUpdateReceiver() {
        try {
            context.unregisterReceiver(gattUpdateReceiver)
        } catch (e: Exception) {
            Log.e(_tag, "deregisterGattUpdateReceiver: ${e.message}")
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Synchronized
    fun registerEshareReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                eShareReceiver, makeEShareIntentFilter(), Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(eShareReceiver, makeEShareIntentFilter())
        }
    }

    @Synchronized
    fun unregisterEshareReceiver() {
        try {
            context.unregisterReceiver(eShareReceiver)
        } catch (e: Exception) {
            Log.e(_tag, "deregisterEshareReceiver: ${e.message}")
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun registerHotspotReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                hotspotReceiver, makeHotspotIntentFilter(), Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(hotspotReceiver, makeHotspotIntentFilter())
        }
    }

    fun unregisterHotspotReceiver() {
        try {
            context.unregisterReceiver(hotspotReceiver)
        } catch (e: Exception) {
            Log.e(_tag, "deregisterHotspotReceiver: ${e.message}")
        }
    }

    /**
     * Scan and then trigger callback to repository when ready
     */
    fun triggerBleScan() {
        scanLeDevices()
    }

    /**
     * Used to scan for ble devices
     * uses the 'scanning' boolean
     */
    @SuppressLint("MissingPermission")
    private fun scanLeDevices() {
        val leScanner = bleManager.getLeScanner()

        if (!scanning) {
            handler.postDelayed(
                {
                    scanning = false
                    leScanner?.stopScan(leScanCallback)
                    bleManager.getModelListener()?.onScanFinished()
                },
                SCAN_PERIOD,
            )
            scanning = true
            bleManager.getModelListener()?.onScanStarted()
            leScanner?.startScan(leScanCallback)
        } else {
            bleManager.getModelListener()?.onScanFailed(-1)
            scanning = false
            leScanner?.stopScan(leScanCallback)
        }
    }

    fun stopScan() {
        bleManager.getLeScanner()?.stopScan(leScanCallback)
        scanning = false
        bleManager.getModelListener()?.onScanCancelled()
    }

    /**
     * Callback for Ble Scan, this will trigger flows based on what it finds
     */
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Log.i(_tag, "onScanFailed - errorCode: $errorCode")
            bleManager.getModelListener()?.onScanFailed(errorCode)
        }

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result == null || result.device == null || result.device.name == null) return

            if (bleManager.addToBleDeviceList(result.device)) {
                Log.d(_tag, "onScanResult - added: ${result.device.name}")
                bleManager.getModelListener()?.listOfDevicesUpdated()
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.i(_tag, "onBatchScanResults - size: ${results?.size}")

            results?.let { bleManager.getModelListener()?.onBatchScanResults(results) }
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        val result = bleManager.getBleService()?.connect(device.address)
        if (result == true) {
            bleManager.setConnectedDevice(device, status = BleConnectionStatus.Connecting)
            stopScan()
        }
    }

    fun disconnectToDevice(): Boolean = (bleManager.getBleService()?.disconnect() ?: false)

    private fun makeGattUpdateIntentFilter() = IntentFilter().apply {
        addAction(BleAction.GATT_CONNECTED)
        addAction(BleAction.GATT_DISCONNECTED)
    }

    private fun makeEShareIntentFilter() = IntentFilter().apply {
        EShareAction.values().forEach { addAction(it) }
        addAction(ESightBleAction.DataAvailable)
    }

    private fun makeHotspotIntentFilter(): IntentFilter = IntentFilter().apply {
        addAction(HotspotAction.StatusChanged)
    }

    companion object {
        private const val SCAN_PERIOD: Long = 20000 //20 seconds, what we had in e4
    }
}
