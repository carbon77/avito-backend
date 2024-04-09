package com.zakat.avitobackend.utils

import com.zakat.avitobackend.model.Banner
import com.zakat.avitobackend.model.Feature
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

class BannerSpecifications {
    companion object {
        fun hasFeatureId(featureId: Int): Specification<Banner> = Specification { root, query, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Feature>("feature").get<Int>("id"), featureId)
        }

        fun hasTagId(tagId: Int): Specification<Banner> = Specification { root, query, criteriaBuilder ->
            val subRoot = root.join<Any, Any>("tags", JoinType.INNER)
            criteriaBuilder.equal(subRoot.get<Int>("id"), criteriaBuilder.literal(tagId))
        }
    }
}