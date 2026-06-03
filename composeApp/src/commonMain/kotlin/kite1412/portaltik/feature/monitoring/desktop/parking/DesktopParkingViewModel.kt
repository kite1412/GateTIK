package kite1412.portaltik.feature.monitoring.desktop.parking

import androidx.lifecycle.viewModelScope
import kite1412.portaltik.datastore.GateTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.portaltik.ui.util.stateIn

class DesktopParkingViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    val parkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)
}