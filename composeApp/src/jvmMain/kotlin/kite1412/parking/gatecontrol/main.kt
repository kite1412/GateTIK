package kite1412.parking.gatecontrol

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
            ParkingGateControlApp()
        }
    }
}