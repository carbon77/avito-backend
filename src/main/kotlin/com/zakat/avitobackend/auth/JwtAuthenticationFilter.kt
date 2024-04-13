package com.zakat.avitobackend.auth

import com.zakat.avitobackend.service.JwtService
import com.zakat.avitobackend.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
) : OncePerRequestFilter() {
    companion object {
        val publicApis: List<String> = listOf("/auth/**", "/docs/**", "/swagger-ui/**", "/v3/api-docs/**")
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val requestMatcher = OrRequestMatcher(publicApis.map {
            AntPathRequestMatcher(it, null)
        })
        return requestMatcher.matches(request)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not authorized")
            return
        }

        val jwt: String = authHeader.substring(7)
        val userEmail: String? = jwtService.extractUsername(jwt)
        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val user = userService.loadUserByUsername(userEmail)

            if (jwtService.isTokenValid(jwt, user)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.authorities
                )

                authToken.details = WebAuthenticationDetailsSource()
                    .buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}