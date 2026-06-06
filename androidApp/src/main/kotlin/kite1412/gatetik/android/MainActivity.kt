package kite1412.gatetik.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import kite1412.gatetik.CsvExporter
import kite1412.gatetik.LocalCsvExporter
import kite1412.gatetik.app.GateTikApp
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val csvExporter by inject<CsvExporter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalCsvExporter provides csvExporter
            ) {
                GateTikApp()
            }
        }
    }
}

@Preview
@Composable
fun GateTikAppPreview() {
    GateTikApp()
}