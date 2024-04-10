package com.zakat.avitobackend.controller

import com.zakat.avitobackend.dto.BannerDto
import com.zakat.avitobackend.dto.CreateBannerRequest
import com.zakat.avitobackend.dto.CreateBannerResponse
import com.zakat.avitobackend.dto.PatchBannerRequest
import com.zakat.avitobackend.service.BannerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BannerController(
    private val bannerService: BannerService,
) {

    @GetMapping("/user_banner", produces = ["application/json"])
    fun findUserBanners(
        @RequestParam("tag_id", required = true) tagId: Int,
        @RequestParam("feature_id", required = true) featureId: Int,
        @RequestParam("use_last_revision") useLastRevision: Boolean = false,
    ): Any {
        return bannerService.findUserBanners(tagId, featureId, useLastRevision)
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

    @DeleteMapping("/banner/{id}")
    fun deleteById(@PathVariable("id") bannerId: Int): ResponseEntity<Any> {
        bannerService.deleteById(bannerId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/banner/{id}")
    fun patchBanner(@RequestBody req: PatchBannerRequest, @PathVariable("id") bannerId: Int) {
        bannerService.patchBanner(bannerId, req)
    }
}