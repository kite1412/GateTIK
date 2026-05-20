package kite1412.portaltik

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kite1412.portaltik.app.PortalTikApp
import kite1412.portaltik.di.initKoin

fun main() {
    val koin = initKoin()

    application {
        Window(
            onCloseRequest = {
                koin.close()
                exitApplication()
            },
            title = "Parking Gate Control"
        ) {
            PortalTikApp()
        }
    }
}