/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.repositories

import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager

class EShareRepoManagerImpl(private val eshareRepo: EshareRepository) : EShareRepoManager,
    IBleStateManager by BleStateManagerImpl() {

    override fun configureBtConnectionListener(callback: () -> Unit) {
        eshareRepo.setupBtDisconnectListener(callback)
    }
}
