package ru.imagebook.server.adm.vellum

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.imagebook.server.adm.config.API_URL
import ru.imagebook.shared.model.Vellum

@RestController
@RequestMapping("$API_URL/vellums")
class VellumController(val service: VellumServiceI) {
    @GetMapping("/")
    fun vellums(): List<Vellum> {
        return service.list()
    }

    @PostMapping("/add")
    fun addVellum() {
        service.add()
    }

    @PostMapping("/{vellumId}/delete")
    fun deleteVellum(@PathVariable vellumId: Int) {
        service.delete(vellumId)

    }

    @PostMapping("/{vellumId}/update")
    fun updateVellum(@PathVariable vellumId: Int, @RequestPart vellum: Vellum, @RequestPart(required = false) appImageFile: MultipartFile?) {
        service.update(vellumId, vellum, appImageFile)
    }
}
