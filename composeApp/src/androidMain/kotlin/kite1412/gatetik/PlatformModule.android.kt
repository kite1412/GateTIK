package kite1412.gatetik

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import kite1412.gatetik.datastore.createDataStore
import kite1412.gatetik.datastore.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        createDataStore(context = get())
    }

    single {
        AndroidLocationService(
            appScope = get(),
            context = get()
        )
    } bind LocationService::class

    single {
        UnsupportedAndroidCsvExporter()
    } bind CsvExporter::class
}

private fun createDataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        storage = FileStorage(
            serializer = PreferencesFileSerializer,
            produceFile = { context.filesDir.resolve(dataStoreFileName) }
        )
    )