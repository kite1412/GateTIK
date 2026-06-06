package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.network.backend.dto.model.BackendUserRole
import kite1412.gatetik.network.backend.dto.model.BackendUserStatus
import kite1412.gatetik.network.backend.dto.request.BackendUserUpdateRequest

fun UserUpdate.toUpdateRequest() = BackendUserUpdateRequest(
    fullName = fullName,
    password = password,
    npmNip = npmNip,
    phoneNumber = phoneNumber,
    role = BackendUserRole.valueOf(role.name),
    status = BackendUserStatus.valueOf(status.name),
    ktmPath = ktmPath
)
