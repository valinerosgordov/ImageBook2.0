package ru.imagebook.server.auth

data class SharedUser(
        val id: Int,
        val active: Boolean,
        val username: String
)