package com.esightcorp.mobile.app.eshare.repositories

import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager

interface EShareRepoManager: IBleStateManager {
    fun configureBtConnectionListener(callback: () -> Unit)
}
