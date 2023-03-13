package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.esightcorp.mobile.app.bluetooth.BluetoothModel.Companion.PERFORM_ACTION_CONFIG_DESCRIPTOR_UUID
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

    private lateinit var MAG_BLE_PERFORM_ACTION_Characteristic: BluetoothGattCharacteristic
    private lateinit var MAG_BLE_TOUCH_EVENT_Characteristic: BluetoothGattCharacteristic
    private lateinit var MAG_BLE_BUTTON_PRESS_Characteristic: BluetoothGattCharacteristic

    //gatt callback
    private val bluetoothGattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Log.d(TAG, "onConnectionStateChange: State connected")
                bluetoothGatt = gatt
                connectionState = STATE_CONNECTED
                broadcastUpdate(ACTION_GATT_CONNECTED)
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.d(TAG, "onConnectionStateChange: State disconnected")
                connectionState = STATE_DISCONNECTED
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        @RequiresApi(33)
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                Log.d(TAG, "onServicesDiscovered: ${gatt?.services.toString()}")
                val service = gatt?.getService(SERVICE_UUID)
                MAG_BLE_BUTTON_PRESS_Characteristic = service?.getCharacteristic(
                    UUID_CHARACTERISTIC_BUTTON_PRESSED)!!
                MAG_BLE_PERFORM_ACTION_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_PERFORM_ACTION)!!
                MAG_BLE_TOUCH_EVENT_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_TOUCH_EVENT)!!
                setCharacteristicNotification()
            }else{
                Log.w(TAG, "onServicesDiscovered received $status" )
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.d(TAG, "onDescriptorWrite: $status")
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

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            Log.d(TAG, "onCharacteristicChanged: $value")
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCharacteristicNotification(){
        bluetoothGatt?.let { gatt ->
            gatt.setCharacteristicNotification(MAG_BLE_PERFORM_ACTION_Characteristic, true)
            val descriptor = MAG_BLE_PERFORM_ACTION_Characteristic.getDescriptor(PERFORM_ACTION_CONFIG_DESCRIPTOR_UUID)
            descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            if(descriptor != null){
                gatt.writeDescriptor(descriptor)
            }
            Log.w(TAG, "setCharacteristicNotification: SET the characteristic notification" )
        }?:  run{
            Log.w(TAG, "setCharacteristicNotification: BluetoothGatt is not initialized" )
        }
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

    /**
     * Broadcast update methods
     *
     * multiple overloads on this one
     */
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
                Log.d(TAG, "broadcastUpdate: $characteristic")
            }
        }
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, device: String){
        val intent = Intent(action)
        intent.putExtra(DEVICE, device)
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
//
//    private fun sendMessage(characteristic: BluetoothGattCharacteristic, hexMessage: Int):Int{
//        var result = -1
//
//        return result
//    }
//    @SuppressLint("MissingPermission")
//    private fun sendPortAndIpAddress(characteristic: BluetoothGattCharacteristic, hexMessage: Int, port: String, ipAddress: String){
//        Log.d(TAG, "sendMessage: ${port}")
//        var result = -1
//        val value = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(hexMessage)
//            .order(ByteOrder.LITTLE_ENDIAN).array(), 2, 4)
//        val port = port.encodeToByteArray()
//        val ipAddress = ipAddress.encodeToByteArray()
//        var writeValue = byteArrayOf()
//        writeValue += value
//        writeValue += port
//        writeValue += ipAddress
//        sendMessage(characteristic, writeValue)
//
//    }

    fun sendIpAndPort(ip: String, port: String){
        sendMessage(MAG_BLE_PERFORM_ACTION_Characteristic, BluetoothPayload.Builder(BluetoothPayload.BleCodes.STREAM_OUT)
            .port(port)
            .ipAddress(ip)
            .build()
            .getByteArrayBlePayload())
    }

    fun sendWifiCreds(ssid: String, pwd: String, type: String){
        Log.i(TAG, "sendWifiCreds: SSID = $ssid, Password = $pwd, Wifi Type is $type")
        sendMessage(MAG_BLE_PERFORM_ACTION_Characteristic, BluetoothPayload.Builder(BluetoothPayload.BleCodes.WIFI_CREDS)
            .ssid(ssid)
            .wifiPwd(pwd)
            .wifiType(type)
            .build().getByteArrayBlePayload())
    }

    @SuppressLint("MissingPermission")
    private fun sendMessage(characteristic: BluetoothGattCharacteristic, byteArray: ByteArray): Boolean{
        var intResult = -1
        var boolResult = false

        bluetoothGatt?.let{gatt ->
            if(Build.VERSION.SDK_INT >= 33){
                intResult = gatt.writeCharacteristic(characteristic, byteArray, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                boolResult = decodeSendMessageResult(intResult)
            }else{
                characteristic.value = byteArray
                boolResult = gatt.writeCharacteristic(characteristic)
            }
        }
        Log.d(TAG, "sendMessage: ${intResult}, $boolResult")
        return boolResult
    }

    private fun decodeSendMessageResult(result: Int): Boolean{
        when(result){
            BluetoothStatusCodes.SUCCESS -> {
                Log.d(TAG, "decodeSendMessageResult: SUCCESS")
            }
            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ALLOWED -> {
                Log.d(TAG, "decodeSendMessageResult: BLUETOOTH NOT ALLOWED ")
            }
            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED -> {
                Log.d(TAG, "decodeSendMessageResult: BLUETOOTH NOT ENABLED")
            }
            BluetoothStatusCodes.ERROR_DEVICE_NOT_BONDED -> {
                Log.d(TAG, "decodeSendMessageResult: NOT BONDED ")
            }
            BluetoothStatusCodes.ERROR_GATT_WRITE_NOT_ALLOWED -> {
                Log.d(TAG, "decodeSendMessageResult:  WRITE NOT ALLOWED")
            }
            BluetoothStatusCodes.ERROR_GATT_WRITE_REQUEST_BUSY -> {
                Log.d(TAG, "decodeSendMessageResult: WRITE BUSY")
            }
            BluetoothStatusCodes.ERROR_UNKNOWN ->{
                Log.d(TAG, "decodeSendMessageResult: UNKNOWN")
            }
            else -> {
                Log.d(TAG, "decodeSendMessageResult: UNKNOWN ELSE")
            }
        }
        return false
    }


    //TODO: remove for production, currently only used for debugging
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

//                setCharacteristicNotification(gattCharacteristic, true)
            }
        }
        Log.d(TAG, "logGattServices: CHARACTERISTIC DATA $gattCharacteristicData")
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
        const val DEVICE =
            "com.esightcorp.bluetooth.le.DEVICE"

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2

        val SERVICE_UUID =
            UUID.fromString("1706BBC0-88AB-4B8D-877E-2237916EE929")
        val UUID_CHARACTERISTIC_BUTTON_PRESSED =
            UUID.fromString("603a8cf2-fdad-480b-b1c1-feef15f05260")
        val UUID_CHARACTERISTIC_TOUCH_EVENT =
            UUID.fromString("84f6e3ed-d348-4925-8bea-d7009a0e490a")
        val UUID_CHARACTERISTIC_PERFORM_ACTION =
            UUID.fromString("07fb80d6-6d0b-4253-9f8f-9dd13ad56aff")
        val UUID_DESCRIPTOR_PERFORM_ACTION_CONFIG =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

}
