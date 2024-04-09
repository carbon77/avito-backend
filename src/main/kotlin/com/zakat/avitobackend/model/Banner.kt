package com.zakat.avitobackend.model

import com.fasterxml.jackson.databind.JsonNode
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Type
import java.sql.Timestamp

@Entity
@Table(name = "banners")
data class Banner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    val id: Int? = null,

    @Column(columnDefinition = "jsonb")
    @Type(JsonType::class)
    val content: JsonNode? = null,

    val isActive: Boolean? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "feature_id", nullable = false)
    var feature: Feature? = null,

    @ManyToMany
    @JoinTable(
        name = "banners_tags",
        joinColumns = [JoinColumn(name = "banner_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableList<Tag> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Banner

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}