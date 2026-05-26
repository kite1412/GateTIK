package kite1412.portaltik.network.domain.util

import kite1412.portaltik.util.Error

data class ServerError(override val message: String = "Server Error") : Error
