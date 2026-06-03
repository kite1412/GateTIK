package kite1412.portaltik.feature.monitoring.desktop.dashboard

import kite1412.portaltik.datastore.GateTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel

class DesktopDashboardViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore)