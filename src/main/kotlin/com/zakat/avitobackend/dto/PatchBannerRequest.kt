package com.zakat.avitobackend.dto

import com.fasterxml.jackson.databind.JsonNode

data class PatchBannerRequest(
    var tagIds: List<Int>?,
    var featureId: Int?,
    var content: JsonNode?,
    var isActive: Boolean?,
)
