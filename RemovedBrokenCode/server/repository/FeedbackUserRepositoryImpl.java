package ru.imagebook.server.repository;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.imagebook.shared.model.site.FeedbackUser;

/**
 * Created by zinchenko on 09.10.14.
 */
@Repository
public class FeedbackUserRepositoryImpl extends BaseGenericRepository<FeedbackUser, Integer>
        implements FeedbackUserRepository{

    @Override
    public FeedbackUser findFeedbackUser(Integer internalUserId) {
        return (FeedbackUser) createQuery("from FeedbackUser where feedbackInternalUser.id = :internalUserId")
                .setInteger("internalUserId", internalUserId)
                .uniqueResult();
    }
}
