package ru.imagebook.server.exchange

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class ExchangeConfig(
        @Value("\${exchange.url}")
        val url: String,

        @Value("\${exchange.token}")
        val token: String,

        @Value("\${exchange.render.ftp.host}")
        val renderHost: String,

        @Value("\${exchange.render.ftp.user}")
        val renderUser: String,

        @Value("\${exchange.render.ftp.password}")
        val renderPassword: String,

        @Value("\${exchange.render.path}")
        val renderPath: String
)