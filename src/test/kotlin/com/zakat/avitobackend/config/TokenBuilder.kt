package com.zakat.avitobackend.config

import com.zakat.avitobackend.dto.LoginRequest
import com.zakat.avitobackend.service.AuthService
import org.springframework.stereotype.Component

@Component
class TokenBuilder(
    private val authService: AuthService,
) {

    fun adminToken(): String {
        return authService.login(LoginRequest("admin@example.com", "admin")).token
    }

    fun userToken(): String {
        return authService.login(LoginRequest("user@example.com", "user")).token
    }
}