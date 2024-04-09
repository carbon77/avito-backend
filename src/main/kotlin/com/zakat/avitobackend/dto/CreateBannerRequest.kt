package com.zakat.avitobackend.dto

import com.fasterxml.jackson.databind.JsonNode

data class CreateBannerRequest(
    var tagIds: List<Int>,
    var featureId: Int,
    var content: JsonNode,
    var isActive: Boolean,
)
