package ru.imagebook.server.config

import org.springframework.stereotype.Service
import ru.imagebook.server.service.FileConfig

@Service
class FlyleafConfig(val fileConfig: FileConfig) {
    fun appImagePath(appImageFilename: String) = fileConfig.path + "/flyleaf/images/app/" + appImageFilename

    fun sliderImagePath(sliderImageFilename: String) = fileConfig.path + "/flyleaf/images/slider/" + sliderImageFilename
}