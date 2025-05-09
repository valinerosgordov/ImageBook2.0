package ru.imagebook.server.adm.config

import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Component
class LocalCorsConfigurationSource : UrlBasedCorsConfigurationSource() {
    // TODO dev mode only
    init {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:8070", "http://admin.test.imagebook.ru")
            allowedMethods = listOf("GET", "PUT", "POST", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
        }
        registerCorsConfiguration("/**", configuration)
    }
}