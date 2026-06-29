package kite1412.gatetik.feature.monitoring.mobile.intercom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.domain.usecase.GetCctvUseCase
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.showSnackbar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MobileIntercomViewModel(
    private val getCctvUseCase: GetCctvUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _intercomCameras = MutableStateFlow<LoadState<List<Cctv>>>(LoadState.Loading())
    val intercomCameras = _intercomCameras.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadIntercomCameras()
    }

    private fun loadIntercomCameras() {
        getCctvUseCase.observeAllAsLoadStateFlow()
            .onEach { state ->
                @Suppress("UNCHECKED_CAST")
                _intercomCameras.value = if (state is LoadState.Success) {
                    LoadState.Success(state.data.filter { it.type == CctvType.INTERCOM })
                } else {
                    state as LoadState<List<Cctv>>
                }
            }
            .launchIn(viewModelScope)
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            val res = getCctvUseCase.observeAllAsLoadStateFlow()
                .first { it !is LoadState.Loading }

            if (res is LoadState.Success) {
                _intercomCameras.value = LoadState.Success(res.data.filter { it.type == CctvType.INTERCOM })
            } else if (res is LoadState.Error) {
                _uiEvent.showSnackbar("Gagal memuat ulang")
            }
            isRefreshing = false
        }
    }
}
