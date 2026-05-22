package kite1412.portaltik.app.util

import androidx.compose.ui.unit.DpSize
import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.ScaffoldComponentState
import kite1412.portaltik.ui.util.ScaffoldComponentsController

class AppScaffoldComponentsController(
    private val componentStates: MutableMap<ScaffoldComponent, ScaffoldComponentState>
) : ScaffoldComponentsController {
    init {
        ScaffoldComponent.entries.forEach { component ->
            componentStates[component] = ScaffoldComponentState()
        }
    }

    override fun getState(component: ScaffoldComponent): ScaffoldComponentState =
        componentStates[component] ?: ScaffoldComponentState()

    override fun showComponent(component: ScaffoldComponent) {
        ensureExists(component)
        componentStates[component] = componentStates[component]!!.copy(
            isVisible = true
        )
    }

    override fun hideComponent(component: ScaffoldComponent) {
        ensureExists(component)
        componentStates[component] = componentStates[component]!!.copy(
            isVisible = false
        )
    }

    override fun updateComponentSize(
        component: ScaffoldComponent,
        size: DpSize
    ) {
        val state = getState(component)

        componentStates[component] = state.copy(size = size)
    }

    private fun ensureExists(component: ScaffoldComponent) {
        val state = componentStates[component]

        if (state == null) componentStates[component] = ScaffoldComponentState()
    }
}