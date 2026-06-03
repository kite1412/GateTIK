package kite1412.gatetik.ui.util

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}
