package com.zakat.avitobackend.model

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.sql.Timestamp

@Entity
@Table(name = "banners")
data class Banner(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "banner_id")
    val id: Int? = null,

    @Column(columnDefinition = "jsonb")
    @Type(JsonType::class)
    val content: Any? = null,

    val isActive: Boolean? = null,
    val createdAt: Timestamp? = null,
    val updateAt: Timestamp? = null,

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
)