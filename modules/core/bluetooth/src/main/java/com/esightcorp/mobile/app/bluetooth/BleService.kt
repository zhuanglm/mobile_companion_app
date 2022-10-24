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
                connectionState = STATE_CONNECTED
                broadcastUpdate(ACTION_GATT_CONNECTED)
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.d(TAG, "onConnectionStateChange: State disconnected")
                connectionState = STATE_DISCONNECTED
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            Log.d(TAG, "onCharacteristicRead: ${characteristic.toString()}" )
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
                bluetoothGatt = device.connectGatt(baseContext, true, bluetoothGattCallback)
            }catch (exception: IllegalArgumentException){
                Log.w(TAG, "connect: Device not found with provided address.")
                return false
            }
        } ?: kotlin.run {
            Log.w(TAG, "connect: Bluetooth Adapter not initialized")
            return false
        }
        return false
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

    companion object{
        const val ACTION_GATT_CONNECTED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED =
            "com.esightcorp.bluetooth.le.ACTION_GATT_DISCONNECTED"
        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2
    }
}