package com.zakat.avitobackend.repository

import com.zakat.avitobackend.model.Banner
import com.zakat.avitobackend.model.Feature
import com.zakat.avitobackend.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface BannerRepository : JpaRepository<Banner, Int>, JpaSpecificationExecutor<Banner> {
    fun deleteByFeature(feature: Feature): Int
    fun deleteByTags(tags: List<Tag>): Int
}