package kite1412.portaltik.feature.monitoring.mobile.cctv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.usecase.GetMainCctvUseCase
import kite1412.portaltik.ui.util.stateIn

class MobileCctvViewModel(
    getMainCctvUseCase: GetMainCctvUseCase
) : ViewModel() {
    val mainCctv = getMainCctvUseCase().stateIn(viewModelScope)
}