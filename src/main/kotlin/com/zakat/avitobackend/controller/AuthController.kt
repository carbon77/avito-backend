package com.zakat.avitobackend.controller

import com.zakat.avitobackend.dto.AuthResponse
import com.zakat.avitobackend.dto.LoginRequest
import com.zakat.avitobackend.dto.RegisterRequest
import com.zakat.avitobackend.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): AuthResponse {
        return authService.register(req)
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): AuthResponse {
        return authService.login(req)
    }
}