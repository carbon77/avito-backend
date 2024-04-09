package com.zakat.avitobackend.repository

import com.zakat.avitobackend.model.Banner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface BannerRepository : JpaRepository<Banner, Int>, JpaSpecificationExecutor<Banner>