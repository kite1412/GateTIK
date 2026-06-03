package kite1412.portaltik.feature.monitoring.desktop.cctv

import kite1412.portaltik.datastore.GateTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel

class DesktopCctvViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore)