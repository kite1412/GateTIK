package kite1412.gatetik.datastore.extension

import kite1412.gatetik.datastore.model.DataStoreUser
import kite1412.gatetik.model.User

fun User.toDataStoreUser() = DataStoreUser(
    id = id,
    fullName = fullName,
    email = email,
    role = role.name,
    status = status.name,
    instituteNumber = institutionNumber,
    createdAt = createdAt
)