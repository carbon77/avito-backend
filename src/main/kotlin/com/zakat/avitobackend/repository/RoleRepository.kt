package com.zakat.avitobackend.repository;

import com.zakat.avitobackend.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RoleRepository : JpaRepository<Role, Long> {
    fun findAllByNameIn(names: List<String>): List<Role>
}