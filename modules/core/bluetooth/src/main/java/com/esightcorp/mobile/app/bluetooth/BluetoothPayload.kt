package com.esightcorp.mobile.app.bluetooth

import android.util.Log
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class BluetoothPayload private constructor(
    private val bleCode: BleCodes,
    private val SSID: String?,
    private val wifiPwd: String?,
    private val wifiType: String?,
    private val ipAddress: String?,
    private val port: String?,
    private val hotspotSsid: String?,
    private val hotspotPwd: String?,
    private val longPress: Boolean?,
    private val buttonName: RemoteButtonName?
) {

    private val _tag = this.javaClass.simpleName

    data class Builder(
        private var bleCode: BleCodes,
        private var SSID: String? = null,
        private var wifiPwd: String? = null,
        private var wifiType: String? = null,
        private var ipAddress: String? = null,
        private var port: String? = null,
        private var hotspotSsid: String? = null,
        private var hotspotPwd: String? = null,
        private var longPress: Boolean? = false,
        private var buttonName: RemoteButtonName? = null
    ) {
        //        fun bleCode(bleCode: BleCodes) = apply { this.bleCode = bleCode }
        fun ssid(ssid: String) = apply { this.SSID = ssid }
        fun wifiPwd(password: String) = apply { this.wifiPwd = password }
        fun wifiType(type: String) = apply { this.wifiType = type }
        fun ipAddress(address: String) = apply { this.ipAddress = address }
        fun port(port: String) = apply { this.port = port }
        fun hotspotSsid(ssid: String) = apply { this.hotspotSsid = ssid }
        fun hotspotPwd(password: String) = apply { this.hotspotPwd = password }
        fun longPress(longPress: Boolean) = apply { this.longPress = longPress }
        fun buttonName(buttonName: RemoteButtonName) = apply { this.buttonName = buttonName }

        fun build() = BluetoothPayload(
            bleCode,
            SSID,
            wifiPwd,
            wifiType,
            ipAddress,
            port,
            hotspotSsid,
            hotspotPwd,
            longPress,
            buttonName,
        )
    }

    enum class BleCodes(val code: String) {
        STREAM_OUT("start_stream_out"),
        STREAM_OUT_SHUTDOWN("stop_stream_out"),
        HOTSPOT_CREDS("0x03"),
        WIFI_CREDS("0x11"),
        BUTTON_PRESS("0x12")
        //TODO: make this enum class its own file
    }

    enum class RemoteButtonName(val value: String) {
        FINDER("finder"),
        MODE("mode"),
        MENU("menu"),
        UP("up"),
        DOWN("down"),
        VOL_UP("volume_up"),
        VOL_DOWN("volume_down"),
        ACTION_UP("action_up"),
    }

    /**
     * Payloads need to go out as a JSON -> ByteArray
     */
    fun getByteArrayBlePayload(): ByteArray {
        var byteArray = byteArrayOf()
        when (bleCode) {
            BleCodes.STREAM_OUT -> {
                if (port != null && ipAddress != null) {
                    val outgoingJson = JSONObject()
                        .put(code, BleCodes.STREAM_OUT.code)
                        .put(portName, port)
                        .put(ipAddressName, ipAddress)
                    byteArray += outgoingJson.toString().toByteArray(StandardCharsets.UTF_8)
                }
            }

            BleCodes.STREAM_OUT_SHUTDOWN -> {
                val outgoingJson = JSONObject()
                    .put(code, BleCodes.STREAM_OUT_SHUTDOWN.code)
                byteArray += outgoingJson.toString().toByteArray(StandardCharsets.UTF_8)
            }

            BleCodes.HOTSPOT_CREDS -> {
                if (hotspotPwd != null && hotspotSsid != null) {
                    val outgoingJson = JSONObject()
                        .put(code, BleCodes.HOTSPOT_CREDS.code)
                        .put(ssid, hotspotSsid)
                        .put(pwd, hotspotPwd)
                    byteArray += outgoingJson.toString().toByteArray(StandardCharsets.UTF_8)
                }
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
                }
            }

            BleCodes.BUTTON_PRESS -> {
                if (longPress != null && buttonName != null) {
                    byteArray += buttonName.value.toByteArray(StandardCharsets.UTF_8)
                }
            }
        }

        Log.d(_tag, "getByteArrayBlePayload: \nSize: ${byteArray.size}\nPayload: ${String(byteArray, StandardCharsets.UTF_8)}")

        return byteArray
    }

    companion object {
        const val ssid: String = "ssid"
        const val pwd: String = "pwd"
        const val type: String = "type"
        const val code: String = "command"
        const val portName: String = "port"
        const val ipAddressName: String = "ip"
        const val buttonNameJsonField: String = "button_name"
    }
}
