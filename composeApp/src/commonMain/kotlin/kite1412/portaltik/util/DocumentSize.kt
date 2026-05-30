package kite1412.portaltik.util

import kite1412.portaltik.FileSize

val Int.mb: FileSize
    get() = FileSize(this * 1024L * 1024L)