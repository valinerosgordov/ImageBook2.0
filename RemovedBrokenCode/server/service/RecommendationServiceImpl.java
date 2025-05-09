package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.repository.RecommendationRepository;
import ru.imagebook.server.repository.Repository;
import ru.imagebook.shared.model.site.Recommendation;

/**
 * Created by zinchenko on 21.09.14.
 */
public class RecommendationServiceImpl
        extends BaseGenericService<Recommendation, Integer>
        implements RecommendationService {

    private RecommendationRepository recommendationRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepository repository) {
        super(repository);
        recommendationRepository = repository;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NoEntityPermissionException {
        Recommendation recommendation = recommendationRepository.find(id);
        deleteFileOfRecommendation(recommendation);
        super.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Recommendation entity) throws NoEntityPermissionException {
        deleteFileOfRecommendation(entity);
        super.delete(entity);
    }

    protected void deleteFileOfRecommendation(Recommendation recommendation) {
        fileService.deleteFile(FilePathConfig.RECOMMENDATION_ENTITY, recommendation.getImageName());
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
