package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "BleService"

class BleService : Service() {

    private val binder = LocalBinder()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var connectionState = STATE_DISCONNECTED

    private lateinit var WIFI_INFO_Characteristic: BluetoothGattCharacteristic
    private lateinit var BUTTON_PRESS_Characteristic: BluetoothGattCharacteristic
    private lateinit var ERROR_Characteristic: BluetoothGattCharacteristic
    private lateinit var HOTSPOT_Characteristic: BluetoothGattCharacteristic
    private lateinit var WIFI_CONNECTION_STATUS_Characteristic: BluetoothGattCharacteristic
    private lateinit var ESHARE_COMMANDS_Characteristic: BluetoothGattCharacteristic
    private lateinit var ESHARE_STATUS_Characteristic: BluetoothGattCharacteristic

    private lateinit var ESHARE_STATUS_Descriptor: BluetoothGattDescriptor
    private lateinit var ESHARE_COMMANDS_Descriptor: BluetoothGattDescriptor
    private lateinit var WIFI_CONNECTION_STATUS_Descriptor: BluetoothGattDescriptor
    private lateinit var HOTSPOT_Descriptor: BluetoothGattDescriptor
    private lateinit var WIFI_INFO_Descriptor: BluetoothGattDescriptor
    private lateinit var ERROR_Descriptor: BluetoothGattDescriptor

    private val characteristicToDescriptorMap  : HashMap<BluetoothGattCharacteristic, BluetoothGattDescriptor> = hashMapOf()


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
                    UUID_DESCRIPTOR_WIFI_INFO
                )
                ERROR_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_ERROR
                )
                ERROR_Descriptor = ERROR_Characteristic.getDescriptor(
                    UUID_DESCRIPTOR_ERROR
                )
                HOTSPOT_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_HOTSPOT
                )
                HOTSPOT_Descriptor = HOTSPOT_Characteristic.getDescriptor(
                    UUID_DESCRIPTOR_HOTSPOT
                )
                WIFI_CONNECTION_STATUS_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_WIFI_CONNECTION_STATUS
                )
                WIFI_CONNECTION_STATUS_Descriptor =
                    WIFI_CONNECTION_STATUS_Characteristic.getDescriptor(
                        UUID_DESCRIPTOR_WIFI_CONNECTION_STATUS
                    )
                ESHARE_COMMANDS_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_ESHARE_COMMANDS
                )
                ESHARE_COMMANDS_Descriptor = ESHARE_COMMANDS_Characteristic.getDescriptor(
                    UUID_DESCRIPTOR_ESHARE_COMMANDS
                )
                ESHARE_STATUS_Characteristic = service.getCharacteristic(
                    UUID_CHARACTERISTIC_ESHARE_STATUS
                )
                ESHARE_STATUS_Descriptor = ESHARE_STATUS_Characteristic.getDescriptor(
                    UUID_DESCRIPTOR_ESHARE_STATUS
                )

                characteristicToDescriptorMap.put(WIFI_INFO_Characteristic, WIFI_INFO_Descriptor)
                characteristicToDescriptorMap.put(ERROR_Characteristic, ERROR_Descriptor)
                characteristicToDescriptorMap.put(HOTSPOT_Characteristic, HOTSPOT_Descriptor)
                characteristicToDescriptorMap.put(WIFI_CONNECTION_STATUS_Characteristic, WIFI_CONNECTION_STATUS_Descriptor)
                characteristicToDescriptorMap.put(ESHARE_COMMANDS_Characteristic, ESHARE_COMMANDS_Descriptor)
                characteristicToDescriptorMap.put(ESHARE_STATUS_Characteristic, ESHARE_STATUS_Descriptor)

                setCharacteristicEnabledNotification(
                    characteristicToDescriptorMap.keys.first(),
                    characteristicToDescriptorMap.values.first(),
                    gatt
                )
                characteristicToDescriptorMap.remove(characteristicToDescriptorMap.keys.first())

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
                Log.e(
                    TAG,
                    "onCharacteristicRead: ${String(characteristic.value, StandardCharsets.UTF_8)}"
                )
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
            when (characteristic.uuid) {

                UUID_CHARACTERISTIC_WIFI_INFO -> {
                    when (incoming) {
                        "WIFI_SUCCESS" -> {
                            broadcastUpdate(ACTION_WIFI_CONNECTED)
                        }
                        "WIFI_ERROR" -> {
                            broadcastUpdate(ACTION_WIFI_ERROR)
                            gatt.readCharacteristic(ERROR_Characteristic)
                        }
                        else -> {
                            Log.i(TAG, "onCharacteristicChanged: not handling this $incoming")
                        }
                    }
                }
                UUID_CHARACTERISTIC_ERROR -> {
                    broadcastUpdate(ACTION_ERROR, incoming)
                    Log.i(TAG, "onCharacteristicChanged: Error $incoming")
                }
                UUID_CHARACTERISTIC_BUTTON_PRESSED -> {
                    Log.i(TAG, "onCharacteristicChanged: Button pressed  $incoming")

                }
                UUID_CHARACTERISTIC_HOTSPOT -> {
                    Log.i(TAG, "onCharacteristicChanged: Hotspot $incoming")
                }
                UUID_CHARACTERISTIC_ESHARE_COMMANDS -> {
                    Log.i(TAG, "onCharacteristicChanged: Eshare $incoming")
                }
                UUID_CHARACTERISTIC_ESHARE_STATUS -> {
                    Log.i(TAG, "onCharacteristicChanged: Eshare status $incoming")
                }
                UUID_CHARACTERISTIC_WIFI_CONNECTION_STATUS -> {
                    Log.i(TAG, "onCharacteristicChanged: Wifi connection status $incoming")
                }

                else -> {
                    Log.i(TAG, "onCharacteristicChanged: not handling this")
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
            gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            Log.e(TAG, "onDescriptorRead: ")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.e(TAG, "onDescriptorWrite: ")
            if(characteristicToDescriptorMap.isNotEmpty()) {
                Log.i(TAG, "onDescriptorWrite: ${characteristicToDescriptorMap.values.first()}")
                setCharacteristicEnabledNotification(
                    characteristicToDescriptorMap.keys.first(),
                    characteristicToDescriptorMap.values.first(),
                    gatt
                )
                characteristicToDescriptorMap.remove(characteristicToDescriptorMap.keys.first())
            }
        }


    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    private fun setCharacteristicEnabledNotification(
        characteristic: BluetoothGattCharacteristic,
        descriptor: BluetoothGattDescriptor,
        gatt: BluetoothGatt,
    ){
        characteristic.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        gatt.writeDescriptor(descriptor)
        gatt.setCharacteristicNotification(characteristic, true)
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

    private fun broadcastUpdate(
        action: String, value: String
    ) {
        val intent = Intent(action)
        intent.data = Uri.parse(value)
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
        const val ACTION_ERROR = "com.esightcorp.wifi.ACTION_ERROR"

        const val REQUEST_MTU_SIZE = 200

        private const val STATE_DISCONNECTED = BluetoothGatt.STATE_DISCONNECTED
        private const val STATE_CONNECTED = BluetoothGatt.STATE_CONNECTED

        val SERVICE_UUID = UUID.fromString("0000b81d-0000-1000-8000-00805f9b34fb")

        val UUID_DESCRIPTOR_WIFI_INFO: UUID = UUID.fromString("6b90805b2-08f9-4168-86f6-d49f5046f7b3")
        val UUID_DESCRIPTOR_ERROR: UUID = UUID.fromString("f385809a-2e27-4630-b189-7ea51b79c058")
        val UUID_DESCRIPTOR_HOTSPOT: UUID = UUID.fromString("7a786414-d6ec-466d-80ca-d9f6a51c2ad1")
        val UUID_DESCRIPTOR_ESHARE_COMMANDS: UUID = UUID.fromString("11235813-2134-5589-1442-ac3cf6d01c7d")
        val UUID_DESCRIPTOR_ESHARE_STATUS: UUID = UUID.fromString("68daeef1-e849-4530-8bf4-d7c10d6fc3ac")
        val UUID_DESCRIPTOR_WIFI_CONNECTION_STATUS: UUID =
            UUID.fromString("f38a6f74-8e88-456e-aa85-92d28b2c4823")

        val UUID_CHARACTERISTIC_HOTSPOT: UUID = UUID.fromString("76a70d9c-276e-4230-9eb0-ea302fdd3c3d")
        val UUID_CHARACTERISTIC_ESHARE_COMMANDS: UUID =
            UUID.fromString("11235813-2134-5589-1442-ac3cf6d01c7d")
        val UUID_CHARACTERISTIC_ESHARE_STATUS: UUID =
            UUID.fromString("68daeef1-e849-4530-8bf4-d7c10d6fc3ac")
        val UUID_CHARACTERISTIC_BUTTON_PRESSED: UUID =
            UUID.fromString("603a8cf2-fdad-480b-b1c1-feef15f05260")

        val UUID_CHARACTERISTIC_ERROR: UUID = UUID.fromString("2b0605b2-08f9-4168-86f6-d49f5046f7a1")

        val UUID_CHARACTERISTIC_WIFI_CONNECTION_STATUS: UUID =
            UUID.fromString("1530e059-4d5e-46a8-947b-a6714f9ee5b2")
        val UUID_CHARACTERISTIC_WIFI_INFO: UUID = UUID.fromString("00001111-2222-6666-9999-00805f9b34fd")



    }

}
