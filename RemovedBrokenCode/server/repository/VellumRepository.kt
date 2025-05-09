package ru.imagebook.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.imagebook.shared.model.Vellum

@Repository
interface VellumRepository : JpaRepository<Vellum, Int> {
    fun findByActiveTrueOrderByNumber(): List<Vellum>
}