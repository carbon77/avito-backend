package com.zakat.avitobackend.model

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import com.fasterxml.jackson.databind.JsonNode
import io.hypersistence.utils.hibernate.type.array.IntArrayType
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.sql.Timestamp

@Entity
@Table(name = "banner_history")
data class BannerHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_history_id")
    var id: Int? = null,

    @Column(columnDefinition = "jsonb")
    @Type(JsonType::class)
    var content: JsonNode? = null,

    var isActive: Boolean? = null,
    val createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null,
    var featureId: Int? = null,

    @Type(IntArrayType::class)
    @Column(name = "tag_ids", columnDefinition = "integer[]")
    val tagIds: IntArray = intArrayOf(),

    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "banner_id", nullable = false)
    @JsonIncludeProperties(value = ["id"])
    var banner: Banner? = null,
)
