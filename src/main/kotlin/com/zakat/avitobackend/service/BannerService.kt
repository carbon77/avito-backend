package com.zakat.avitobackend.service

import com.zakat.avitobackend.dto.BannerDto
import com.zakat.avitobackend.dto.CreateBannerRequest
import com.zakat.avitobackend.dto.CreateBannerResponse
import com.zakat.avitobackend.dto.PatchBannerRequest
import com.zakat.avitobackend.model.Banner
import com.zakat.avitobackend.model.BannerHistory
import com.zakat.avitobackend.model.Feature
import com.zakat.avitobackend.model.Tag
import com.zakat.avitobackend.repository.BannerHistoryRepository
import com.zakat.avitobackend.repository.BannerRepository
import com.zakat.avitobackend.repository.FeatureRepository
import com.zakat.avitobackend.repository.TagRepository
import com.zakat.avitobackend.utils.BannerSpecifications.Companion.hasFeatureId
import com.zakat.avitobackend.utils.BannerSpecifications.Companion.hasTagId
import com.zakat.avitobackend.utils.BannerSpecifications.Companion.isActive
import com.zakat.avitobackend.utils.OffsetBasedPageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.allOf
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.lang.Thread.sleep
import java.sql.Timestamp
import java.time.Instant

@Service
class BannerService(
    private val bannerRepository: BannerRepository,
    private val tagRepository: TagRepository,
    private val featureRepository: FeatureRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val bannerHistoryRepository: BannerHistoryRepository,
    private val bannerHistoryService: BannerHistoryService,
) {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun createBanner(req: CreateBannerRequest): CreateBannerResponse {
        val feature = Feature(id = req.featureId)
        val tags: MutableList<Tag> = req.tagIds.map { Tag(id = it) } as MutableList<Tag>

        tagRepository.saveAll(tags)
        featureRepository.save(feature)

        val banner = Banner(
            feature = feature,
            tags = tags,
            content = req.content,
            isActive = req.isActive,
            createdAt = Timestamp.from(Instant.now()),
            updatedAt = Timestamp.from(Instant.now()),
        )
        bannerRepository.save(banner)

        return CreateBannerResponse(
            bannerId = banner.id,
        )
    }

    @Transactional(readOnly = true)
    fun findAllByFilters(
        featureId: Int?,
        tagId: Int?,
        limit: Int?,
        offset: Int?,
    ): List<BannerDto> {
        val predicates = mutableListOf<Specification<Banner>>()

        if (offset != null && offset < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Offset must be greater then 0")
        }

        if (limit != null && limit < 1) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater then 1")
        }

        featureId?.let {
            predicates.add(hasFeatureId(featureId))
        }

        tagId?.let {
            predicates.add(hasTagId(tagId))
        }

        val whereClause = allOf(predicates)
        val pageable = OffsetBasedPageRequest(limit ?: Int.MAX_VALUE, offset ?: 0)
        return bannerRepository.findAll(whereClause, pageable).toList().map { banner ->
            BannerDto(
                bannerId = banner.id,
                tagIds = banner.tags.map { it.id }.toList(),
                featureId = banner.feature?.id,
                content = banner.content,
                isActive = banner.isActive,
                createdAt = banner.createdAt,
                updatedAt = banner.updatedAt,
            )
        }
    }

    @Transactional(readOnly = true)
    fun findUserBanners(tagId: Int, featureId: Int, useLastRevision: Boolean): Any {
        if (useLastRevision) {
            val query = "SELECT content FROM banners " +
                    "WHERE feature_id=$featureId AND is_active AND " +
                    "$tagId IN (SELECT tag_id FROM banners_tags WHERE banners_tags.banner_id=banners.banner_id);"

            val resultSet = jdbcTemplate.query(query) { rs, rowNum -> rs.getString("content") }
            if (resultSet.isEmpty()) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Banner not found")
            }

            return resultSet[0]
        }

        return bannerRepository.findAll(
            where(
                isActive().and(hasFeatureId(featureId)).and(hasTagId(tagId))
            )
        ).firstOrNull()?.content ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Banner not found")
    }

    @Transactional
    fun deleteById(bannerId: Int) {
        if (!bannerRepository.existsById(bannerId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Banner not found")
        }

        bannerRepository.deleteById(bannerId)
    }

    @Transactional
    fun patchBanner(bannerId: Int, req: PatchBannerRequest) {
        val banner = bannerRepository.findById(bannerId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Banner not found")
        }

        bannerHistoryService.create(banner)

        banner.content = req.content ?: banner.content
        banner.isActive = req.isActive ?: banner.isActive

        req.featureId?.let {
            val newFeature = Feature(id = req.featureId)
            featureRepository.save(newFeature)
            banner.feature = newFeature
        }

        req.tagIds?.let {
            val newTags: MutableList<Tag> = req.tagIds!!.map {
                val tag = Tag(id = it)
                tagRepository.save(tag)
                tag
            }.toMutableList()
            banner.tags = newTags
        }

        if (req.isActive != null || req.content != null || req.featureId != null || req.tagIds != null) {
            banner.updatedAt = Timestamp.from(Instant.now())
        }

        bannerRepository.save(banner)
    }

    @Transactional
    fun deleteByFeature(featureId: Int) {
        sleep(2000)
        val feature = featureRepository.findById(featureId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Feature not found")
        }
        bannerRepository.deleteByFeature(feature)
    }

    @Transactional
    fun deleteByTag(tagId: Int) {
        sleep(2000)
        val tag = tagRepository.findById(tagId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")
        }
        bannerRepository.deleteByTags(listOf(tag))
    }

    @Transactional(readOnly = true)
    fun findHistoryById(bannerId: Int): List<BannerHistory> {
        val banner = bannerRepository.findById(bannerId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Banner not found")
        }
        return bannerHistoryRepository.findByBanner(banner)
    }
}