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
import java.nio.charset.StandardCharsets
import java.util.*

private const val TAG = "BleService"

class BleService : Service() {

    private val binder = LocalBinder()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var connectionState = STATE_DISCONNECTED

    private lateinit var WIFI_INFO_Characteristic: BluetoothGattCharacteristic
    private lateinit var BUTTON_PRESS_Characteristic: BluetoothGattCharacteristic
    private lateinit var ERROR_Characteristic: BluetoothGattCharacteristic
    private lateinit var WIFI_INFO_Descriptor: BluetoothGattDescriptor


    //gatt callback
    @SuppressLint("MissingPermission")
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "onConnectionStateChange: State connected")
                bluetoothGatt = gatt
                connectionState = STATE_CONNECTED
                gatt?.requestMtu(REQUEST_MTU_SIZE)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "onConnectionStateChange: State disconnected")
                connectionState = STATE_DISCONNECTED
                bluetoothGatt = null
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i(
                TAG,
                "onServicesDiscovered: device ${gatt.device.name} and status is success? ${status == BluetoothGatt.GATT_SUCCESS}"
            )
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                val service = gatt.getService(SERVICE_UUID)
                BUTTON_PRESS_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_BUTTON_PRESSED
                )
                WIFI_INFO_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_WIFI_INFO
                )
                WIFI_INFO_Descriptor = WIFI_INFO_Characteristic.getDescriptor(
                    UUID_DESCRIPTOR_WIFI_INFO)
                ERROR_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_ERROR
                )
                WIFI_INFO_Descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                gatt.writeDescriptor(WIFI_INFO_Descriptor)
                gatt.setCharacteristicNotification(WIFI_INFO_Characteristic, true)

            } else {
                Log.e(
                    TAG,
                    "onServicesDiscovered received GATT_FAILURE ? ${status == BluetoothGatt.GATT_FAILURE}"
                )
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            Log.d(TAG, "onCharacteristicRead: unknown status ")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicRead: ${characteristic.value}")
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray
        ) {
            val incoming = String(value, StandardCharsets.UTF_8)
            Log.d(
                TAG,
                "onCharacteristicChanged: ${gatt.device?.name}, ${characteristic.uuid}, $incoming"
            )
            if(incoming.equals("WIFI_ERROR")){
                Log.d(TAG, "onCharacteristicChanged: equals error ")
                broadcastUpdate(ACTION_WIFI_ERROR)
            }
            when(incoming){
                "WIFI_SUCCESS"->{
                    broadcastUpdate(ACTION_WIFI_CONNECTED)
                }
                "WIFI_ERROR" -> {
                    broadcastUpdate(ACTION_WIFI_ERROR)
                }
                else ->{
                    Log.i(TAG, "onCharacteristicChanged: not handling this " + incoming)
                }
            }
        }


        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(
                TAG,
                "onCharacteristicWrite: ${gatt?.device?.name}, ${characteristic?.uuid}, is success? ${status == BluetoothGatt.GATT_SUCCESS} "
            )
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            Log.d(TAG, "onMtuChanged: ${gatt?.device?.name}  ${mtu}")
            broadcastUpdate(ACTION_GATT_CONNECTED)
        }


        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            Log.e(TAG, "onDescriptorRead: " )
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.e(TAG, "onDescriptorWrite: " )
        }


    }


    fun initialize(): Boolean {
        bluetoothAdapter =
            (baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (bluetoothAdapter == null) {
            Log.e(TAG, "initialize: Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun connect(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                Log.d(TAG, "connect: $address")
                bluetoothGatt = device.connectGatt(baseContext, false, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "connect: Device not found with provided address.")
                return false
            }
        } ?: kotlin.run {
            Log.e(TAG, "connect: Bluetooth Adapter not initialized")
            return false
        }
        return true
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): BleService {
            return this@BleService
        }
    }

    /**
     * Broadcast update methods
     *
     * multiple overloads on this one
     */
    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)
        when (characteristic.uuid) {
            else -> {
                val data: ByteArray? = characteristic.value
                if (data?.isNotEmpty() == true) {
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

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    @SuppressLint("MissingPermission")
    fun getSupportedGattServices(): List<BluetoothGattService>? {
        return bluetoothGatt?.services
    }

    @SuppressLint("MissingPermission")
    fun discoverServices(): Boolean {
        return bluetoothGatt?.discoverServices() ?: false
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.let { gatt ->
            gatt.readCharacteristic(characteristic)
        } ?: run {
            Log.w(TAG, "readCharacteristic: BluetoothGatt not initialized")
        }
    }

    @SuppressLint("MissingPermission")
    private fun close() {
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    fun sendWifiCreds(ssid: String, pwd: String, type: String) {
        Log.i(TAG, "sendWifiCreds: SSID = $ssid, Password = $pwd, Wifi Type is $type")
        sendMessage(
            WIFI_INFO_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.WIFI_CREDS).ssid(ssid).wifiPwd(pwd)
                .wifiType(type).build().getByteArrayBlePayload()
        )
    }

    @SuppressLint("MissingPermission")
    private fun sendMessage(
        characteristic: BluetoothGattCharacteristic, byteArray: ByteArray
    ): Boolean {
        var intResult = -1
        var boolResult = false

        bluetoothGatt?.let { gatt ->
            if (Build.VERSION.SDK_INT >= 33) {
                intResult = gatt.writeCharacteristic(
                    characteristic, byteArray, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
                boolResult = decodeSendMessageResult(intResult)
            } else {
                characteristic.value = byteArray
                boolResult = gatt.writeCharacteristic(characteristic)
            }
        }
        Log.d(TAG, "sendMessage: ${intResult}, $boolResult")
        return boolResult
    }

    private fun decodeSendMessageResult(result: Int): Boolean {
        when (result) {
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
            BluetoothStatusCodes.ERROR_UNKNOWN -> {
                Log.d(TAG, "decodeSendMessageResult: UNKNOWN")
            }
            else -> {
                Log.d(TAG, "decodeSendMessageResult: UNKNOWN ELSE")
            }
        }
        return false
    }

    companion object {
        const val ACTION_GATT_CONNECTED = "com.esightcorp.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.esightcorp.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "com.esightcorp.bluetooth.le.ACTION_DATA_AVAILABLE"

        const val EXTRA_DATA = "com.esightcorp.bluetooth.le.EXTRA_DATA"
        const val DEVICE = "com.esightcorp.bluetooth.le.DEVICE"

        const val ACTION_WIFI_CONNECTED = "com.esightcorp.wifi.ACTION_WIFI_CONNECTED"
        const val ACTION_WIFI_ERROR = "com.esightcorp.wifi.ACTION_WIFI_ERROR"

        const val REQUEST_MTU_SIZE = 200

        private const val STATE_DISCONNECTED = BluetoothGatt.STATE_DISCONNECTED
        private const val STATE_CONNECTED = BluetoothGatt.STATE_CONNECTED

        val SERVICE_UUID = UUID.fromString("0000b81d-0000-1000-8000-00805f9b34fb")
        val UUID_CHARACTERISTIC_BUTTON_PRESSED =
            UUID.fromString("603a8cf2-fdad-480b-b1c1-feef15f05260")
        val UUID_CHARACTERISTIC_WIFI_INFO = UUID.fromString("00001111-2222-6666-9999-00805f9b34fd")
        val UUID_CHARACTERISTIC_ERROR = UUID.fromString("2b0605b2-08f9-4168-86f6-d49f5046f7a1")
        val UUID_DESCRIPTOR_WIFI_INFO = UUID.fromString("6b90805b2-08f9-4168-86f6-d49f5046f7b3")
    }

}
