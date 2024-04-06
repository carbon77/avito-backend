package com.zakat.avitobackend.repository;

import com.zakat.avitobackend.model.Feature
import org.springframework.data.jpa.repository.JpaRepository

interface FeatureRepository : JpaRepository<Feature, Int> {
}