package com.zakat.avitobackend.repository;

import com.zakat.avitobackend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User
}