package ru.imagebook.server.repository;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import ru.imagebook.shared.model.site.Feedback;

import java.util.List;

/**
 * Created by zinchenko on 07.09.14.
 */
@Repository
public class FeedbackRepositoryImpl extends BaseGenericRepository<Feedback, Integer> implements FeedbackRepository {

    @Override
    public List<Feedback> getRange(Integer from, Integer to) {
        return createCriteria(Feedback.class)
                .addOrder(Order.desc(Feedback.CREATE_DATE))
                .setFirstResult(from)
                .setMaxResults(to-from)
                .list();
    }

}
