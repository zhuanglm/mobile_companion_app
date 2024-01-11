package com.esightcorp.mobile.app.companion.repositories

import com.esightcorp.mobile.app.bluetooth.IBleEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor() : IBleEventListener {
    var appListener: IAppRepoListener? = null

    override fun onDisconnected() {
        appListener?.onDisconnected()
    }
}
