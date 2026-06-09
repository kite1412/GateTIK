package kite1412.gatetik.feature.monitoring.desktop.profile

import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel

class DesktopProfileViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore) {
}