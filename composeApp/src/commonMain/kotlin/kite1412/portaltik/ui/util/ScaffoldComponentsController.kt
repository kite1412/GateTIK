package kite1412.portaltik.ui.util

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

interface ScaffoldComponentsController {
    fun getState(component: ScaffoldComponent): ScaffoldComponentState
    fun showComponent(component: ScaffoldComponent)
    fun hideComponent(component: ScaffoldComponent)
    fun updateComponentSize(component: ScaffoldComponent, size: DpSize)
}

enum class ScaffoldComponent {
    NAV_BAR
}

data class ScaffoldComponentState(
    val isVisible: Boolean = true,
    val size: DpSize = DpSize(0.dp, 0.dp)
)

val MockScaffoldComponentController = object : ScaffoldComponentsController {
    override fun getState(component: ScaffoldComponent): ScaffoldComponentState =
        ScaffoldComponentState()
    override fun showComponent(component: ScaffoldComponent) {}
    override fun hideComponent(component: ScaffoldComponent) {}
    override fun updateComponentSize(
        component: ScaffoldComponent,
        size: DpSize
    ) {}
}