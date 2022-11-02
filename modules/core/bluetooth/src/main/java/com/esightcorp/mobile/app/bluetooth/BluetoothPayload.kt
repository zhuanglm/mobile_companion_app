package com.esightcorp.mobile.app.bluetooth

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class BluetoothPayload private constructor(
    val bleCode: BleCodes,
    val SSID: String?,
    val wifiPwd: String?,
    val wifiType: String?,
    val ipAddress: String?,
    val port: String?
){

    data class Builder(
        private var bleCode: BleCodes,
        private var SSID: String? = null,
        private var wifiPwd: String? = null,
        private var wifiType: String? = null,
        private var ipAddress: String? = null,
        private var port: String? = null
    ){
//        fun bleCode(bleCode: BleCodes) = apply { this.bleCode = bleCode }
        fun ssid(ssid: String) = apply { this.SSID = ssid }
        fun wifiPwd(password: String) = apply { this.wifiPwd = password }
        fun wifiType(type: String) = apply { this.wifiType = type }
        fun ipAddress(address: String) = apply { this.ipAddress = address }
        fun port(port:String) = apply { this.port = port }
        fun build() = BluetoothPayload(bleCode, SSID, wifiPwd, wifiType, ipAddress, port)
    }

    enum class BleCodes(val code: Int){
        STREAM_OUT(0x02),
        STREAM_OUT_SHUTDOWN(0x0203),
        HOTSPOT_CREDS(0x03),
        WIFI_CREDS(0x11)
        //TODO: make this enum class its own file
    }

    fun getByteArrayBlePayload(): ByteArray{
        var byteArray = byteArrayOf()
        when(bleCode){
            BleCodes.STREAM_OUT -> {
                val code = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(BleCodes.STREAM_OUT.code)
                    .order(ByteOrder.LITTLE_ENDIAN).array(), 2, 4)
                byteArray += code
                if(port != null && ipAddress != null){
                    val port = port.encodeToByteArray()
                    byteArray += port
                    val ip = ipAddress.encodeToByteArray()
                    byteArray += ip
                }
            }
            BleCodes.STREAM_OUT_SHUTDOWN -> {
                val code = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(BleCodes.STREAM_OUT_SHUTDOWN.code)
                    .order(ByteOrder.LITTLE_ENDIAN).array(), 2, 4)
                byteArray += code
            }
            BleCodes.HOTSPOT_CREDS -> {
                val code = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(BleCodes.HOTSPOT_CREDS.code)
                    .order(ByteOrder.LITTLE_ENDIAN).array(), 2, 4)
                byteArray += code
            }
            BleCodes.WIFI_CREDS -> {
                val code = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(BleCodes.WIFI_CREDS.code)
                    .order(ByteOrder.LITTLE_ENDIAN).array(), 2, 4)
                byteArray += code
            }
        }
        return byteArray
    }

    companion object {
    }

}