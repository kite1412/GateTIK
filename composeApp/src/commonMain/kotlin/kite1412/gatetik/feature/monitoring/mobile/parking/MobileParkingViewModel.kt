package kite1412.gatetik.feature.monitoring.mobile.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.ui.util.stateIn

class MobileParkingViewModel(
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : ViewModel() {
    val mainParkingQuota = getMainParkingQuotaUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
}