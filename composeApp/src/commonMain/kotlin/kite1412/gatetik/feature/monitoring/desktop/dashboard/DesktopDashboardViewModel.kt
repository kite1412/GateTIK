package kite1412.gatetik.feature.monitoring.desktop.dashboard

import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel

class DesktopDashboardViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore)