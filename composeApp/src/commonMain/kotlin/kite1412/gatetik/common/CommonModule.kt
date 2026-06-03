package kite1412.gatetik.common

import org.koin.dsl.module

val commonModule = module {
    single { AppCoroutineScope() }
}