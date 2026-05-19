package kite1412.portaltik

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(
            onCloseRequest = {
                exitApplication()
            },
            title = "Parking Gate Control"
        ) {
            PortalTikApp()
        }
    }
}