package kite1412.gatetik.feature.monitoring.mobile.cctv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.domain.usecase.GetMainCctvUseCase
import kite1412.gatetik.ui.util.stateIn

class MobileCctvViewModel(
    getMainCctvUseCase: GetMainCctvUseCase
) : ViewModel() {
    val mainCctv = getMainCctvUseCase().stateIn(viewModelScope)
}