package kite1412.gatetik.datastore

import org.koin.dsl.module

val dataStoreModule = module {
    single {
        GateTikDataStore(dataStore = get())
    }
}