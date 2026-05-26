package kite1412.portaltik.ui.util

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}
