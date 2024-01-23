package com.esightcorp.mobile.app.eshare.repositories

import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager

class EShareRepoManagerImpl(private val eshareRepo: EshareRepository) : EShareRepoManager,
    IBleStateManager by BleStateManagerImpl() {

    override fun configureBtConnectionListener(callback: () -> Unit) {
        eshareRepo.setupBtDisconnectListener(callback)
    }
}
