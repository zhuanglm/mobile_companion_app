package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.state.EshareStoppedUiState
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EshareConnectionStoppedViewModel @Inject constructor(
    application: Application
) : ESightBaseViewModel(application) {

    private var _uiState = MutableStateFlow(EshareStoppedUiState())
    val uiState: StateFlow<EshareStoppedUiState> = _uiState.asStateFlow()

    fun updateState(stoppedReason: EShareStoppedReason?) = when (stoppedReason) {
        EShareStoppedReason.USER_DECLINED -> _uiState.update {
            it.copy(
                titleId = R.string.label_eshare_connection_unsuccessful_title,
                descriptionId = R.string.label_eshare_stopped_user_declined_message
            )
        }

        EShareStoppedReason.EXISTING_ACTIVE_SESSION -> _uiState.update {
            it.copy(
                titleId = R.string.label_eshare_stopped_busy_title,
                descriptionId = R.string.label_eshare_stopped_busy_message
            )
        }

        EShareStoppedReason.REMOTE_STOPPED -> _uiState.update {
            it.copy(
                titleId = R.string.label_eshare_stopped_title,
                descriptionId = R.string.label_eshare_stopped_message
            )
        }

        EShareStoppedReason.HOTSPOT_ERROR -> _uiState.update {
            it.copy(
                titleId = R.string.label_eshare_connection_unsuccessful_title,
                descriptionId = R.string.label_eshare_stopped_hotspot_setup_error_message,
            )
        }

        else -> {}
    }
}
