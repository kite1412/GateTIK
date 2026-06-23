package kite1412.gatetik.feature.monitoring.mobile.parking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.model.ParkingQuota
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

class MobileParkingViewModel(
    private val getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _mainParkingQuota = MutableStateFlow<LoadState<ParkingQuota?>>(LoadState.Loading())
    val mainParkingQuota = _mainParkingQuota.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        getMainParkingQuotaUseCase.observeAsLoadStateFlow()
            .onEach { _mainParkingQuota.value = it }
            .launchIn(viewModelScope)
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            val res = getMainParkingQuotaUseCase.observeAsLoadStateFlow()
                .first { it !is LoadState.Loading }

            if (res is LoadState.Success)
                _mainParkingQuota.value = res
            else if (res is LoadState.Error)
                _uiEvent.showSnackbar("Gagal memuat ulang")

            isRefreshing = false
        }
    }
}
