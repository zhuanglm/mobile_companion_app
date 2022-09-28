package com.esightcorp.mobile.app.wificonnection.viewmodels

import com.esightcorp.mobile.app.wificonnection.repositories.IWifiConnectionRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WifiConnectionViewModel @Inject constructor(
    wifiConnectionRepository: IWifiConnectionRespository
) {
}