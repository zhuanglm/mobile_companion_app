package com.esightcorp.mobile.app.btconnection.viewmodels

import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BtConnectionViewModel @Inject constructor(
    btConnectionRepository: IBtConnectionRepository
) {
}