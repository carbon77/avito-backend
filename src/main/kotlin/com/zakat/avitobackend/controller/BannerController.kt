package com.zakat.avitobackend.controller

import com.zakat.avitobackend.dto.BannerDto
import com.zakat.avitobackend.dto.CreateBannerRequest
import com.zakat.avitobackend.dto.CreateBannerResponse
import com.zakat.avitobackend.dto.PatchBannerRequest
import com.zakat.avitobackend.service.BannerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.CompletableFuture

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

    @DeleteMapping("/banner")
    fun deleteByFeatureOrTag(
        @RequestParam("feature_id") featureId: Int?,
        @RequestParam("tag_id") tagId: Int?
    ): ResponseEntity<Any> {
        if (featureId == null && tagId == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You must set feature_id or tag_id!")
        }

        if (featureId != null && tagId != null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You can set only one param!")
        }

        CompletableFuture.runAsync {
            if (featureId != null) {
                bannerService.deleteByFeature(featureId)
            } else if (tagId != null) {
                bannerService.deleteByTag(tagId)
            }
        }

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/banner/{id}")
    fun patchBanner(@RequestBody req: PatchBannerRequest, @PathVariable("id") bannerId: Int) {
        bannerService.patchBanner(bannerId, req)
    }
}