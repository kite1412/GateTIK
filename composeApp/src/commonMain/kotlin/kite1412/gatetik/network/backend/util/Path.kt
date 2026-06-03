package kite1412.gatetik.network.backend.util

/**
 * Utility function to standardize paths using the /api prefix for Laravel backend.
 */
fun getPath(path: String): String = "/api/${path.removePrefix("/")}"