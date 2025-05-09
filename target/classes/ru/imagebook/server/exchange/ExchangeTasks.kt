package ru.imagebook.server.exchange

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ExchangeTasks(
        val exchangeService: IExchangeService,

        @Value("\${exchange.loadRenderedAlbums}")
        val loadRenderedAlbums: Boolean
) {
    @Scheduled(fixedDelayString = "\${exchange.loadRenderedAlbumsDelaySec}000")
    fun loadRenderedAlbumsTask() {
        if (loadRenderedAlbums)
            exchangeService.loadRenderedAlbums()
    }
}