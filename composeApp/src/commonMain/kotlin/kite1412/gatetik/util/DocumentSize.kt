package kite1412.gatetik.util

import kite1412.gatetik.FileSize

val Int.mb: FileSize
    get() = FileSize(this * 1024L * 1024L)