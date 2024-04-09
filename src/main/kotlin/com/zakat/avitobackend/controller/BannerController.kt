package com.zakat.avitobackend.controller

import com.zakat.avitobackend.dto.BannerDto
import com.zakat.avitobackend.dto.CreateBannerRequest
import com.zakat.avitobackend.dto.CreateBannerResponse
import com.zakat.avitobackend.service.BannerService
import org.springframework.web.bind.annotation.*

@RestController
class BannerController(
    private val bannerService: BannerService,
) {

    @GetMapping("/user_banner")
    fun findUserBanners(): String {
        return "User banner"
    }

    @PostMapping("/banner")
    fun create(
        @RequestBody request: CreateBannerRequest
    ): CreateBannerResponse {
        return bannerService.createBanner(request)
    }

    @GetMapping("/banner")
    fun findAllByFilters(
        @RequestParam("feature_id") featureId: Int?,
        @RequestParam("tag_id") tagId: Int?,
        @RequestParam("limit") limit: Int?,
        @RequestParam("offset") offset: Int?,
    ): List<BannerDto> {
        return bannerService.findAllByFilters(
            featureId, tagId, limit, offset
        )
    }
}