package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.content.getSystemService
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val TAG = "BleService"

class BleService : Service(){

    private val binder = LocalBinder()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var connectionState = STATE_DISCONNECTED

    //gatt callback
    private val bluetoothGattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Log.d(TAG, "onConnectionStateChange: state connected")
                bluetoothGatt = gatt
                connectionState = STATE_CONNECTED
                broadcastUpdate(ACTION_GATT_CONNECTED)
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.d(TAG, "onConnectionStateChange: State disconnected")
                connectionState = STATE_DISCONNECTED
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                Log.d(TAG, "onServicesDiscovered: ${gatt?.services.toString()}")
                logGattServices(gatt?.services)
            }else{
                Log.w(TAG, "onServicesDiscovered received $status" )
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                Log.e(TAG, "onCharacteristicRead: ${characteristic.value}" )
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean ){
        bluetoothGatt?.let { gatt ->
            gatt.setCharacteristicNotification(characteristic, enabled)

            if(CHARACTERISTIC_BUTTON_PRESSED == characteristic.uuid ||
                CHARACTERISTIC_PERFORM_ACTION == characteristic.uuid ||
                CHARACTERISTIC_TOUCH_EVENT == characteristic.uuid){
                val descriptor = characteristic.getDescriptor(PERFORM_ACTION_CONFIG_DESCRIPTOR_UUID)
                descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                if(descriptor != null){
                    gatt.writeDescriptor(descriptor)
                }
            }
        }?:  run{
            Log.w(TAG, "setCharacteristicNotification: BluetoothGatt is not initialized", )
        }
    }


    @SuppressLint("MissingPermission")
    fun writeCharacteristic(){
        bluetoothGatt?.let { gatt ->
            gatt.writeCharacteristic()        }
    }

    fun initialize():Boolean{
        bluetoothAdapter = (baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if(bluetoothAdapter == null){
            Log.e(TAG, "initialize: Unable to obtain a BluetoothAdapter. " )
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun connect(address: String): Boolean{
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                Log.d(TAG, "connect: $address")
                bluetoothGatt = device.connectGatt(baseContext, false, bluetoothGattCallback)
            }catch (exception: IllegalArgumentException){
                Log.w(TAG, "connect: Device not found with provided address.")
                return false
            }
        } ?: kotlin.run {
            Log.w(TAG, "connect: Bluetooth Adapter not initialized")
            return false
        }
        return true
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: ")
        return binder
    }

    inner class LocalBinder: Binder(){
        fun getService(): BleService{
            return this@BleService
        }
    }

    private fun broadcastUpdate(action: String){
        val intent = Intent(action)
        sendBroadcast(intent)
    }
    
    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic){
        val intent = Intent(action)
        when(characteristic.uuid){
            else -> {
                val data: ByteArray? = characteristic.value
                if(data?.isNotEmpty() == true){
                    val hexString: String = data.joinToString(separator = " ") {
                        String.format("%02X", it)
                    }
                    intent.putExtra(EXTRA_DATA, "$data\n$hexString")
                }
                Log.d(TAG, "broadcastUpdate: ${characteristic.toString()}")
            }
        }
        sendBroadcast(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    @SuppressLint("MissingPermission")
    fun getSupportedGattServices(): List<BluetoothGattService>? {
        Log.e(TAG, "getSupportedGattServices: " )
        return bluetoothGatt?.services
    }

    @SuppressLint("MissingPermission")
    fun discoverServices(): Boolean{
        return bluetoothGatt?.discoverServices() ?: false
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic){
        bluetoothGatt?.let{ gatt ->
            gatt.readCharacteristic(characteristic)
        }?: run{
            Log.w(TAG, "readCharacteristic: BluetoothGatt not initialized")
        }
    }

    @SuppressLint("MissingPermission")
    private fun close(){
        bluetoothGatt?.let{ gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    private fun logGattServices(gattServices: List<BluetoothGattService>?){
        if(gattServices == null) return
        val NAME = "NAME"
        val LIST_UUID = "UUID"
        var uuid: String?
        val gattServiceData: MutableList<HashMap<String, String>> = mutableListOf()
        val gattCharacteristicData: MutableList<ArrayList<HashMap<String, String>>> = mutableListOf()

        gattServices.forEach { gattService ->
            val currentServiceData = hashMapOf<String, String>()
            uuid = gattService.uuid.toString()
            currentServiceData[NAME] = "unknown"
            currentServiceData[LIST_UUID] = uuid!!
            gattServiceData += currentServiceData

            val gattCharacteristicGroupData: ArrayList<HashMap<String, String>> = arrayListOf()
            val gattCharacteristics = gattService.characteristics
            val charas: MutableList<BluetoothGattCharacteristic> = mutableListOf()
            gattCharacteristics.forEach{ gattCharacteristic ->
                charas += gattCharacteristic
                val currentCharaData: HashMap<String, String> = hashMapOf()
                uuid = gattCharacteristic.uuid.toString()
                currentCharaData[NAME] = "characteristic"
                currentCharaData[LIST_UUID] = uuid!!
                gattCharacteristicGroupData += currentCharaData
                gattCharacteristicData += gattCharacteristicGroupData

                setCharacteristicNotification(gattCharacteristic, true)
            }
        }
        Log.d(TAG, "logGattServices: CHARACTERISTIC DATA ${gattCharacteristicData.toString()}")
    }

    companion object{
        const val ACTION_GATT_CONNECTED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE =
            "com.esightcorp.bluetooth.le.ACTION_DATA_AVAILABLE"
        const val EXTRA_DATA =
            "com.esightcorp.bluetooth.le.EXTRA_DATA"

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2

        val SERVICE_UUID =
            UUID.fromString("1706BBC0-88AB-4B8D-877E-2237916EE929")
        val CHARACTERISTIC_BUTTON_PRESSED =
            UUID.fromString("603a8cf2-fdad-480b-b1c1-feef15f05260")
        val CHARACTERISTIC_TOUCH_EVENT =
            UUID.fromString("84f6e3ed-d348-4925-8bea-d7009a0e490a")
        val CHARACTERISTIC_PERFORM_ACTION =
            UUID.fromString("07fb80d6-6d0b-4253-9f8f-9dd13ad56aff")
        val PERFORM_ACTION_CONFIG_DESCRIPTOR_UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

}
