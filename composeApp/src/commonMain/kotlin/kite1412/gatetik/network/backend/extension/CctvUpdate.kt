package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.network.backend.dto.request.BackendCctvUpdateRequest

fun CctvUpdate.toUpdateRequest() = BackendCctvUpdateRequest(
    cameraName = cameraName,
    path = path,
    streamUrl = streamUrl,
    type = type?.toModel()
)