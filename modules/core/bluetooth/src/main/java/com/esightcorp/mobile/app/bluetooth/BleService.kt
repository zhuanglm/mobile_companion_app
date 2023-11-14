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

class BleService : Service() {
    private val _tag = this.javaClass.simpleName

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

    var lastBroadcastTimeEshareError = 0L


    private val characteristicToDescriptorMap: HashMap<BluetoothGattCharacteristic, BluetoothGattDescriptor> =
        hashMapOf()


    //gatt callback
    @SuppressLint("MissingPermission")
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d(
                        _tag, "onConnectionStateChange - STATE_CONNECTED")
                    bluetoothGatt = gatt
                    connectionState = STATE_CONNECTED
                    gatt.requestMtu(REQUEST_MTU_SIZE)
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d(_tag, "onConnectionStateChange - STATE_DISCONNECTED")
                    close()
                    connectionState = STATE_DISCONNECTED
                    broadcastUpdate(ACTION_GATT_DISCONNECTED)
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i(
                _tag,
                "onServicesDiscovered: device ${gatt.device.name} and status is success? ${status == BluetoothGatt.GATT_SUCCESS}"
            )

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)

                val service = gatt.getService(SERVICE_UUID) ?: return
                BUTTON_PRESS_Characteristic =
                    service.getCharacteristic(UUID_CHARACTERISTIC_BUTTON_PRESSED)
                WIFI_INFO_Characteristic = service.getCharacteristic(UUID_CHARACTERISTIC_WIFI_INFO)
                WIFI_INFO_Characteristic.getDescriptor(UUID_DESCRIPTOR_WIFI_INFO)?.let {
                    WIFI_INFO_Descriptor = it
                    characteristicToDescriptorMap[WIFI_INFO_Characteristic] = it
                }

                ERROR_Characteristic = service.getCharacteristic(UUID_CHARACTERISTIC_ERROR)
                ERROR_Characteristic.getDescriptor(UUID_DESCRIPTOR_ERROR)?.let {
                    Log.i(_tag, "onServicesDiscovered: ERROR CHARACTERTISTIC SETTING DESCRIPTOR")
                    ERROR_Descriptor = it
                    characteristicToDescriptorMap[ERROR_Characteristic] = it
                }

                HOTSPOT_Characteristic = service.getCharacteristic(UUID_CHARACTERISTIC_HOTSPOT)
                HOTSPOT_Characteristic.getDescriptor(UUID_DESCRIPTOR_HOTSPOT)?.let {
                    HOTSPOT_Descriptor = it
                    characteristicToDescriptorMap[HOTSPOT_Characteristic] = it
                }

                WIFI_CONNECTION_STATUS_Characteristic =
                    service.getCharacteristic(UUID_CHARACTERISTIC_WIFI_CONNECTION_STATUS)
                WIFI_CONNECTION_STATUS_Characteristic.getDescriptor(
                    UUID_DESCRIPTOR_WIFI_CONNECTION_STATUS
                )?.let {
                    WIFI_CONNECTION_STATUS_Descriptor = it
                    characteristicToDescriptorMap[WIFI_CONNECTION_STATUS_Characteristic] = it
                }

                ESHARE_COMMANDS_Characteristic =
                    service.getCharacteristic(UUID_CHARACTERISTIC_ESHARE_COMMANDS)
                ESHARE_COMMANDS_Characteristic.getDescriptor(UUID_DESCRIPTOR_ESHARE_COMMANDS)
                    ?.let {
                        ESHARE_COMMANDS_Descriptor = it
                        characteristicToDescriptorMap[ESHARE_COMMANDS_Characteristic] = it
                    }

                ESHARE_STATUS_Characteristic =
                    service.getCharacteristic(UUID_CHARACTERISTIC_ESHARE_STATUS)
                ESHARE_STATUS_Characteristic.getDescriptor(UUID_DESCRIPTOR_ESHARE_STATUS)?.let {
                    ESHARE_STATUS_Descriptor = it
                    characteristicToDescriptorMap[ESHARE_STATUS_Characteristic] = it
                }

                while (characteristicToDescriptorMap.isNotEmpty()) {
                    setCharacteristicEnabledNotification(
                        characteristicToDescriptorMap.keys.first(),
                        characteristicToDescriptorMap.values.first(),
                        gatt
                    )
                    characteristicToDescriptorMap.remove(characteristicToDescriptorMap.keys.first())
                }
            } else {
                Log.e(
                    _tag,
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
            Log.d(_tag, "onCharacteristicRead: unknown status ")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(
                    _tag,
                    "onCharacteristicRead: ${value.decodeToString()}"
                )
                when (characteristic) {
                    ESHARE_STATUS_Characteristic -> {
                        broadcastUpdate(ACTION_ESHARE_STATUS, characteristic)
                    }

                    else -> {
                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
                    }
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray
        ) {
            val incoming = String(value, StandardCharsets.UTF_8)
            Log.d(
                _tag,
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
                            Log.i(_tag, "onCharacteristicChanged: not handling this $incoming")
                        }
                    }
                }

                UUID_CHARACTERISTIC_ERROR -> {
                    Log.i(_tag, "onCharacteristicChanged: Error $incoming")
                    when (incoming) {
                        "ERROR_IP_NOT_REACHABLE" -> {
                            val BROADCAST_DEBOUNCE_TIME = 1000  // 1 second
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastBroadcastTimeEshareError > BROADCAST_DEBOUNCE_TIME) {
                                broadcastUpdate(ACTION_ESHARE_IP_NOT_REACHABLE)
                                lastBroadcastTimeEshareError = currentTime
                            }

                        }

                        "ERROR_BUSY" -> {
                            broadcastUpdate(ACTION_ESHARE_BUSY)
                        }

                        "ERROR_ADDR_NOT_FOUND" -> {
                            broadcastUpdate(ACTION_ESHARE_ADDR_NOT_AVAILABLE)
                        }

                        else -> {
                            broadcastUpdate(ACTION_ERROR, incoming)
                        }
                    }
                }

                UUID_CHARACTERISTIC_BUTTON_PRESSED -> {
                    Log.i(_tag, "onCharacteristicChanged: Button pressed  $incoming")

                }

                UUID_CHARACTERISTIC_HOTSPOT -> {
                    broadcastUpdate(ACTION_HOTSPOT, incoming)
                }

                UUID_CHARACTERISTIC_ESHARE_STATUS -> {
                    broadcastUpdate(ACTION_ESHARE_STATUS, incoming)
                }

                UUID_CHARACTERISTIC_WIFI_CONNECTION_STATUS -> {
                    broadcastUpdate(ACTION_WIFI_CONNECTION_STATUS, incoming)
                }

                else -> {
                    Log.i(_tag, "onCharacteristicChanged: not handling this")
                }

            }
        }


        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(
                _tag,
                "onCharacteristicWrite: ${gatt?.device?.name}, ${characteristic?.uuid}, is success? ${status == BluetoothGatt.GATT_SUCCESS} "
            )
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            Log.d(_tag, "onMtuChanged: ${gatt?.device?.name}  ${mtu}")
            broadcastUpdate(ACTION_GATT_CONNECTED)
        }


        override fun onDescriptorRead(
            gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            Log.e(_tag, "onDescriptorRead: ")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
        }


    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    private fun setCharacteristicEnabledNotification(
        characteristic: BluetoothGattCharacteristic,
        descriptor: BluetoothGattDescriptor,
        gatt: BluetoothGatt,
    ) {
        Log.i(_tag, "setCharacteristicEnabledNotification: ${characteristic.uuid} ")
        characteristic.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        gatt.writeDescriptor(descriptor)
        gatt.setCharacteristicNotification(characteristic, true)
    }


    fun initialize(): Boolean {
        bluetoothAdapter =
            (baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (bluetoothAdapter == null) {
            Log.e(_tag, "initialize: Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun connect(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                Log.d(_tag, "connect: $address")
                device.connectGatt(baseContext, false, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Log.w(_tag, "connect: Device not found with provided address.")
                return false
            }
        } ?: kotlin.run {
            Log.e(_tag, "connect: Bluetooth Adapter not initialized")
            return false
        }
        return true
    }

    /**
     * Request a disconnection to the current bluetooth GATT
     *
     * @return true if the request **CAN** be executed, otherwise false.
     * Note: the actual disconnect status will be notified later through `ACTION_GATT_DISCONNECTED`
     */
    @SuppressLint("MissingPermission")
    fun disconnect(): Boolean = when (bluetoothGatt) {
        null -> false
        else -> {
            bluetoothGatt!!.disconnect()
            true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(_tag, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.i(_tag, "onBind: ")
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
        val data: ByteArray? = characteristic.value
        if (data?.isNotEmpty() == true) {
            val dataString: String = data.decodeToString()
            intent.putExtra(EXTRA_DATA, dataString)
        }
        Log.d(_tag, "broadcastUpdate: $characteristic")
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(
        action: String, value: String
    ) {
        Log.i(_tag, "broadcastUpdate: ${action} ${value}")
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
        bluetoothGatt?.readCharacteristic(characteristic) ?: run {
            Log.w(_tag, "readCharacteristic: BluetoothGatt not initialized")
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
        Log.i(_tag, "sendWifiCreds: SSID = $ssid, Password = $pwd, Wifi Type is $type")
        sendMessage(
            WIFI_INFO_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.WIFI_CREDS).ssid(ssid).wifiPwd(pwd)
                .wifiType(type).build().getByteArrayBlePayload()
        )
    }

    fun sendPortAndIp(port: Int, ip: String) {
        Log.i(_tag, "sendPortAndIp: Port = $port, IP = $ip")
        sendMessage(
            ESHARE_COMMANDS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.STREAM_OUT).port(port.toString())
                .ipAddress(ip).build().getByteArrayBlePayload()
        )
    }

    fun startHotspot(ssid: String, password: String) {
        Log.i(_tag, "startHotspot: SSID = $ssid, Password = $password")
        sendMessage(
            HOTSPOT_Characteristic,
            BluetoothPayload.Builder(
                BluetoothPayload.BleCodes.HOTSPOT_CREDS,
                hotspotSsid = ssid,
                hotspotPwd = password
            )
                .build().getByteArrayBlePayload()
        )
    }

    fun stopEshare() {
        sendMessage(
            ESHARE_COMMANDS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.STREAM_OUT_SHUTDOWN).build()
                .getByteArrayBlePayload()
        )
    }

    fun writeUpButtonPress() {
        Log.d(_tag, "writeUpButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.UP).build().getByteArrayBlePayload()
        )
    }

    fun writeDownButtonPress() {
        Log.d(_tag, "writeDownButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.DOWN).build().getByteArrayBlePayload()
        )
    }

    fun writeModeButtonPress() {
        Log.d(_tag, "writeModeButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.MODE).build().getByteArrayBlePayload()
        )
    }

    fun writeMenuButtonPress() {
        Log.d(_tag, "writeMenuButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.MENU).build().getByteArrayBlePayload()
        )
    }

    fun writeVolumeUpButtonPress() {
        Log.d(_tag, "writeVolumeUpButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.VOL_UP).build()
                .getByteArrayBlePayload()
        )
    }

    fun writeVolumeDownButtonPress() {
        Log.d(_tag, "writeVolumeDownButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.VOL_DOWN).build()
                .getByteArrayBlePayload()
        )
    }

    fun writeFinderButtonPressDownEvent() {
        Log.d(_tag, "writeFinderButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.FINDER).build()
                .getByteArrayBlePayload()
        )
    }

    fun writeActionUpEvent() {
        Log.d(_tag, "writeFinderButtonPress: ")
        sendMessage(
            BUTTON_PRESS_Characteristic,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.ACTION_UP).build()
                .getByteArrayBlePayload()
        )
    }

    fun checkIfEshareIsRunning() {
        Log.i(_tag, "checkIfEshareIsRunning: Reading the eShare Status Characteristic")
        readCharacteristic(ESHARE_STATUS_Characteristic)
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
        Log.d(_tag, "sendMessage: SDK 33${intResult}, else $boolResult")
        return boolResult
    }

    private fun decodeSendMessageResult(result: Int): Boolean {
        when (result) {
            BluetoothStatusCodes.SUCCESS -> {
                Log.d(_tag, "decodeSendMessageResult: SUCCESS")
                return true
            }

            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ALLOWED -> {
                Log.d(_tag, "decodeSendMessageResult: BLUETOOTH NOT ALLOWED ")
            }

            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED -> {
                Log.d(_tag, "decodeSendMessageResult: BLUETOOTH NOT ENABLED")
            }

            BluetoothStatusCodes.ERROR_DEVICE_NOT_BONDED -> {
                Log.d(_tag, "decodeSendMessageResult: NOT BONDED ")
            }

            BluetoothStatusCodes.ERROR_GATT_WRITE_NOT_ALLOWED -> {
                Log.d(_tag, "decodeSendMessageResult:  WRITE NOT ALLOWED")
            }

            BluetoothStatusCodes.ERROR_GATT_WRITE_REQUEST_BUSY -> {
                Log.d(_tag, "decodeSendMessageResult: WRITE BUSY")
            }

            BluetoothStatusCodes.ERROR_UNKNOWN -> {
                Log.d(_tag, "decodeSendMessageResult: UNKNOWN")
            }

            else -> {
                Log.d(_tag, "decodeSendMessageResult: UNKNOWN ELSE")
            }
        }
        return false
    }

    companion object {
        const val ACTION_GATT_CONNECTED = "com.esightcorp.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.esightcorp.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_ESHARE_STATUS = "com.esightcorp.bluetooth.le.ACTION_ESHARE_STATUS"
        const val ACTION_GATT_SERVICES_DISCOVERED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "com.esightcorp.bluetooth.le.ACTION_DATA_AVAILABLE"

        const val EXTRA_DATA = "com.esightcorp.bluetooth.le.EXTRA_DATA"
        const val DEVICE = "com.esightcorp.bluetooth.le.DEVICE"

        const val ACTION_WIFI_CONNECTED = "com.esightcorp.wifi.ACTION_WIFI_CONNECTED"
        const val ACTION_WIFI_ERROR = "com.esightcorp.wifi.ACTION_WIFI_ERROR"
        const val ACTION_ERROR = "com.esightcorp.wifi.ACTION_ERROR"
        const val ACTION_WIFI_CONNECTION_STATUS =
            "com.esightcorp.wifi.ACTION_WIFI_CONNECTION_STATUS"
        const val ACTION_HOTSPOT = "com.esightcorp.wifi.ACTION_HOTSPOT"


        const val ACTION_ESHARE_BUSY = "com.esightcorp.wifi.ACTION_ESHARE_BUSY"
        const val ACTION_ESHARE_ADDR_NOT_AVAILABLE =
            "com.esightcorp.wifi.ACTION_ESHARE_ADDR_NOT_AVAILABLE"
        const val ACTION_ESHARE_IP_NOT_REACHABLE =
            "com.esightcorp.wifi.ACTION_ESHARE_IP_NOT_REACHABLE"

        const val REQUEST_MTU_SIZE = 200

        private const val STATE_DISCONNECTED = BluetoothGatt.STATE_DISCONNECTED
        private const val STATE_CONNECTED = BluetoothGatt.STATE_CONNECTED

        val SERVICE_UUID: UUID = UUID.fromString("0000b81d-0000-1000-8000-00805f9b34fb")

        val UUID_DESCRIPTOR_WIFI_INFO: UUID =
            UUID.fromString("6b90805b2-08f9-4168-86f6-d49f5046f7b3")
        val UUID_DESCRIPTOR_ERROR: UUID = UUID.fromString("f385809a-2e27-4630-b189-7ea51b79c058")
        val UUID_DESCRIPTOR_HOTSPOT: UUID = UUID.fromString("7a786414-d6ec-466d-80ca-d9f6a51c2ad1")
        val UUID_DESCRIPTOR_ESHARE_COMMANDS: UUID =
            UUID.fromString("11235813-2134-5589-1442-ac3cf6d01c7d")
        val UUID_DESCRIPTOR_ESHARE_STATUS: UUID =
            UUID.fromString("68daeef1-e849-4530-8bf4-d7c10d6fc3ac")
        val UUID_DESCRIPTOR_WIFI_CONNECTION_STATUS: UUID =
            UUID.fromString("f38a6f74-8e88-456e-aa85-92d28b2c4823")

        val UUID_CHARACTERISTIC_HOTSPOT: UUID =
            UUID.fromString("76a70d9c-276e-4230-9eb0-ea302fdd3c3d")
        val UUID_CHARACTERISTIC_ESHARE_COMMANDS: UUID =
            UUID.fromString("72330422-8c4d-3eca-1234-e2bafeb7dd0d")
        val UUID_CHARACTERISTIC_ESHARE_STATUS: UUID =
            UUID.fromString("72330422-8c4d-3eca-1234-e2bafeb7dd0e")
        val UUID_CHARACTERISTIC_BUTTON_PRESSED: UUID =
            UUID.fromString("603a8cf2-fdad-480b-b1c1-feef15f05260")

        val UUID_CHARACTERISTIC_ERROR: UUID =
            UUID.fromString("2b0605b2-08f9-4168-86f6-d49f5046f7a1")

        val UUID_CHARACTERISTIC_WIFI_CONNECTION_STATUS: UUID =
            UUID.fromString("1530e059-4d5e-46a8-947b-a6714f9ee5b2")
        val UUID_CHARACTERISTIC_WIFI_INFO: UUID =
            UUID.fromString("00001111-2222-6666-9999-00805f9b34fd")

    }

}
