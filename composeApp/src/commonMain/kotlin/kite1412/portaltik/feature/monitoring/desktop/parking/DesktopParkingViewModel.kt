package kite1412.portaltik.feature.monitoring.desktop.parking

import androidx.lifecycle.viewModelScope
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.portaltik.ui.util.stateIn

class DesktopParkingViewModel(
    authentication: Authentication,
    dataStore: PortalTikDataStore,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    val parkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)
}