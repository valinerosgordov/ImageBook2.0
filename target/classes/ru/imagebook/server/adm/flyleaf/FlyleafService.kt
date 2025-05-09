package ru.imagebook.server.adm.flyleaf

import com.minogin.util.toFilename
import com.minogin.util.uniquePath
import org.apache.commons.io.FilenameUtils.getName
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.imagebook.server.config.FlyleafConfig
import ru.imagebook.server.repository.FlyleafRepository
import ru.imagebook.shared.model.Flyleaf
import java.io.File
import javax.transaction.Transactional

interface FlyleafServiceI {
    fun list(): List<Flyleaf>
    fun add()
    fun delete(flyleafId: Int)
    fun update(flyleafId: Int, flyleaf: Flyleaf, appImageFile: MultipartFile?)
}

@Service
class FlyleafService(
        val flyleafConfig: FlyleafConfig,
        val repository: FlyleafRepository
) : FlyleafServiceI {
    @Transactional
    override fun list(): List<Flyleaf> {
        return repository.findAll(Sort.by(Flyleaf.NUMBER))
    }

    @Transactional
    override fun add() {
        val flyleaf = Flyleaf()
        flyleaf.innerName = "Новый"
        repository.save(flyleaf)
    }

    @Transactional
    override fun delete(flyleafId: Int) {
        if (flyleafId != Flyleaf.DEFAULT_ID)
            repository.deleteById(flyleafId)
    }

    @Transactional
    override fun update(flyleafId: Int, flyleaf: Flyleaf, appImageFile: MultipartFile?) {
        flyleaf.id = flyleafId

        if (appImageFile != null) {
            val fileName = toFilename(appImageFile.originalFilename)
            val path = uniquePath(flyleafConfig.appImagePath(fileName))
            File(path).parentFile.mkdirs()
            appImageFile.transferTo(File(path))
            flyleaf.appImageFilename = getName(path)
        }

        repository.save(flyleaf)
    }
}