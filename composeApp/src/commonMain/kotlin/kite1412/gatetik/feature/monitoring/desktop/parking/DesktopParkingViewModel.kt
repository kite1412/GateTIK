package kite1412.gatetik.feature.monitoring.desktop.parking

import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.ui.util.stateIn

class DesktopParkingViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    val parkingQuota = getMainParkingQuotaUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
}