package kite1412.portaltik.data

import kite1412.portaltik.data.repository.AccessLogRepositoryImpl
import kite1412.portaltik.data.repository.CctvRepositoryImpl
import kite1412.portaltik.data.repository.GateRepositoryImpl
import kite1412.portaltik.data.repository.ParkingQuotaRepositoryImpl
import kite1412.portaltik.domain.repository.AccessLogRepository
import kite1412.portaltik.domain.repository.CctvRepository
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.domain.repository.ParkingQuotaRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        CctvRepositoryImpl(
            remoteDataSource = get()
        )
    } bind CctvRepository::class

    single {
        GateRepositoryImpl(
            remoteDataSource = get()
        )
    } bind GateRepository::class

    single {
        ParkingQuotaRepositoryImpl(
            remoteDataSource = get()
        )
    } bind ParkingQuotaRepository::class

    single {
        AccessLogRepositoryImpl(
            remoteDataSource = get()
        )
    } bind AccessLogRepository::class
}