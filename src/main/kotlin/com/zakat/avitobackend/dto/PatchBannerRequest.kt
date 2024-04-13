package com.zakat.avitobackend.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class PatchBannerRequest(
    @JsonProperty("tag_ids")
    var tagIds: List<Int>?,

    @JsonProperty("feature_id")
    var featureId: Int?,
    var content: JsonNode?,

    @JsonProperty("is_active")
    var isActive: Boolean?,
)
