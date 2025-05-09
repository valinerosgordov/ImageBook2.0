package ru.imagebook.server.auth

import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

const val SESSION_EXPIRATION_HOURS: Long = 24

@Service
class SharedAuthService {
    val sessions = CacheBuilder.newBuilder()
            .expireAfterWrite(SESSION_EXPIRATION_HOURS, TimeUnit.HOURS)
            .build<String, SharedUser>()


    fun startSession(user: SharedUser) {
        val token = UUID.randomUUID().toString()
        sessions.put(token, user)
    }

    fun auth(token: String): SharedUser? {
        return sessions.getIfPresent(token)
    }
}