package kite1412.gatetik

import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import kite1412.gatetik.datastore.createDataStore
import kite1412.gatetik.datastore.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        createDataStore()
    }

    single {
        UnsupportedJvmLocationService()
    } bind LocationService::class
}

private fun createDataStore() = createDataStore(
    storage = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = {
            File(
                System.getProperty("user.home"),
                ".gatetik/$dataStoreFileName"
            )
        }
    )
)