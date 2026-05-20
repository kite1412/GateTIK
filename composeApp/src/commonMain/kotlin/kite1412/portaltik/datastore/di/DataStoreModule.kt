package kite1412.portaltik.datastore.di

import kite1412.portaltik.datastore.PortalTikDataStore
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        PortalTikDataStore(dataStore = get())
    }
}