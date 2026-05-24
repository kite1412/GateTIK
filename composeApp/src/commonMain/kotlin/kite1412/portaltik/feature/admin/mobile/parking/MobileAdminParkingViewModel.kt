package kite1412.portaltik.feature.admin.mobile.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.ui.util.stateIn

class MobileAdminParkingViewModel(
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : ViewModel() {
    val mainParkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)
}