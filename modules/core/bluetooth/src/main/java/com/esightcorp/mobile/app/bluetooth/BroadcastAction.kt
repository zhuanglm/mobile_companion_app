package com.esightcorp.mobile.app.bluetooth

import android.content.IntentFilter

interface IAction {
    fun actionName(): String
}

enum class BleAction(private val iName: String) : IAction {
    GATT_CONNECTED("com.esightcorp.bluetooth.le.ACTION_GATT_CONNECTED"),
    GATT_DISCONNECTED("com.esightcorp.bluetooth.le.ACTION_GATT_DISCONNECTED"),
    GATT_CONNECT_FAILED("com.esightcorp.bluetooth.le.ACTION_GATT_CONNECT_FAILED")
    ;

    override fun actionName() = iName

    companion object {
        fun from(action: String?) = values().find { it.actionName() == action }
    }
}

enum class EShareAction(private val iName: String) : IAction {
    StatusChanged("com.esightcorp.bluetooth.le.ACTION_ESHARE_STATUS"),

    Busy("com.esightcorp.wifi.ACTION_ESHARE_BUSY"),

    AddressNotAvailable("com.esightcorp.wifi.ACTION_ESHARE_ADDR_NOT_AVAILABLE"),

    IpNotReachable("com.esightcorp.wifi.ACTION_ESHARE_IP_NOT_REACHABLE"),

    UserDenied("com.esightcorp.wifi.eshare.ACTION_USER_DENIED"),

    ;

    override fun actionName() = iName

    companion object {
        fun from(action: String?): EShareAction? = values().find { it.actionName() == action }
    }
}

enum class ESightBleAction(private val iName: String) : IAction {
    DataAvailable("com.esightcorp.bluetooth.le.ACTION_DATA_AVAILABLE")

    ;

    override fun actionName() = iName

    companion object {
        fun from(action: String?) = values().find { it.actionName() == action }
    }
}

enum class HotspotAction(private val iName: String) : IAction {
    StatusChanged("com.esightcorp.wifi.ACTION_HOTSPOT"),

    ;

    override fun actionName() = iName

    companion object {
        fun from(action: String?) = HotspotAction.values().find { it.actionName() == action }
    }
}

fun String?.toIAction(): IAction? {
    var action: IAction? = null

    arrayListOf(
        BleAction.from(this),
        EShareAction.from(this),
        ESightBleAction.from(this),
        HotspotAction.from(this),

        //TODO: add newly defined IAction here ...
    ).forEach { actItem ->
        actItem?.let {
            action = it
            return@forEach
        }
    }

    return action
}

fun IntentFilter.addAction(action: IAction) = addAction(action.actionName())
