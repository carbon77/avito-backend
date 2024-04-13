package com.zakat.avitobackend.repository

import com.zakat.avitobackend.model.Banner
import com.zakat.avitobackend.model.BannerHistory
import org.springframework.data.jpa.repository.JpaRepository

interface BannerHistoryRepository : JpaRepository<BannerHistory, Int> {
    fun findByBanner(banner: Banner): List<BannerHistory>
}