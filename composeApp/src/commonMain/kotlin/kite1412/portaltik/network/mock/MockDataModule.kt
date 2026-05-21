package kite1412.portaltik.network.mock

import kite1412.portaltik.domain.Authentication
import org.koin.dsl.bind
import org.koin.dsl.module

val mockRemoteModule = module {
    single {
        MockAuthentication()
    } bind Authentication::class
}