package com.zakat.avitobackend.service

import com.zakat.avitobackend.model.Banner
import com.zakat.avitobackend.model.BannerHistory
import com.zakat.avitobackend.repository.BannerHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BannerHistoryService(
    private val bannerHistoryRepository: BannerHistoryRepository,
) {

    @Transactional
    fun create(banner: Banner) {
        val history = bannerHistoryRepository.findByBanner(banner).sortedByDescending { it.id }
        if (history.size >= 3) {
            bannerHistoryRepository.delete(history.last())
        }
        val bannerHistory = BannerHistory(
            banner = banner,
            content = banner.content,
            isActive = banner.isActive,
            updatedAt = banner.updatedAt,
            createdAt = banner.createdAt,
            featureId = banner.feature?.id,
            tagIds = banner.tags.mapNotNull { it.id }.toIntArray()
        )
        bannerHistoryRepository.save(bannerHistory)
    }
}