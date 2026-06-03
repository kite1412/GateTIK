package kite1412.gatetik.network.domain.util

import kite1412.gatetik.util.Error

data class ServerError(override val message: String = "Server Error") : Error
