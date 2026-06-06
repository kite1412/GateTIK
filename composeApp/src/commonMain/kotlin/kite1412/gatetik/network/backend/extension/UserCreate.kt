package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.domain.model.UserCreate
import kite1412.gatetik.network.backend.dto.model.BackendUserRole
import kite1412.gatetik.network.backend.dto.model.BackendUserStatus
import kite1412.gatetik.network.backend.dto.request.BackendUserCreateRequest

fun UserCreate.toCreateRequest() = BackendUserCreateRequest(
    fullName = fullName,
    email = email,
    password = password,
    npmNip = institutionNumber,
    phoneNumber = phoneNumber,
    role = BackendUserRole.valueOf(role.name),
    status = BackendUserStatus.valueOf(status.name),
    profilePhoto = profilePhoto,
    ktmPath = ktmPath,
    lastLoginAt = lastLoginAt
)