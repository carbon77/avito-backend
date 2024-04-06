package com.zakat.avitobackend.repository;

import com.zakat.avitobackend.model.Banner
import org.springframework.data.jpa.repository.JpaRepository

interface BannerRepository : JpaRepository<Banner, Int> {
}