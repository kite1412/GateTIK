package kite1412.portaltik.network.backend

import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.network.backend.datasource.BackendAccessLogDataSource
import kite1412.portaltik.network.backend.datasource.BackendCctvDataSource
import kite1412.portaltik.network.backend.datasource.BackendGateDataSource
import kite1412.portaltik.network.backend.datasource.BackendParkingQuotaDataSource
import kite1412.portaltik.network.domain.datasource.AccessLogRemoteDataSource
import kite1412.portaltik.network.domain.datasource.CctvRemoteDataSource
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource
import kite1412.portaltik.network.domain.datasource.ParkingQuotaRemoteDataSource
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

    single {
        BackendParkingQuotaDataSource()
    } bind ParkingQuotaRemoteDataSource::class

    single {
        BackendAccessLogDataSource()
    } bind AccessLogRemoteDataSource::class
}