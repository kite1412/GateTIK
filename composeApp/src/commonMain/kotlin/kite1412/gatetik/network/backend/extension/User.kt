package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.model.User
import kite1412.gatetik.network.backend.dto.request.BackendProfileUpdateRequest

fun User.toUpdateRequest() = BackendProfileUpdateRequest(
    id = id,
    fullName = fullName,
    email = email,
    npmNip = institutionNumber,
    phoneNumber = phoneNumber
)