package com.zakat.avitobackend.repository;

import com.zakat.avitobackend.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Int> {
}