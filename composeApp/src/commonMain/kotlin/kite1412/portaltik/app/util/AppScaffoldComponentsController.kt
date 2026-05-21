package kite1412.portaltik.app.util

import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.ScaffoldComponentsController

class AppScaffoldComponentsController(
    private val visibilityMap: MutableMap<ScaffoldComponent, Boolean>
) : ScaffoldComponentsController {
    init {
        ScaffoldComponent.entries.forEach { component ->
            visibilityMap[component] = true
        }
    }

    override fun getVisibilityState(component: ScaffoldComponent): Boolean =
        visibilityMap[component] ?: false

    override fun showComponent(component: ScaffoldComponent) {
        visibilityMap[component] = true
    }

    override fun hideComponent(component: ScaffoldComponent) {
        visibilityMap[component] = false
    }
}