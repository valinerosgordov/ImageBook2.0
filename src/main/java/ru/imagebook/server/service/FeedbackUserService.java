package ru.imagebook.server.service;

import ru.imagebook.shared.model.site.FeedbackUser;

/**
 * Created by zinchenko on 09.10.14.
 */
public interface FeedbackUserService extends Service<FeedbackUser, Integer> {

    FeedbackUser findFeedbackUser(Integer internalUserId);

}
