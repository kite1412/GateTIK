package kite1412.gatetik.data

import kite1412.gatetik.data.repository.AccessLogRepositoryImpl
import kite1412.gatetik.data.repository.CctvRepositoryImpl
import kite1412.gatetik.data.repository.GateRepositoryImpl
import kite1412.gatetik.data.repository.ParkingQuotaRepositoryImpl
import kite1412.gatetik.data.repository.UserRepositoryImpl
import kite1412.gatetik.domain.repository.AccessLogRepository
import kite1412.gatetik.domain.repository.CctvRepository
import kite1412.gatetik.domain.repository.GateRepository
import kite1412.gatetik.domain.repository.ParkingQuotaRepository
import kite1412.gatetik.domain.repository.UserRepository
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
            remoteDataSource = get(),
            parkingQuotaRepositoryImpl = get()
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

    single {
        UserRepositoryImpl(
            remoteDataSource = get()
        )
    } bind UserRepository::class
}