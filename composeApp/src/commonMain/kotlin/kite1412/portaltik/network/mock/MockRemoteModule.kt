package kite1412.portaltik.network.mock

import kite1412.portaltik.network.domain.datasource.IotDeviceRemoteDataSource
import kite1412.portaltik.network.mock.datasource.MockIotDeviceRemoteDataSource
import org.koin.dsl.bind
import org.koin.dsl.module

val mockRemoteModule = module {
//    single {
//        MockAuthentication()
//    } bind Authentication::class

//    single {
//        MockCctvRemoteDataSource()
//    } bind CctvRemoteDataSource::class

//    single {
//        MockGateRemoteDataSource()
//    } bind GateRemoteDataSource::class

    single {
        MockIotDeviceRemoteDataSource()
    } bind IotDeviceRemoteDataSource::class

//    single {
//        MockParkingQuotaRemoteDataSource()
//    } bind ParkingQuotaRemoteDataSource::class

//    single {
//        MockAccessLogRemoteDataSource()
//    } bind AccessLogRemoteDataSource::class
}