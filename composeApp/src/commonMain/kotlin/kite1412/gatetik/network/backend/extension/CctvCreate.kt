package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.network.backend.dto.request.BackendCctvCreateRequest

fun CctvCreate.toCreateRequest() = BackendCctvCreateRequest(
    cameraName = cameraName,
    path = path,
    streamUrl = streamUrl,
    type = type.toModel()
)