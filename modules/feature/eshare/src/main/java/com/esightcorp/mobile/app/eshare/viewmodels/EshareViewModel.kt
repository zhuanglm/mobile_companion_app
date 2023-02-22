package com.esightcorp.mobile.app.eshare.viewmodels

import com.esightcorp.mobile.app.eshare.repositories.IEshareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EshareViewModel @Inject constructor(
    eshareRepository: IEshareRepository
)