package kite1412.portaltik.feature.monitoring.desktop.cctv

import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel

class DesktopCctvViewModel(
    authentication: Authentication,
    dataStore: PortalTikDataStore
) : DesktopBaseViewModel(authentication, dataStore)