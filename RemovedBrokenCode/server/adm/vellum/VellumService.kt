package ru.imagebook.server.adm.vellum

import com.minogin.util.toFilename
import com.minogin.util.uniquePath
import org.apache.commons.io.FilenameUtils.getName
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.imagebook.server.config.VellumConfig
import ru.imagebook.server.repository.VellumRepository
import ru.imagebook.shared.model.Vellum
import java.io.File
import javax.transaction.Transactional

interface VellumServiceI {
    fun list(): List<Vellum>
    fun add()
    fun delete(vellumId: Int)
    fun update(vellumId: Int, vellum: Vellum, appImageFile: MultipartFile?)
}

@Service
class VellumService(
        val vellumConfig: VellumConfig,
        val repository: VellumRepository
) : VellumServiceI {
    @Transactional
    override fun list(): List<Vellum> {
        return repository.findAll(Sort.by(Vellum.NUMBER))
    }

    @Transactional
    override fun add() {
        val vellum = Vellum()
        vellum.innerName = "Новый"
        repository.save(vellum)
    }

    @Transactional
    override fun delete(vellumId: Int) {
        repository.deleteById(vellumId)
    }

    @Transactional
    override fun update(vellumId: Int, vellum: Vellum, appImageFile: MultipartFile?) {
        vellum.id = vellumId

        if (appImageFile != null) {
            val fileName = toFilename(appImageFile.originalFilename)
            val path = uniquePath(vellumConfig.appImagePath(fileName))
            File(path).parentFile.mkdirs()
            appImageFile.transferTo(File(path))
            vellum.appImageFilename = getName(path)
        }

        repository.save(vellum)
    }
}