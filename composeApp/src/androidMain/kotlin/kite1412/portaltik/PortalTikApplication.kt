package kite1412.portaltik

import android.app.Application
import kite1412.portaltik.di.initKoin
import org.koin.android.ext.koin.androidContext

class PortalTikApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@PortalTikApplication)
        }
    }
}