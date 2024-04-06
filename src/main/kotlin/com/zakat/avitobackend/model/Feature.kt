package com.zakat.avitobackend.model

import jakarta.persistence.*

@Entity
@Table(name = "features")
data class Feature(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "feature_id")
    val id: Int? = null,
)