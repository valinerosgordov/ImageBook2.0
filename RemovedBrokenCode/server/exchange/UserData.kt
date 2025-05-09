package ru.imagebook.server.exchange

data class UserData(
        val username: String,
        val roles: List<RoleData>
)