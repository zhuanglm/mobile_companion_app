package com.esightcorp.mobile.app.bluetooth

interface HotspotModelListener {
    fun onHotspotStatusChanged(status: HotspotStatus? = null)
}
