package kite1412.portaltik.datastore.extension

import kite1412.portaltik.datastore.model.DataStoreUser
import kite1412.portaltik.model.User

fun User.toDataStoreUser() = DataStoreUser(
    id = id,
    fullName = fullName,
    email = email,
    role = role.name,
    status = status.name,
    instituteNumber = instituteNumber
)