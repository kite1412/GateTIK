package kite1412.gatetik.common.extension

fun <Key, Value> MutableMap<Key, Value>.includeIfNotNull(key: Key, value: Value?) {
    if (value != null) set(key, value)
}