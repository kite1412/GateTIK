package kite1412.portaltik

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import kite1412.portaltik.datastore.createDataStore
import kite1412.portaltik.datastore.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import org.videolan.libvlc.LibVLC

actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        createDataStore(context = get())
    }

    single {
        LibVLC(get())
    }

    single {
        AndroidLocationService(
            appScope = get(),
            context = get()
        )
    } bind LocationService::class
}

private fun createDataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        storage = FileStorage(
            serializer = PreferencesFileSerializer,
            produceFile = { context.filesDir.resolve(dataStoreFileName) }
        )
    )