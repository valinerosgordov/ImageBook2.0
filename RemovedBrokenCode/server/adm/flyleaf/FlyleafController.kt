package ru.imagebook.server.adm.flyleaf

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.imagebook.server.adm.config.API_URL
import ru.imagebook.shared.model.Flyleaf

@RestController
@RequestMapping("$API_URL/flyleafs")
class FlyleafController(val service: FlyleafServiceI) {
    @GetMapping("/")
    fun flyleafs(): List<Flyleaf> {
        return service.list()
    }

    @PostMapping("/add")
    fun addFlyleaf() {
        service.add()
    }

    @PostMapping("/{flyleafId}/delete")
    fun deleteFlyleaf(@PathVariable flyleafId: Int) {
        service.delete(flyleafId)

    }

    @PostMapping("/{flyleafId}/update")
    fun updateFlyleaf(@PathVariable flyleafId: Int, @RequestPart flyleaf: Flyleaf, @RequestPart(required = false) appImageFile: MultipartFile?) {
        service.update(flyleafId, flyleaf, appImageFile)
    }
}
