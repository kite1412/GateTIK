package kite1412.portaltik.feature.monitoring.desktop.usermanagement

import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel

class DesktopUserManagementViewModel(
    authentication: Authentication,
    dataStore: PortalTikDataStore
) : DesktopBaseViewModel(authentication, dataStore) {
}