package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.imagebook.server.repository.BaseGenericRepository;
import ru.imagebook.server.repository.FeedbackUserRepository;
import ru.imagebook.server.repository.Repository;
import ru.imagebook.shared.model.site.FeedbackUser;

/**
 * Created by zinchenko on 09.10.14.
 */
public class FeedbackUserServiceImpl extends BaseGenericService<FeedbackUser, Integer>
        implements FeedbackUserService {

    private FeedbackUserRepository feedbackUserRepository;

    @Autowired
    public FeedbackUserServiceImpl(FeedbackUserRepository repository) {
        super(repository);
        feedbackUserRepository = repository;
    }

    @Override
    public FeedbackUser findFeedbackUser(Integer internalUserId) {
        return feedbackUserRepository.findFeedbackUser(internalUserId);
    }

}
