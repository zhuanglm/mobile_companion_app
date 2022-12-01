package com.esightcorp.mobile.app.bluetooth

import android.util.Log

private const val TAG = "BluetoothPayload"
class BluetoothPayload private constructor(
    val bleCode: BleCodes,
    val SSID: String?,
    val wifiPwd: String?,
    val wifiType: String?,
    val ipAddress: String?,
    val port: String?
){

    val delimiter = "$"
    val startAndEnd = "*"

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

    enum class BleCodes(val code: String){
        STREAM_OUT("0x02"),
        STREAM_OUT_SHUTDOWN("0x0203"),
        HOTSPOT_CREDS("0x03"),
        WIFI_CREDS("0x11")
        //TODO: make this enum class its own file
    }

    /**
     * For the payload to work properly:
     * there is a character at the beginning [startAndEnd]
     * there is a character between each piece of data [delimiter]
     * and there is a character at the end [startAndEnd]
     *
     * Order is very important here, as it needs to be decoded properly on the glasses
     *
     */

    fun getByteArrayBlePayload(): ByteArray{
        var byteArray = byteArrayOf()
        val charset = Charsets.UTF_8
        when(bleCode){
            BleCodes.STREAM_OUT -> {
                val code = startAndEnd.plus(BleCodes.STREAM_OUT.code).plus(delimiter).toByteArray(charset)
                byteArray += code
                if(port != null && ipAddress != null){
                    val port = port.plus(delimiter).toByteArray(charset)
                    byteArray += port
                    val ip = ipAddress.plus(startAndEnd).toByteArray(charset)
                    byteArray += ip
                }
            }
            BleCodes.STREAM_OUT_SHUTDOWN -> {
                val code = startAndEnd.plus(BleCodes.STREAM_OUT_SHUTDOWN.code).toByteArray(charset)
                byteArray += code
            }
            BleCodes.HOTSPOT_CREDS -> {
                val code = BleCodes.HOTSPOT_CREDS.code.toByteArray(charset)
                byteArray += code
            }
            BleCodes.WIFI_CREDS -> {
                val code = startAndEnd.plus(BleCodes.WIFI_CREDS.code).plus(delimiter).toByteArray(charset)
                byteArray += code
                if(SSID != null && wifiPwd != null && wifiType != null){
                    val ssid = SSID.plus(delimiter).toByteArray(charset)
                    val pwd = wifiPwd.plus(delimiter).toByteArray(charset)
                    val type = wifiType.plus(startAndEnd).toByteArray(charset)
                    byteArray += ssid
                    byteArray += pwd
                    byteArray += type

                }
            }
        }
        Log.d(TAG, "getByteArrayBlePayload: ${String(byteArray, charset)}" )
        return byteArray
    }

    companion object {
    }

}
