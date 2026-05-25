package kite1412.portaltik.common

import org.koin.dsl.module

val commonModule = module {
    single { AppCoroutineScope() }
}