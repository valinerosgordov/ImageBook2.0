package ru.imagebook.server.repository;

import ru.imagebook.shared.model.site.FeedbackUser;

/**
 * Created by zinchenko on 09.10.14.
 */
public interface FeedbackUserRepository extends Repository<FeedbackUser, Integer> {

    FeedbackUser findFeedbackUser(Integer internalUserId);

}
