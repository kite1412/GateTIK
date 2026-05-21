package kite1412.portaltik.ui.util

interface ScaffoldComponentsController {
    fun getVisibilityState(component: ScaffoldComponent): Boolean
    fun showComponent(component: ScaffoldComponent)
    fun hideComponent(component: ScaffoldComponent)
}

enum class ScaffoldComponent {
    NAV_BAR
}