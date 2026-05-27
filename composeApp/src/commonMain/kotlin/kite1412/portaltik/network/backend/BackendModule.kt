package kite1412.portaltik.network.backend

import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.network.backend.datasource.BackendCctvDataSource
import kite1412.portaltik.network.backend.datasource.BackendGateDataSource
import kite1412.portaltik.network.domain.datasource.CctvRemoteDataSource
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource
import org.koin.dsl.bind
import org.koin.dsl.module

val backendModule = module {
    single {
        BackendAuthentication(
            appScope = get(),
            dataStore = get()
        )
    } bind Authentication::class

    single {
        BackendGateDataSource()
    } bind GateRemoteDataSource::class

    single {
        BackendCctvDataSource()
    } bind CctvRemoteDataSource::class
}