package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.repository.FeedbackAnswerRepository;
import ru.imagebook.shared.model.site.FeedbackAnswer;

import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by zinchenko on 18.09.14.
 */
public class FeedbackAnswerServiceImpl extends BaseGenericService<FeedbackAnswer, Integer> implements FeedbackAnswerService {
    @Autowired
    public FeedbackAnswerServiceImpl(FeedbackAnswerRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public void save(FeedbackAnswer entity) {
        entity.setAnswerDate(new Date());
        super.save(entity);
    }
}
