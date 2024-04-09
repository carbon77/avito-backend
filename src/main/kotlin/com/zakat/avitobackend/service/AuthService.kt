package com.zakat.avitobackend.service

import com.zakat.avitobackend.dto.AuthResponse
import com.zakat.avitobackend.dto.LoginRequest
import com.zakat.avitobackend.dto.RegisterRequest
import com.zakat.avitobackend.model.User
import com.zakat.avitobackend.repository.RoleRepository
import com.zakat.avitobackend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        val roles = roleRepository.findAllByNameIn(request.roles)
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            roles = roles,
        )

        userRepository.save(user)
        val token = jwtService.generateToken(user)
        return AuthResponse(token)
    }

    fun login(request: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email, request.password
            )
        )
        val user = userService.loadUserByUsername(request.email)
        val jwtToken = jwtService.generateToken(user)
        return AuthResponse(jwtToken)
    }
}