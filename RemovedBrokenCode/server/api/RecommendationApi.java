package ru.imagebook.server.api;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.imagebook.server.service.*;
import ru.imagebook.shared.model.site.Recommendation;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by zinchenko on 14.09.14.
 */
@Controller
@RequestMapping("/recommendation")
public class RecommendationApi extends BaseGenericRestController<Recommendation, Integer> {

    @Autowired
    private FileService fileService;

    private RecommendationService recommendationService;

    @Autowired
    protected RecommendationApi(RecommendationService service) {
        super(service);
        this.recommendationService = service;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    Recommendation save(MultipartFile file, Recommendation recommendation) throws IOException {
        String fileName = fileService.saveFile(FilePathConfig.RECOMMENDATION_ENTITY, file);
        recommendation.setImageName(fileName);
        recommendationService.save(recommendation);
        return recommendation;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    Recommendation update(@RequestBody Recommendation entity) throws NoEntityPermissionException {
        return super.update(entity);
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
