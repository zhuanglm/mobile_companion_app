package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import javax.inject.Inject

class BtDisabledViewModel  @Inject constructor(
    application: Application,
): AndroidViewModel(application)