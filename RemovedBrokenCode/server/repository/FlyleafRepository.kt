package ru.imagebook.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.imagebook.shared.model.Flyleaf

@Repository
interface FlyleafRepository : JpaRepository<Flyleaf, Int> {
    fun findByActiveTrueOrderByNumber(): List<Flyleaf>
}