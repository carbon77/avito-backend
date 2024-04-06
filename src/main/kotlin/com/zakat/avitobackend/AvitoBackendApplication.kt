package com.zakat.avitobackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
class AvitoBackendApplication

fun main(args: Array<String>) {
	runApplication<AvitoBackendApplication>(*args)
}
