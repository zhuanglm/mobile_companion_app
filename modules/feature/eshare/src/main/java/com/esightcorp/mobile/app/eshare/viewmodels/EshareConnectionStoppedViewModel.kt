package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EshareConnectionStoppedViewModel @Inject constructor(
    application: Application
) : ESightBaseViewModel(application)
