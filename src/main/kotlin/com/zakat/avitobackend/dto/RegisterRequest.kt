package com.zakat.avitobackend.dto

data class RegisterRequest(
    var email: String,
    var password: String,
    var roles: List<String>,
)
