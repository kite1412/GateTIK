package kite1412.portaltik.datastore

import org.koin.dsl.module

val dataStoreModule = module {
    single {
        PortalTikDataStore(dataStore = get())
    }
}