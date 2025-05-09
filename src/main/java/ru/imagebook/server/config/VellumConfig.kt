package ru.imagebook.server.config

import org.springframework.stereotype.Service
import ru.imagebook.server.service.FileConfig

@Service
class VellumConfig(val fileConfig: FileConfig) {
    fun appImagePath(appImageFilename: String) = fileConfig.path + "/vellum/images/app/" + appImageFilename

    fun sliderImagePath(sliderImageFilename: String) = fileConfig.path + "/vellum/images/slider/" + sliderImageFilename
}