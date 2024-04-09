package com.zakat.avitobackend.dto

import com.fasterxml.jackson.databind.JsonNode
import java.sql.Timestamp

data class BannerDto(
    var bannerId: Int?,
    var tagIds: List<Int?>,
    var featureId: Int?,
    var content: JsonNode?,
    var isActive: Boolean?,
    var createdAt: Timestamp?,
    var updatedAt: Timestamp?,
)