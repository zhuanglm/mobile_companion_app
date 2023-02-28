package com.esightcorp.mobile.app.bluetooth

import android.util.Log
import org.json.JSONObject
import java.nio.charset.StandardCharsets

private const val TAG = "BluetoothPayload"

class BluetoothPayload private constructor(
    val bleCode: BleCodes,
    val SSID: String?,
    val wifiPwd: String?,
    val wifiType: String?,
    val ipAddress: String?,
    val port: String?
) {

    data class Builder(
        private var bleCode: BleCodes,
        private var SSID: String? = null,
        private var wifiPwd: String? = null,
        private var wifiType: String? = null,
        private var ipAddress: String? = null,
        private var port: String? = null
    ) {
        //        fun bleCode(bleCode: BleCodes) = apply { this.bleCode = bleCode }
        fun ssid(ssid: String) = apply { this.SSID = ssid }
        fun wifiPwd(password: String) = apply { this.wifiPwd = password }
        fun wifiType(type: String) = apply { this.wifiType = type }
        fun ipAddress(address: String) = apply { this.ipAddress = address }
        fun port(port: String) = apply { this.port = port }
        fun build() = BluetoothPayload(bleCode, SSID, wifiPwd, wifiType, ipAddress, port)
    }

    enum class BleCodes(val code: String) {
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

    fun getByteArrayBlePayload(): ByteArray {
        var byteArray = byteArrayOf()
        val charset = Charsets.UTF_8
        when (bleCode) {
            BleCodes.STREAM_OUT -> {
                val code = (BleCodes.STREAM_OUT.code).plus(delimiter).toByteArray(charset)
                byteArray += code
                if (port != null && ipAddress != null) {
                    val port = port.plus(delimiter).toByteArray(charset)
                    byteArray += port
                    val ip = ipAddress.toByteArray(charset)
                    byteArray += ip
                }
            }
            BleCodes.STREAM_OUT_SHUTDOWN -> {
                val code = (BleCodes.STREAM_OUT_SHUTDOWN.code).toByteArray(charset)
                byteArray += code
            }
            BleCodes.HOTSPOT_CREDS -> {
                val code = BleCodes.HOTSPOT_CREDS.code.toByteArray(charset)
                byteArray += code
            }
            BleCodes.WIFI_CREDS -> {
                if (SSID != null && wifiPwd != null && wifiType != null) {
                    val outgoingJson =
                        JSONObject()
                            .put(code, BleCodes.WIFI_CREDS.code)
                            .put(ssid, SSID)
                            .put(pwd, wifiPwd)
                            .put(type, wifiType)
                    byteArray += outgoingJson.toString().toByteArray(StandardCharsets.UTF_8)
                    Log.i(TAG, "getByteArrayBlePayload: size of byte array -> ${byteArray.size}")
                }
            }
        }
        Log.d(TAG, "getByteArrayBlePayload: ${String(byteArray, charset)}")
        return byteArray
    }

    companion object {
        const val delimiter = ":::"
        const val ssid: String = "ssid"
        const val pwd: String = "pwd"
        const val type: String = "type"
        const val code: String = "code"
    }

}
