package kite1412.gatetik.ui.util

import kotlinx.coroutines.flow.MutableSharedFlow

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}

suspend fun MutableSharedFlow<UiEvent>.showSnackbar(message: String) {
    emit(UiEvent.ShowSnackbar(message))
}
