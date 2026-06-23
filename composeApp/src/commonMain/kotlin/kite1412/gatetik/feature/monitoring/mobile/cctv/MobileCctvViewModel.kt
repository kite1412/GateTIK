package kite1412.gatetik.feature.monitoring.mobile.cctv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.domain.usecase.GetCctvUseCase
import kite1412.gatetik.ui.util.stateIn

class MobileCctvViewModel(
    getCctvUseCase: GetCctvUseCase
) : ViewModel() {
    val mainCctv = getCctvUseCase.observeMainAsLoadStateFlow().stateIn(viewModelScope)
    val cctvs = getCctvUseCase.observeAllAsLoadStateFlow().stateIn(viewModelScope)
}