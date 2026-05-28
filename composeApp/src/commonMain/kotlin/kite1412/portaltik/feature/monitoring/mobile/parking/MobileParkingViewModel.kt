package kite1412.portaltik.feature.monitoring.mobile.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.ui.util.stateIn

class MobileParkingViewModel(
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : ViewModel() {
    val mainParkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)
}