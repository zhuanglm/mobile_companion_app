/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

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
import java.util.concurrent.LinkedBlockingDeque

class BleService : Service() {
    private val _tag = this.javaClass.simpleName

    private val binder = LocalBinder()
    private val bleWriteOperationDeque = LinkedBlockingDeque<BleGattOperation>()

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var connectionState = STATE_DISCONNECTED
    private var lastBroadcastTimeEshareError = 0L
    private var bleWorkerThread: Thread? = null

    //region Service implementation

    inner class LocalBinder : Binder() {
        fun getService(): BleService {
            return this@BleService
        }
    }

    fun initialize(): Boolean {
        bluetoothAdapter =
            (baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (bluetoothAdapter == null) {
            Log.e(_tag, "initialize: Unable to obtain a BluetoothAdapter.")
            return false
        }
        startBleWorkerThread()

        return true
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.i(_tag, "onBind: ")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }
    //endregion

    //region ESight BLE functionality

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

    fun sendWifiCreds(ssid: String, pwd: String, type: String) {
        Log.i(_tag, "sendWifiCreds: SSID = $ssid, Password = $pwd, Wifi Type is $type")
        sendMessage(
            ESightCharacteristic.WIFI_INFO, BluetoothPayload.Builder(
                BluetoothPayload.BleCodes.WIFI_CREDS,
                SSID = ssid,
                wifiPwd = pwd,
                wifiType = type,
            ).build()
        )
    }

    fun sendPortAndIp(port: Int, ip: String) {
        Log.i(_tag, "sendPortAndIp: Port = $port, IP = $ip")
        sendMessage(
            ESightCharacteristic.ESHARE_COMMANDS, BluetoothPayload.Builder(
                BluetoothPayload.BleCodes.STREAM_OUT, port = port.toString(), ipAddress = ip,
            ).build()
        )
    }

    fun readWifiConnectionStatus() = readCharacteristic(ESightCharacteristic.WIFI_CONNECTION_STATUS)

    fun startHotspot(ssid: String, password: String) {
        Log.i(_tag, "startHotspot: SSID = $ssid, Password = $password")
        sendMessage(
            ESightCharacteristic.HOTSPOT, BluetoothPayload.Builder(
                BluetoothPayload.BleCodes.HOTSPOT_CREDS, hotspotSsid = ssid, hotspotPwd = password,
            ).build()
        )
    }

    fun stopEshare() {
        sendMessage(
            ESightCharacteristic.ESHARE_COMMANDS,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.STREAM_OUT_SHUTDOWN).build()
        )
    }

    fun writeUpButtonPress() {
        Log.d(_tag, "writeUpButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.UP).build()
        )
    }

    fun writeDownButtonPress() {
        Log.d(_tag, "writeDownButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.DOWN).build()
        )
    }

    fun writeModeButtonPress() {
        Log.d(_tag, "writeModeButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.MODE).build()
        )
    }

    fun writeMenuButtonPress() {
        Log.d(_tag, "writeMenuButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.MENU).build()
        )
    }

    fun writeVolumeUpButtonPress() {
        Log.d(_tag, "writeVolumeUpButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.VOL_UP).build()
        )
    }

    fun writeVolumeDownButtonPress() {
        Log.d(_tag, "writeVolumeDownButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.VOL_DOWN).build()
        )
    }

    fun writeFinderButtonPressDownEvent() {
        Log.d(_tag, "writeFinderButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.FINDER).build()
        )
    }

    fun writeActionUpEvent() {
        Log.d(_tag, "writeFinderButtonPress: ")
        sendMessage(
            ESightCharacteristic.BUTTON_PRESSED,
            BluetoothPayload.Builder(BluetoothPayload.BleCodes.BUTTON_PRESS)
                .buttonName(BluetoothPayload.RemoteButtonName.ACTION_UP).build()
        )
    }

    fun checkIfEshareIsRunning() {
        Log.i(_tag, "checkIfEshareIsRunning: Reading the eShare Status Characteristic")
        readCharacteristic(ESightCharacteristic.ESHARE_STATUS)
    }
    //endregion

    //region Internal implementation

    //region GATT implementation

    @SuppressLint("MissingPermission")
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d(_tag, "onConnectionStateChange - STATE_CONNECTED")
                    bluetoothGatt = gatt
                    connectionState = STATE_CONNECTED
                    gatt.requestMtu(REQUEST_MTU_SIZE)
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d(_tag, "onConnectionStateChange - STATE_DISCONNECTED")
                    close()
                    connectionState = STATE_DISCONNECTED
                    broadcastUpdate(BleAction.GATT_DISCONNECTED)
                }
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            Log.d(_tag, "onMtuChanged: ${gatt.device?.name}  $mtu")
            gatt.discoverServices()
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i(_tag, "onServicesDiscovered - device: ${gatt.device.name}, status: $status")
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(_tag, "onServicesDiscovered failed", Exception())
                broadcastUpdate(BleAction.GATT_CONNECT_FAILED)
                return
            }

            gatt.getService(SERVICE_UUID) ?: run {
                Log.e(_tag, "Failed to discover service: $SERVICE_UUID")
                broadcastUpdate(BleAction.GATT_CONNECT_FAILED)
                return
            }

            Log.w(_tag, "onServicesDiscovered - success")

            ESightCharacteristic.values().forEach { chType ->
                configureCharacteristicNotification(gatt, chType)
            }

            broadcastUpdate(BleAction.GATT_CONNECTED)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            logCharacteristic("onCharacteristicRead", characteristic, value, status)

            if (status != BluetoothGatt.GATT_SUCCESS) return

            when (ESightCharacteristic.from(characteristic)) {
                ESightCharacteristic.ESHARE_STATUS -> broadcastUpdate(
                    EShareAction.StatusChanged, value
                )

                else -> broadcastUpdate(ESightBleAction.DataAvailable, value)
            }
        }

        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("onCharacteristicRead(gatt, characteristic, characteristic.value, status)")
        )
        @Suppress("Deprecation")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int,
        ) {
            onCharacteristicRead(gatt, characteristic, characteristic.value, status)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
        ) {
            val incoming = String(value, StandardCharsets.UTF_8)
            logCharacteristic("onCharacteristicChanged", characteristic, value)

            when (val chType = ESightCharacteristic.from(characteristic)) {
                ESightCharacteristic.WIFI_INFO -> {
                    when (incoming) {
                        "WIFI_SUCCESS" -> {
                            broadcastUpdate(ACTION_WIFI_CONNECTED)
                        }

                        "WIFI_ERROR" -> {
                            broadcastUpdate(ACTION_WIFI_ERROR)
                            readCharacteristic(ESightCharacteristic.ERROR_INFO)
                        }

                        else -> {
                            Log.i(_tag, "onCharacteristicChanged: not handling this $incoming")
                        }
                    }
                }

                ESightCharacteristic.ERROR_INFO -> {
                    Log.i(_tag, "onCharacteristicChanged: Error $incoming")
                    when (incoming) {
                        "ERROR_IP_NOT_REACHABLE" -> {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastBroadcastTimeEshareError > BROADCAST_DEBOUNCE_TIME) {
                                broadcastUpdate(EShareAction.IpNotReachable)
                                lastBroadcastTimeEshareError = currentTime
                            } else {
                                Log.e(_tag, "Ignored broadcasting $incoming")
                            }
                        }

                        "ERROR_BUSY" -> broadcastUpdate(EShareAction.Busy)

                        "ERROR_ADDR_NOT_FOUND" -> broadcastUpdate(EShareAction.AddressNotAvailable)

                        "ERROR_ESHARE_DENIED" -> broadcastUpdate(EShareAction.UserDenied)

                        else -> {
                            broadcastUpdate(ACTION_ERROR, incoming)
                        }
                    }
                }

                ESightCharacteristic.HOTSPOT -> {
                    broadcastUpdate(HotspotAction.StatusChanged, incoming)
                }

                ESightCharacteristic.ESHARE_STATUS -> {
                    when (EShareStatus.from(incoming)) {
                        EShareStatus.STOPPED -> broadcastUpdate(
                            EShareAction.StatusChanged,
                            incoming
                        )

                        else -> Log.e(
                            _tag,
                            "onCharacteristicChanged: not handling!\n$chType, data: $incoming"
                        )
                    }
                }

                ESightCharacteristic.WIFI_CONNECTION_STATUS -> {
                    broadcastUpdate(ACTION_WIFI_CONNECTION_STATUS, incoming)
                }

                else -> {
                    Log.e(_tag, "onCharacteristicChanged: not handling!\n$chType, data: $incoming")
                }
            }
        }

        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("onCharacteristicChanged(gatt, characteristic, characteristic.value)")
        )
        @Suppress("Deprecation")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            onCharacteristicChanged(gatt, characteristic, characteristic.value)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int,
        ) {
            logCharacteristic("onCharacteristicWrite", characteristic, status = status)
        }

        /**
         * Log characteristic callback info, for debugging purpose
         */
        private fun logCharacteristic(
            tag: String,
            characteristic: BluetoothGattCharacteristic,
            data: ByteArray? = null,
            status: Int? = null,
        ) {
            val log = StringBuffer("$tag: ")

            log.append("${ESightCharacteristic.from(characteristic)}(${characteristic.uuid}) ")

            data?.let { log.append(" ->> data: ${String(data, StandardCharsets.UTF_8)}, ") }

            status?.let { log.append("success: ${status == BluetoothGatt.GATT_SUCCESS}($status)") }

            Log.w(_tag, log.toString())
        }
    }

    @SuppressLint("MissingPermission")
    @Suppress("Deprecation")
    private fun configureCharacteristicNotification(
        gatt: BluetoothGatt,
        chType: ESightCharacteristic,
    ): Boolean = when (val descriptor = chType.getNotifyDescriptor(gatt)) {
        null -> false
        else -> {
            val characteristic = chType.getCharacteristic(gatt)!!.apply {
                value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            }

            gatt.writeDescriptor(descriptor)
            gatt.setCharacteristicNotification(characteristic, true)
        }
    }

    @SuppressLint("MissingPermission")
    private fun readCharacteristic(chType: ESightCharacteristic) {
        bluetoothGatt?.let { gatt ->
            chType.getCharacteristic(gatt)?.let { gatt.readCharacteristic(it) } ?: run {
                Log.e(_tag, "readCharacteristic - Could not find characteristic: ${chType.uuid}")
            }
        } ?: run {
            Log.e(_tag, "readCharacteristic - BluetoothGatt not initialized")
        }
    }

    @SuppressLint("MissingPermission")
    private fun close() {
        shutdownBleWorker()
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    //region Retry mechanism for Writes

    /**
     * BleBusyException
     * @param message The message that the exception should return
     */
    class BleBusyException(message:String): Exception(message)

    /*
     * The idea behind this is to use the FIFO method to maintain order for the BLE Write requests
     */
    private fun bleWorkerWriteLogic(){
        while (!Thread.currentThread().isInterrupted){
            try {
                val operation = bleWriteOperationDeque.takeFirst()
                try{
                    Log.i("BleService", "bleWorkerWriteLogic: perform write with $operation")
                    performBleWrite(operation)
                } catch (e: BleBusyException){
                    // Re-enqueue at the front if BLE is busy. We want to be able to keep the right order
                    Log.e("BleService", "bleWorkerWriteLogic: ", e )
                    bleWriteOperationDeque.offerFirst(operation)
                }
            }catch (e: InterruptedException){
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun startBleWorkerThread(){
        if(bleWorkerThread == null || !bleWorkerThread!!.isAlive){
            Log.i("BleService", "startBleWorkerThread: ")
            bleWorkerThread = Thread(::bleWorkerWriteLogic)
            bleWorkerThread?.start()
        }else{
            Log.e("BleService", "startBleWorkerThread: Already started!!!!!!!!!! " )
        }

    }
    private fun shutdownBleWorker(){
        Log.i("BleService", "shutdownBleWorker: ")
        bleWorkerThread?.interrupt()
    }

    @Suppress("Deprecation")
    private fun performBleWrite(operation: BleGattOperation){
        Log.i("BleService", "performBleWrite:\nCharacteristic ${operation.characteristic}\nData ${operation.data}")
        when(Build.VERSION.SDK_INT >= 33){
            true -> {
                //write using SDK 33+
                val intResult = bluetoothGatt!!.writeCharacteristic(
                    operation.characteristic, operation.data, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
                if (intResult == BluetoothStatusCodes.ERROR_GATT_WRITE_REQUEST_BUSY){
                    throw BleBusyException("Write Request Busy. Please try again later")
                }
                decodeSendMessageResult(intResult)
            }
            false -> {
                //write using SDK < 33
                operation.characteristic.value = operation.data
                val boolResult = bluetoothGatt!!.writeCharacteristic(operation.characteristic)
                if(!boolResult){
                    throw BleBusyException("Write Request Busy. Please try again later")
                }
            }

        }
    }



    @SuppressLint("MissingPermission", "NewApi")
    private fun sendMessage(chType: ESightCharacteristic, payload: BluetoothPayload) {
        val data = payload.getByteArrayBlePayload()
        val characteristic: BluetoothGattCharacteristic? = chType.getCharacteristic(bluetoothGatt)

        if (characteristic != null){
            Log.i("BleService", "sendMessage: Enqueueing the operation")
            Log.i("BleService", "sendMessage: Is the thread alive? ${bleWorkerThread?.isAlive}")
            bleWriteOperationDeque.offerLast(BleGattOperation(data, characteristic))

        }
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
    //endregion
    //endregion

    //region Broadcast utilities

    @Synchronized
    private fun broadcastUpdate(action: IAction, data: String? = null) {
        Log.i(_tag, "broadcastUpdate: $action ->> $data")

        sendBroadcast(
            Intent(action.actionName()).apply {
                data?.let { putExtra(EXTRA_DATA, it) }
            },
        )
    }

    private fun broadcastUpdate(action: IAction, data: ByteArray?) =
        broadcastUpdate(action, data?.decodeToString())

    /**
     * Broadcast update methods
     *
     * multiple overloads on this one
     */
    @Synchronized
    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    @Deprecated("")
    @Synchronized
    private fun broadcastUpdate(action: String, data: ByteArray?) {
        val intent = Intent(action)
        if (data?.isNotEmpty() == true) {
            val dataString: String = data.decodeToString()
            intent.putExtra(EXTRA_DATA, dataString)
        }
        sendBroadcast(intent)
    }

    @Deprecated("")
    @Synchronized
    private fun broadcastUpdate(action: String, value: String) {
        Log.i(_tag, "broadcastUpdate: $action ->> $value")
        val intent = Intent(action).apply {
            putExtra(EXTRA_DATA, value)
        }
        sendBroadcast(intent)
    }

    //endregion

    //region Characteristic definition

    private enum class ESightCharacteristic(
        val uuid: String,
        private val notifyDescriptorUuid: String? = null,
    ) {
        WIFI_INFO(
            "00001111-2222-6666-9999-00805f9b34fd",
            "6b90805b2-08f9-4168-86f6-d49f5046f7b3",
        ),

        ERROR_INFO(
            "2b0605b2-08f9-4168-86f6-d49f5046f7a1",
            "f385809a-2e27-4630-b189-7ea51b79c058",
        ),

        HOTSPOT(
            "76a70d9c-276e-4230-9eb0-ea302fdd3c3d",
            "7a786414-d6ec-466d-80ca-d9f6a51c2ad1",
        ),

        ESHARE_COMMANDS(
            "72330422-8c4d-3eca-1234-e2bafeb7dd0d",
            "11235813-2134-5589-1442-ac3cf6d01c7d",
        ),

        ESHARE_STATUS(
            "72330422-8c4d-3eca-1234-e2bafeb7dd0e",
            "68daeef1-e849-4530-8bf4-d7c10d6fc3ac",
        ),

        WIFI_CONNECTION_STATUS(
            "1530e059-4d5e-46a8-947b-a6714f9ee5b2",
            "f38a6f74-8e88-456e-aa85-92d28b2c4823",
        ),

        BUTTON_PRESSED("603a8cf2-fdad-480b-b1c1-feef15f05260"),

        ;

        fun getCharacteristic(gatt: BluetoothGatt?) = when (gatt) {
            null -> null
            else -> gatt.getService(SERVICE_UUID)?.getCharacteristic(UUID.fromString(uuid))
        }

        fun getNotifyDescriptor(gatt: BluetoothGatt?) = when (notifyDescriptorUuid) {
            null -> null
            else -> getCharacteristic(gatt)?.getDescriptor(UUID.fromString(notifyDescriptorUuid))
        }

        companion object {
            fun from(characteristic: BluetoothGattCharacteristic): ESightCharacteristic? =
                values().find { it.uuid == characteristic.uuid.toString() }
        }
    }

    //endregion

    //endregion

    companion object {
        @Deprecated("Use ESightBleAction")
        const val ACTION_DATA_AVAILABLE = "com.esightcorp.bluetooth.le.ACTION_DATA_AVAILABLE"

        const val EXTRA_DATA = "com.esightcorp.bluetooth.le.EXTRA_DATA"
        const val DEVICE = "com.esightcorp.bluetooth.le.DEVICE"

        const val ACTION_WIFI_CONNECTED = "com.esightcorp.wifi.ACTION_WIFI_CONNECTED"
        const val ACTION_WIFI_ERROR = "com.esightcorp.wifi.ACTION_WIFI_ERROR"
        const val ACTION_ERROR = "com.esightcorp.wifi.ACTION_ERROR"
        const val ACTION_WIFI_CONNECTION_STATUS =
            "com.esightcorp.wifi.ACTION_WIFI_CONNECTION_STATUS"

        private const val REQUEST_MTU_SIZE = 200

        private const val BROADCAST_DEBOUNCE_TIME = 1000  // 1 second
        private const val STATE_DISCONNECTED = BluetoothGatt.STATE_DISCONNECTED
        private const val STATE_CONNECTED = BluetoothGatt.STATE_CONNECTED

        private val SERVICE_UUID: UUID = UUID.fromString("0000b81d-0000-1000-8000-00805f9b34fb")
    }
}
