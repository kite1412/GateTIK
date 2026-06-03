package kite1412.gatetik.network.backend

import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.network.backend.datasource.BackendAccessLogDataSource
import kite1412.gatetik.network.backend.datasource.BackendCctvDataSource
import kite1412.gatetik.network.backend.datasource.BackendGateDataSource
import kite1412.gatetik.network.backend.datasource.BackendParkingQuotaDataSource
import kite1412.gatetik.network.backend.datasource.BackendUserDataSource
import kite1412.gatetik.network.domain.datasource.AccessLogRemoteDataSource
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource
import kite1412.gatetik.network.domain.datasource.GateRemoteDataSource
import kite1412.gatetik.network.domain.datasource.ParkingQuotaRemoteDataSource
import kite1412.gatetik.network.domain.datasource.UserRemoteDataSource
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

    single {
        BackendUserDataSource()
    } bind UserRemoteDataSource::class
}