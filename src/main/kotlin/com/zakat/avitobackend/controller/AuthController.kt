package com.zakat.avitobackend.controller

import com.zakat.avitobackend.dto.AuthResponse
import com.zakat.avitobackend.dto.LoginRequest
import com.zakat.avitobackend.dto.RegisterRequest
import com.zakat.avitobackend.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization API")
class AuthController(
    private val authService: AuthService,
) {

    @Operation(summary = "Создание нового пользователя")
    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): AuthResponse {
        return authService.register(req)
    }

    @Operation(summary = "Получение токена")
    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): AuthResponse {
        return authService.login(req)
    }
}