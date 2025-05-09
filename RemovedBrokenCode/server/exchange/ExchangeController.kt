package ru.imagebook.server.exchange

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

// FIXME Authentication!!!
@RestController("")
class ExchangeController(
        val exchangeService: IExchangeService
) {
    @PostMapping("album/{albumId}/sheet")
    fun addSheets(@PathVariable albumId: String) = exchangeService.addSheets(albumId)

    @DeleteMapping("album/{albumId}/sheet")
    fun deleteSheets(@PathVariable albumId: String) = exchangeService.deleteSheets(albumId)
}